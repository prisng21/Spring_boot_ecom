package com.mdtalalwasim.ecommerce.entity;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String orderId;
	private Date orderDate;
	
	@ManyToOne
	private Product product;
	
	private Double price;
	
	private Integer quantity;
	
	@ManyToOne
	private User user;
	
	private String status;
	
	private String paymentType;
	
	// Razorpay payment tracking
	private String razorpayOrderId;
	private String razorpayPaymentId;
	private String paymentStatus; // PENDING, PAID, FAILED
	
	@OneToOne(cascade = CascadeType.ALL)
	private OrderAddress orderAddress;
	
	
	
	
}
