package com.mdtalalwasim.ecommerce.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mdtalalwasim.ecommerce.entity.Cart;
import com.mdtalalwasim.ecommerce.entity.Category;
import com.mdtalalwasim.ecommerce.entity.ProductOrder;
import com.mdtalalwasim.ecommerce.entity.ProductOrderRequest;
import com.mdtalalwasim.ecommerce.entity.User;
import com.mdtalalwasim.ecommerce.repository.ProductOrderRepository;
import com.mdtalalwasim.ecommerce.service.CartService;
import com.mdtalalwasim.ecommerce.service.CategoryService;
import com.mdtalalwasim.ecommerce.service.ProductOrderService;
import com.mdtalalwasim.ecommerce.service.RazorpayService;
import com.mdtalalwasim.ecommerce.service.UserService;
import com.razorpay.RazorpayException;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	UserService userService;	@Autowired
	CartService cartService;
	
	@Autowired
	ProductOrderService productOrderService;
	
	@Autowired
	RazorpayService razorpayService;
	
	@Autowired
	ProductOrderRepository productOrderRepository;
	
	
	//to track which user is login right Now
	//by default call this method when any request come to this controller because of @ModelAttribut
	@ModelAttribute 
	public void getUserDetails(Principal principal, Model model) {
		if(principal != null) {
			String currenLoggedInUserEmail = principal.getName();
			User currentUserDetails = userService.getUserByEmail(currenLoggedInUserEmail);
			//System.out.println("Current Logged In User is :: USER Controller :: "+currentUserDetails.toString());
			model.addAttribute("currentLoggedInUserDetails",currentUserDetails);
			
			//for showing user cart count
			Long countCartForUser = cartService.getCounterCart(currentUserDetails.getId());
			//System.out.println("User Cart Count :"+countCartForUser);
			model.addAttribute("countCartForUser", countCartForUser);
		}
		
		List<Category> allActiveCategory = categoryService.findAllActiveCategory();
		model.addAttribute("allActiveCategory",allActiveCategory);
		
	}
	
	
	@GetMapping("/")
	public String home(){
		return "user/user-home";
	}
	
	
	//ADD TO CART Module
	@GetMapping("/add-to-cart")
	String addToCart(@RequestParam Long productId, @RequestParam Long userId, HttpSession session) {
		System.out.println("INSIDE ITS");
		Cart saveCart = cartService.saveCart(productId, userId);
		
		//System.out.println("save Cart is :"+saveCart);
		if(ObjectUtils.isEmpty(saveCart)) {
			System.out.println("INSIDE Error");
			session.setAttribute("errorMsg", "Failed Product add to Cart");
		}else {
			session.setAttribute("successMsg", "Successfully, Product added to Cart");
		}
		System.out.println("pid"+productId+" uid:"+userId);
		return "redirect:/product/" + productId;
	}
	
	@GetMapping("/cart")
	String loadCartPage(Principal principal, Model model) {
		//when load cart, it is showing logged in user cart details:
		
		
		User user = getLoggedUserDetails(principal);
		List<Cart> carts = cartService.getCartsByUser(user.getId());
		model.addAttribute("carts", carts);
		if(carts.size() > 0) {
			Double totalOrderPrice = carts.get(carts.size()-1).getTotalOrderPrice();
			model.addAttribute("totalOrderPrice", totalOrderPrice);
		}
		
		
		return "/user/cart";
	}

	@GetMapping("/cart-quantity-update")
	public String updateCartQuantity(@RequestParam("symbol") String symbol , @RequestParam("cartId") Long cartId){
		System.out.println(symbol+ " " + cartId);
		Boolean f = cartService.updateCartQuantity(symbol, cartId);
		return "redirect:/user/cart";
	}

	private User getLoggedUserDetails(Principal principal) {
		String email = principal.getName();
		User user = userService.getUserByEmail(email);
		return user;
	}
	
	
	@GetMapping("/orders")
	public String orderPage(Principal principal, Model model) {
		//when load cart, it is showing logged in user cart details:
		
		
				User user = getLoggedUserDetails(principal);
				List<Cart> carts = cartService.getCartsByUser(user.getId());
				model.addAttribute("carts", carts);
				if(carts.size() > 0) {
					Double orderPrice = carts.get(carts.size()-1).getTotalOrderPrice();
					Double totalOrderPrice = carts.get(carts.size()-1).getTotalOrderPrice()+ 250+ 100;
					model.addAttribute("orderPrice", orderPrice);
					model.addAttribute("totalOrderPrice", totalOrderPrice);
				}
		return "/user/order";
	}
	
	/**
	 * Places the order(s) from the checkout form.
	 * Cash on Delivery -> orders are saved and the user is redirected home.
	 * Online Payment    -> orders are saved as PENDING, a Razorpay order is created
	 *                      for the FIRST order line and the checkout page is shown.
	 */
	@PostMapping("/create-order")
	public String createOrder(@ModelAttribute ProductOrderRequest productOrderRequest,
			Principal principal, Model model, HttpSession session) {
		
		User user = getLoggedUserDetails(principal);
		
		ProductOrder savedOrder = productOrderService.saveProductOrder(user.getId(), productOrderRequest);
		
		if (ObjectUtils.isEmpty(savedOrder)) {
			session.setAttribute("errorMsg", "Your cart is empty. Add products before placing an order.");
			return "redirect:/user/cart";
		}
		
		if ("Online Payment".equalsIgnoreCase(productOrderRequest.getPaymentType())) {
			try {
				String razorpayOrderId = razorpayService.createRazorpayOrder(savedOrder);
				
				//remember which Razorpay order belongs to this checkout
				savedOrder.setRazorpayOrderId(razorpayOrderId);
				productOrderRepository.save(savedOrder);
				
				model.addAttribute("razorpayOrderId", razorpayOrderId);
				model.addAttribute("razorpayKeyId", razorpayService.getKeyId());
				model.addAttribute("amountInPaise", Math.round(savedOrder.getPrice() * savedOrder.getQuantity() * 100));
				model.addAttribute("userEmail", user.getEmail());
				model.addAttribute("userMobile", user.getMobile() != null ? user.getMobile() : "");
				model.addAttribute("userName", user.getName());
				
				return "user/razorpay-checkout";
			} catch (RazorpayException e) {
				session.setAttribute("errorMsg", "Payment could not be initiated: " + e.getMessage());
				return "redirect:/user/orders";
			}
		}
		
		//Cash on Delivery path
		session.setAttribute("successMsg", "Order placed successfully!");
		return "redirect:/user/orders";
	}
	
	/**
	 * Called by the Razorpay checkout page after payment.
	 * Verifies the HMAC signature before marking the order PAID.
	 */
	@PostMapping("/verify-payment")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> verifyPayment(@RequestParam String razorpayOrderId,
			@RequestParam String razorpayPaymentId,
			@RequestParam String razorpaySignature,
			Principal principal) {
		
		Map<String, Object> response = new java.util.HashMap<>();
		
		ProductOrder order = productOrderRepository.findByRazorpayOrderId(razorpayOrderId);
		
		if (ObjectUtils.isEmpty(order)) {
			response.put("status", "failure");
			response.put("message", "Order not found for this payment");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		
		//only the owner of the order can verify its payment
		if (!order.getUser().getEmail().equalsIgnoreCase(principal.getName())) {
			response.put("status", "failure");
			response.put("message", "You are not allowed to verify this order");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
		
		boolean signatureValid = razorpayService.verifyPaymentSignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);
		
		if (signatureValid) {
			order.setRazorpayPaymentId(razorpayPaymentId);
			order.setPaymentStatus("PAID");
			order.setStatus("Paid, In Progress");
			productOrderRepository.save(order);
			
			response.put("status", "success");
			response.put("message", "Payment verified. Order placed successfully!");
			return ResponseEntity.ok(response);
		}
		
		order.setPaymentStatus("FAILED");
		productOrderRepository.save(order);
		
		response.put("status", "failure");
		response.put("message", "Payment verification failed");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	
}
