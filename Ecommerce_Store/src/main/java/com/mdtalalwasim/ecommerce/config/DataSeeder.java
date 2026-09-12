package com.mdtalalwasim.ecommerce.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mdtalalwasim.ecommerce.entity.Category;
import com.mdtalalwasim.ecommerce.entity.Product;
import com.mdtalalwasim.ecommerce.repository.CategoryRepository;
import com.mdtalalwasim.ecommerce.repository.ProductRepository;

/**
 * Seeds sample Category and Product data on application startup.
 * Categories are seeded only when the table is empty; products are added
 * per-item (skipping any product whose title already exists), so new sample
 * items appear even when the database already has data.
 * Images referenced here already exist under static/img/category and static/img/product_image.
 */
@Component
public class DataSeeder implements CommandLineRunner {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public void run(String... args) throws Exception {

		// ---------- CATEGORIES ----------
		if (categoryRepository.count() == 0) {

			List<Category> categories = List.of(
					new Category(null, "Cloths", "cloths.png", true, null, null),
					new Category(null, "Electronics", "electronics.png", true, null, null),
					new Category(null, "Beauty", "beauty.png", true, null, null),
					new Category(null, "Grocery", "grocery.jpeg", true, null, null),
					new Category(null, "Books", "books.jpeg", true, null, null),
					new Category(null, "Mobiles", "mobile.jpg", true, null, null),
					new Category(null, "Accessories", "Accessories.jpg", true, null, null),
					new Category(null, "Footwear", "footwear.jpg", true, null, null),
					new Category(null, "Watches", "watches.jpg", true, null, null));

			categoryRepository.saveAll(categories);
			System.out.println("DataSeeder : " + categories.size() + " categories seeded.");
		} else {
			// Ensure individually-added categories exist even if the table already has data.
			List<Category> extraCategories = List.of(
					new Category(null, "Accessories", "Accessories.jpg", true, null, null),
					new Category(null, "Footwear", "footwear.jpg", true, null, null),
					new Category(null, "Watches", "watches.jpg", true, null, null));

			for (Category category : extraCategories) {
				if (!categoryRepository.existsByCategoryName(category.getCategoryName())) {
					categoryRepository.save(category);
					System.out.println("DataSeeder : category added - " + category.getCategoryName());
				}
			}
		}

		// ---------- PRODUCTS ----------
		if (productRepository.count() == 0) {

			// new Category(null, categoryName, categoryImage, isActive, createdAt, updatedAt)
			// new Product(null, title, description, category, price, stock, image, discount, discountPrice, isActive, createdAt, updatedAt)
			List<Product> products = List.of(
					new Product(null,
							"Men's Casual Shirt - Slim Fit Cotton",
							"Premium slim-fit casual shirt made from 100% breathable cotton. Perfect for office and casual outings. Machine washable, wrinkle resistant fabric with reinforced stitching.",
							"Cloths", 1499.00, 40, "CasualShirt.jpg", 20, 1199.20, true, null, null),

					new Product(null,
							"Men's Casual Shirt - Regular Fit",
							"Comfortable regular-fit casual shirt with soft finish and all-day comfort. Available in multiple colors. Durable buttons and double-stitched seams.",
							"Cloths", 1299.00, 35, "Casual-Shirt-Slim-Fit.jpg", 15, 1104.15, true, null, null),

					new Product(null,
							"Classic Formal Blazer",
							"Tailored formal blazer in a premium blend fabric. Ideal for business meetings, weddings and parties. Fully lined with modern slim cut.",
							"Cloths", 4999.00, 15, "Blazer.jpg", 25, 3749.25, true, null, null),

					new Product(null,
							"Men's Cotton T-Shirt - Round Neck",
							"Soft round-neck cotton t-shirt with breathable fabric. Pre-shrunk, color-fast and skin friendly. A wardrobe essential for every day wear.",
							"Cloths", 599.00, 100, "T-Shirt.jpg", 10, 539.10, true, null, null),

					new Product(null,
							"Men's Panjabi - Classic Design",
							"Traditional panjabi with classic collar design and premium cotton fabric. Elegant embroidery details. Perfect for Eid, festivals and special occasions.",
							"Cloths", 2499.00, 25, "panjabi.jpg", 20, 1999.20, true, null, null),

					new Product(null,
							"Men's Panjabi - Slim Fit",
							"Modern slim-fit panjabi with contemporary styling. High-quality fabric with comfortable fit for long events. Easy to wash and maintain.",
							"Cloths", 2799.00, 20, "panjabi-slim-fit.jpg", 15, 2379.15, true, null, null),

					new Product(null,
							"Winter Knit Wear Sweater",
							"Warm and stylish knitwear sweater for winter. Soft wool-blend yarn with ribbed cuffs and hem. Keeps you cozy without the bulk.",
							"Cloths", 1899.00, 30, "KnitWear.jpg", 30, 1329.30, true, null, null),

					new Product(null,
							"Laptop Pro 15 inch - 16GB RAM, 512GB SSD",
							"High performance laptop with 15 inch Full HD display, 16GB RAM and 512GB NVMe SSD. Ideal for programming, office work and light gaming. All-day battery life.",
							"Electronics", 85000.00, 10, "laptop.png", 10, 76500.00, true, null, null),

					new Product(null,
							"UltraBook Laptop 14 inch - Lightweight",
							"Feather-light UltraBook with 14 inch IPS display, backlit keyboard and fast charging. Perfect for students and professionals on the go.",
							"Electronics", 65000.00, 12, "laptopImg.jpeg", 8, 59800.00, true, null, null),

					new Product(null,
							"Men's Leather Belt - Formal Accessories",
							"Genuine leather formal belt with classic buckle. Scratch resistant and durable. Complements both formal and casual outfits.",
							"Accessories", 899.00, 50, "Accessories.jpg", 12, 791.12, true, null, null),

					new Product(null,
							"Wireless Bluetooth Headphones - Over Ear",
							"Deep bass wireless over-ear headphones with soft cushioned ear cups. Up to 40 hours playtime, Bluetooth 5.0, built-in mic for calls and AUX support.",
							"Electronics", 2999.00, 45, "headphones.jpg", 35, 1949.35, true, null, null),

					new Product(null,
							"Smart Watch - Fitness Tracker with Heart Rate Monitor",
							"Feature-packed smart watch with 1.4 inch HD touch display, heart-rate and SpO2 monitoring, step counter, sleep tracking and 7-day battery life. Water resistant.",
							"Watches", 4499.00, 60, "smartwatch.jpg", 40, 2699.40, true, null, null),

					new Product(null,
							"Men's Running Shoes - Lightweight Sports",
							"Lightweight running shoes with breathable mesh upper and cushioned sole for maximum comfort. Anti-skid grip perfect for gym, running and daily wear.",
							"Footwear", 3499.00, 55, "running-shoes.jpg", 30, 2449.30, true, null, null),

					new Product(null,
							"Men's Genuine Leather Wallet - Bi-Fold",
							"Premium bi-fold wallet made from genuine leather with multiple card slots, ID window and zip coin pocket. Slim design that fits comfortably in your pocket.",
							"Accessories", 1299.00, 70, "leather-wallet.jpg", 25, 974.25, true, null, null),

					new Product(null,
							"Unisex UV Protection Sunglasses",
							"Stylish unisex sunglasses with UV400 polarized lenses that protect your eyes from harmful rays. Lightweight frame suitable for driving, travel and outdoor sports.",
							"Accessories", 1799.00, 40, "sunglasses.jpg", 20, 1439.20, true, null, null),

					new Product(null,
							"Laptop Backpack - Water Resistant 15.6 inch",
							"Durable water-resistant backpack with padded 15.6 inch laptop compartment, USB charging port, anti-theft back pocket and multiple organizer compartments.",
							"Accessories", 2199.00, 35, "backpack.jpg", 22, 1715.22, true, null, null),

					new Product(null,
							"Premium Basmati Rice 5kg - Long Grain",
							"Aged premium basmati rice with long, slender grains and rich aroma. Perfect for biryani, pulao and everyday cooking. Naturally gluten-free.",
							"Grocery", 650.00, 80, "basmati-rice.jpg", 15, 552.50, true, null, null),

					new Product(null,
							"Organic Green Tea - 100 Tea Bags",
							"100% organic green tea bags rich in antioxidants. Supports metabolism and immunity. Smooth, refreshing taste without bitterness.",
							"Grocery", 399.00, 90, "green-tea.jpg", 20, 319.20, true, null, null),

					new Product(null,
							"Extra Virgin Olive Oil 1L - Cold Pressed",
							"First cold-pressed extra virgin olive oil with a rich, fruity flavor. Ideal for salads, cooking and dipping. Cholesterol free and heart healthy.",
							"Grocery", 1299.00, 45, "olive-oil.jpg", 10, 1169.10, true, null, null),

					new Product(null,
							"Pure Natural Honey 500g - Raw & Unprocessed",
							"Raw, unprocessed honey collected from natural sources. No added sugar or preservatives. Rich in antioxidants with a naturally sweet taste.",
							"Grocery", 749.00, 60, "honey.jpg", 25, 561.75, true, null, null),

					new Product(null,
							"The Great Gatsby - Classic Novel (Paperback)",
							"F. Scott Fitzgerald's timeless classic of the Jazz Age. A tale of love, wealth and the American dream. A must-read literary masterpiece.",
							"Books", 299.00, 100, "novel-book.jpg", 10, 269.10, true, null, null),

					new Product(null,
							"Atomic Habits - Build Good Habits & Break Bad Ones",
							"James Clear's bestselling guide to building good habits and breaking bad ones. Practical strategies backed by science for lasting self-improvement.",
							"Books", 599.00, 75, "self-help-book.jpg", 30, 419.30, true, null, null),

					new Product(null,
							"The Complete Cookbook for Beginners - 100+ Recipes",
							"Step-by-step cookbook with over 100 easy recipes for beginners. From breakfast to dinner, master essential cooking techniques with full-color photos.",
							"Books", 899.00, 50, "cookbook.jpg", 15, 764.15, true, null, null),

					new Product(null,
							"Gentle Foaming Face Wash - For All Skin Types",
							"Sulfate-free foaming face wash with aloe vera and vitamin E. Removes dirt, oil and impurities without drying the skin. Suitable for daily use.",
							"Beauty", 449.00, 85, "facewash.jpg", 20, 359.20, true, null, null),

					new Product(null,
							"Luxury Perfume for Men - Long Lasting Fragrance 100ml",
							"Eau de parfum with a bold, long-lasting fragrance blend of citrus, spice and woody notes. Perfect for office, parties and special occasions.",
							"Beauty", 2999.00, 30, "perfume.jpg", 35, 1949.35, true, null, null),

					new Product(null,
							"Smartphone 5G - 8GB RAM, 128GB Storage, 50MP Camera",
							"Sleek 5G smartphone with 6.7 inch AMOLED display, 8GB RAM, 128GB storage and 50MP AI triple camera. 5000mAh battery with fast charging support.",
							"Mobiles", 24999.00, 20, "smartphone.jpg", 12, 21999.12, true, null, null),

					new Product(null,
							"True Wireless Earbuds - Bluetooth 5.3 with ANC",
							"True wireless earbuds with active noise cancellation, Bluetooth 5.3 and touch controls. Up to 30 hours total playtime with the charging case. IPX5 water resistant.",
							"Mobiles", 3499.00, 65, "earbuds.jpg", 45, 1924.45, true, null, null));

			productRepository.saveAll(products);
			System.out.println("DataSeeder : " + products.size() + " products seeded.");
		} else {
			// Ensure individually-added products exist even if the table already has data.
			List<Product> extraProducts = List.of(
					new Product(null,
							"Wireless Bluetooth Headphones - Over Ear",
							"Deep bass wireless over-ear headphones with soft cushioned ear cups. Up to 40 hours playtime, Bluetooth 5.0, built-in mic for calls and AUX support.",
							"Electronics", 2999.00, 45, "headphones.jpg", 35, 1949.35, true, null, null),

					new Product(null,
							"Smart Watch - Fitness Tracker with Heart Rate Monitor",
							"Feature-packed smart watch with 1.4 inch HD touch display, heart-rate and SpO2 monitoring, step counter, sleep tracking and 7-day battery life. Water resistant.",
							"Watches", 4499.00, 60, "smartwatch.jpg", 40, 2699.40, true, null, null),

					new Product(null,
							"Men's Running Shoes - Lightweight Sports",
							"Lightweight running shoes with breathable mesh upper and cushioned sole for maximum comfort. Anti-skid grip perfect for gym, running and daily wear.",
							"Footwear", 3499.00, 55, "running-shoes.jpg", 30, 2449.30, true, null, null),

					new Product(null,
							"Men's Genuine Leather Wallet - Bi-Fold",
							"Premium bi-fold wallet made from genuine leather with multiple card slots, ID window and zip coin pocket. Slim design that fits comfortably in your pocket.",
							"Accessories", 1299.00, 70, "leather-wallet.jpg", 25, 974.25, true, null, null),

					new Product(null,
							"Unisex UV Protection Sunglasses",
							"Stylish unisex sunglasses with UV400 polarized lenses that protect your eyes from harmful rays. Lightweight frame suitable for driving, travel and outdoor sports.",
							"Accessories", 1799.00, 40, "sunglasses.jpg", 20, 1439.20, true, null, null),

					new Product(null,
							"Laptop Backpack - Water Resistant 15.6 inch",
							"Durable water-resistant backpack with padded 15.6 inch laptop compartment, USB charging port, anti-theft back pocket and multiple organizer compartments.",
							"Accessories", 2199.00, 35, "backpack.jpg", 22, 1715.22, true, null, null),

					new Product(null,
							"Premium Basmati Rice 5kg - Long Grain",
							"Aged premium basmati rice with long, slender grains and rich aroma. Perfect for biryani, pulao and everyday cooking. Naturally gluten-free.",
							"Grocery", 650.00, 80, "basmati-rice.jpg", 15, 552.50, true, null, null),

					new Product(null,
							"Organic Green Tea - 100 Tea Bags",
							"100% organic green tea bags rich in antioxidants. Supports metabolism and immunity. Smooth, refreshing taste without bitterness.",
							"Grocery", 399.00, 90, "green-tea.jpg", 20, 319.20, true, null, null),

					new Product(null,
							"Extra Virgin Olive Oil 1L - Cold Pressed",
							"First cold-pressed extra virgin olive oil with a rich, fruity flavor. Ideal for salads, cooking and dipping. Cholesterol free and heart healthy.",
							"Grocery", 1299.00, 45, "olive-oil.jpg", 10, 1169.10, true, null, null),

					new Product(null,
							"Pure Natural Honey 500g - Raw & Unprocessed",
							"Raw, unprocessed honey collected from natural sources. No added sugar or preservatives. Rich in antioxidants with a naturally sweet taste.",
							"Grocery", 749.00, 60, "honey.jpg", 25, 561.75, true, null, null),

					new Product(null,
							"The Great Gatsby - Classic Novel (Paperback)",
							"F. Scott Fitzgerald's timeless classic of the Jazz Age. A tale of love, wealth and the American dream. A must-read literary masterpiece.",
							"Books", 299.00, 100, "novel-book.jpg", 10, 269.10, true, null, null),

					new Product(null,
							"Atomic Habits - Build Good Habits & Break Bad Ones",
							"James Clear's bestselling guide to building good habits and breaking bad ones. Practical strategies backed by science for lasting self-improvement.",
							"Books", 599.00, 75, "self-help-book.jpg", 30, 419.30, true, null, null),

					new Product(null,
							"The Complete Cookbook for Beginners - 100+ Recipes",
							"Step-by-step cookbook with over 100 easy recipes for beginners. From breakfast to dinner, master essential cooking techniques with full-color photos.",
							"Books", 899.00, 50, "cookbook.jpg", 15, 764.15, true, null, null),

					new Product(null,
							"Gentle Foaming Face Wash - For All Skin Types",
							"Sulfate-free foaming face wash with aloe vera and vitamin E. Removes dirt, oil and impurities without drying the skin. Suitable for daily use.",
							"Beauty", 449.00, 85, "facewash.jpg", 20, 359.20, true, null, null),

					new Product(null,
							"Luxury Perfume for Men - Long Lasting Fragrance 100ml",
							"Eau de parfum with a bold, long-lasting fragrance blend of citrus, spice and woody notes. Perfect for office, parties and special occasions.",
							"Beauty", 2999.00, 30, "perfume.jpg", 35, 1949.35, true, null, null),

					new Product(null,
							"Smartphone 5G - 8GB RAM, 128GB Storage, 50MP Camera",
							"Sleek 5G smartphone with 6.7 inch AMOLED display, 8GB RAM, 128GB storage and 50MP AI triple camera. 5000mAh battery with fast charging support.",
							"Mobiles", 24999.00, 20, "smartphone.jpg", 12, 21999.12, true, null, null),

					new Product(null,
							"True Wireless Earbuds - Bluetooth 5.3 with ANC",
							"True wireless earbuds with active noise cancellation, Bluetooth 5.3 and touch controls. Up to 30 hours total playtime with the charging case. IPX5 water resistant.",
							"Mobiles", 3499.00, 65, "earbuds.jpg", 45, 1924.45, true, null, null));

			for (Product product : extraProducts) {
				if (!productRepository.existsByProductTitle(product.getProductTitle())) {
					productRepository.save(product);
					System.out.println("DataSeeder : product added - " + product.getProductTitle());
				}
			}
		}

		// ---------- TOP-UP: ensure every category has at least 5 products ----------
		List<Product> categoryTopUpProducts = List.of(
				// shirt (4 more -> 5 total)
				new Product(null,
						"Men's Formal Shirt - Wrinkle Free",
						"Crisp formal shirt with wrinkle-free finish and spread collar. Tailored fit with reinforced cuffs, ideal for office wear and business meetings. Easy machine wash.",
						"shirt", 1699.00, 30, "FormalShirt.jpg", 20, 1359.20, true, null, null),

				new Product(null,
						"Men's Checkered Casual Shirt - Full Sleeve",
						"Trendy checkered full-sleeve casual shirt in soft brushed cotton. Comfortable regular fit with button-down collar, perfect for weekends and outings.",
						"shirt", 1399.00, 25, "Casual-Shirt-Slim-Fit.jpg", 15, 1189.15, true, null, null),

				new Product(null,
						"Men's Denim Shirt - Classic Blue",
						"Classic blue denim shirt with a slightly faded wash and sturdy stitching. Versatile styling for layering or standalone wear. Gets softer with every wash.",
						"shirt", 1899.00, 20, "CasualShirt.jpg", 25, 1424.25, true, null, null),

				new Product(null,
						"Men's Linen Shirt - Breathable Summer Wear",
						"Lightweight pure linen shirt designed for hot and humid days. Highly breathable with a relaxed fit and natural texture. Available in pastel shades.",
						"shirt", 1599.00, 22, "T-Shirt.jpg", 10, 1439.10, true, null, null),

				// Accessories (2 more -> 5 total)
				new Product(null,
						"Men's Formal Leather Belt - Reversible",
						"Reversible formal belt in genuine leather with rotating buckle offering two colors in one. Scratch resistant, durable and perfect with formal attire.",
						"Accessories", 1099.00, 45, "Accessories.jpg", 18, 901.18, true, null, null),

				new Product(null,
						"Stainless Steel Keychain Gift Set - Pack of 3",
						"Premium stainless steel keychains with polished finish and sturdy rings. Rust-proof and durable. Great as a return gift or everyday carry accessory.",
						"Accessories", 499.00, 80, "backpack.jpg", 10, 449.10, true, null, null),

				// Footwear (4 more -> 5 total)
				new Product(null,
						"Men's Casual Sneakers - Everyday Comfort",
						"Versatile casual sneakers with cushioned insole and breathable canvas upper. Lightweight with flexible grip sole for all-day walking comfort.",
						"Footwear", 2299.00, 40, "running-shoes.jpg", 20, 1839.20, true, null, null),

				new Product(null,
						"Men's Formal Leather Derby Shoes",
						"Handcrafted genuine leather derby shoes with polished finish and cushioned footbed. Durable stitched sole, ideal for office and formal occasions.",
						"Footwear", 3999.00, 18, "leather-wallet.jpg", 30, 2799.30, true, null, null),

				new Product(null,
						"Unisex Comfort Sliders - Anti-Skid Sole",
						"Soft cushioned sliders with contoured footbed and anti-skid sole. Quick-dry material perfect for home, poolside and casual errands.",
						"Footwear", 799.00, 60, "running-shoes.jpg", 12, 703.12, true, null, null),

				new Product(null,
						"Men's Trekking Shoes - High Ankle Grip",
						"Rugged high-ankle trekking shoes with deep-tread outsole for superior grip on trails. Water-resistant upper with extra ankle support for long hikes.",
						"Footwear", 4499.00, 15, "running-shoes.jpg", 35, 2924.35, true, null, null),

				// Watches (4 more -> 5 total)
				new Product(null,
						"Men's Analog Wrist Watch - Leather Strap",
						"Elegant analog wrist watch with genuine leather strap and scratch-resistant glass. Minimalist dial design suitable for office and evening wear.",
						"Watches", 2899.00, 35, "smartwatch.jpg", 25, 2174.25, true, null, null),

				new Product(null,
						"Digital Sports Watch - Water Resistant 50m",
						"Rugged digital sports watch with stopwatch, alarm and backlight. 50m water resistance with durable resin strap, built for workouts and outdoor use.",
						"Watches", 1599.00, 50, "smartwatch.jpg", 15, 1359.15, true, null, null),

				new Product(null,
						"Couple Watch Set - Matching Analog Pair",
						"Matching his-and-hers analog watch set with stainless steel mesh straps and slim dials. Elegant gift-ready packaging for anniversaries and weddings.",
						"Watches", 3499.00, 25, "smartwatch.jpg", 20, 2799.20, true, null, null),

				new Product(null,
						"Kids' Colorful Silicone Watch - Easy Read",
						"Fun kids' analog watch with soft silicone strap and easy-to-read colorful dial. Shock resistant, water splashes safe and adjustable for small wrists.",
						"Watches", 899.00, 40, "smartwatch.jpg", 10, 809.10, true, null, null));

		for (Product product : categoryTopUpProducts) {
			if (!productRepository.existsByProductTitle(product.getProductTitle())) {
				productRepository.save(product);
				System.out.println("DataSeeder : product added - " + product.getProductTitle());
			}
		}
	}
}
