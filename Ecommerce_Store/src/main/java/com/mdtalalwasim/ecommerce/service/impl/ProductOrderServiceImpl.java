package com.mdtalalwasim.ecommerce.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdtalalwasim.ecommerce.entity.Cart;
import com.mdtalalwasim.ecommerce.entity.OrderAddress;
import com.mdtalalwasim.ecommerce.entity.ProductOrder;
import com.mdtalalwasim.ecommerce.entity.ProductOrderRequest;
import com.mdtalalwasim.ecommerce.repository.CartRepository;
import com.mdtalalwasim.ecommerce.repository.ProductOrderRepository;
import com.mdtalalwasim.ecommerce.service.ProductOrderService;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

	@Autowired
	private ProductOrderRepository productOrderRepository;

	@Autowired
	private CartRepository cartRepository;

	@Override
	public ProductOrder saveProductOrder(Long userId, ProductOrderRequest productOrderRequest) {

		//get which products are ordered by user
		List<Cart> listOfCarts = cartRepository.findByUserId(userId);

		if (listOfCarts == null || listOfCarts.isEmpty()) {
			return null;
		}

		//save the shipping address once and share it across all orders of this checkout
		OrderAddress orderAddress = OrderAddress.builder()
				.firstName(productOrderRequest.getFirstName())
				.lastName(productOrderRequest.getLastName())
				.email(productOrderRequest.getEmail())
				.mobile(productOrderRequest.getMobile())
				.address(productOrderRequest.getAddress())
				.city(productOrderRequest.getCity())
				.state(productOrderRequest.getState())
				.pinCode(productOrderRequest.getPinCode())
				.build();

		boolean isOnlinePayment = "Online Payment".equalsIgnoreCase(productOrderRequest.getPaymentType());

		ProductOrder firstSavedOrder = null;

		for (Cart cart : listOfCarts) {

			ProductOrder order = new ProductOrder();

			order.setOrderId(UUID.randomUUID().toString());
			order.setOrderDate(new Date());

			order.setProduct(cart.getProduct());
			order.setPrice(cart.getProduct().getDiscountPrice());
			order.setQuantity(cart.getQuantity());

			order.setUser(cart.getUser());
			order.setStatus("In Progress");

			order.setPaymentType(productOrderRequest.getPaymentType());
			order.setOrderAddress(orderAddress);

			if (isOnlinePayment) {
				//Razorpay order is created by the controller; status becomes PAID after verification
				order.setPaymentStatus("PENDING");
			} else {
				order.setPaymentStatus(null); //Cash on Delivery
			}

			ProductOrder savedOrder = productOrderRepository.save(order);
			if (firstSavedOrder == null) {
				firstSavedOrder = savedOrder;
			}
		}

		//empty the cart once the order(s) are placed
		if (firstSavedOrder != null) {
			cartRepository.deleteAll(listOfCarts);
		}

		return firstSavedOrder;
	}

}
