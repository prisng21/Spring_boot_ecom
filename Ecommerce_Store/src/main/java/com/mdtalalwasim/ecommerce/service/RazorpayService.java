package com.mdtalalwasim.ecommerce.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import com.mdtalalwasim.ecommerce.entity.ProductOrder;

/**
 * Razorpay payment gateway integration (Test mode).
 * Creates Razorpay orders server-side and verifies payment signatures
 * so fake success callbacks cannot mark orders as paid.
 */
@Service
public class RazorpayService {

	@Value("${razorpay.key.id}")
	private String keyId;

	@Value("${razorpay.key.secret}")
	private String keySecret;

	/**
	 * Exposes the public Key ID to the checkout page via the controller.
	 */
	public String getKeyId() {
		return keyId;
	}

	/**
	 * Creates a Razorpay order for the given application order.
	 * Amount is in rupees, Razorpay expects paise, so multiply by 100.
	 */
	public String createRazorpayOrder(ProductOrder order) throws RazorpayException {
		if (keySecret == null || keySecret.isBlank()) {
			throw new RazorpayException("Razorpay Key Secret is not configured. "
					+ "Set the RAZORPAY_KEY_SECRET environment variable and restart the application.");
		}
		
		RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);

		JSONObject orderRequest = new JSONObject();
		//total for this order line = unit price x quantity (in paise)
		double amountInPaise = Math.round(order.getPrice() * order.getQuantity() * 100);
		orderRequest.put("amount", (int) amountInPaise);
		orderRequest.put("currency", "INR");
		orderRequest.put("receipt", order.getOrderId());

		Order razorpayOrder = razorpayClient.orders.create(orderRequest);
		return razorpayOrder.get("id");
	}

	/**
	 * Verifies the checkout signature: HMAC_SHA256(razorpay_order_id + '|' + razorpay_payment_id, keySecret)
	 * matches razorpay_signature.
	 */
	public boolean verifyPaymentSignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
		try {
			String payload = razorpayOrderId + "|" + razorpayPaymentId;
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(keySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
			byte[] signatureBytes = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

			StringBuilder hexSignature = new StringBuilder();
			for (byte b : signatureBytes) {
				hexSignature.append(String.format("%02x", b));
			}

			return MessageDigest.isEqual(
					hexSignature.toString().getBytes(StandardCharsets.UTF_8),
					razorpaySignature.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			System.out.println("Razorpay signature verification failed: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Optionally fetches payment details directly from Razorpay as an extra check.
	 */
	public boolean isPaymentCaptured(String razorpayPaymentId) {
		try {
			RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);
			com.razorpay.Payment payment = razorpayClient.payments.fetch(razorpayPaymentId);
			return "captured".equals(payment.get("status"));
		} catch (RazorpayException e) {
			System.out.println("Razorpay payment fetch failed: " + e.getMessage());
			return false;
		}
	}
}
