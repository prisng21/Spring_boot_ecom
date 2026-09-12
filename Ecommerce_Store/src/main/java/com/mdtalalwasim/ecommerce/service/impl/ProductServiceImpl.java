package com.mdtalalwasim.ecommerce.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mdtalalwasim.ecommerce.entity.Product;
import com.mdtalalwasim.ecommerce.repository.ProductRepository;
import com.mdtalalwasim.ecommerce.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductRepository productRepository;
	

	
	@Override
	public Product saveProduct(Product product) {
		// TODO Auto-generated method stub
		return productRepository.save(product);
	}

	@Override
	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub
		return productRepository.findAll();
	}

	@Override
	public Boolean deleteProduct(long id) {
		// TODO Auto-generated method stub
		 Optional<Product> product = productRepository.findById(id);
		 if(product.isPresent()) {
			 productRepository.deleteById(product.get().getId());
			 return true;
		 }else {
			 return false;
		 }
		 
	}

	@Override
	public Optional<Product> findById(long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Product getProductById(long id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id).orElse(null);
	}

	@Override
	public Product updateProductById(Product product, MultipartFile file) {
		Product dbProductById = getProductById(product.getId());
		
		String imageName = file.isEmpty() ? dbProductById.getProductImage() : file.getOriginalFilename();
		dbProductById.setProductImage(imageName);
		dbProductById.setProductTitle(product.getProductTitle());
		dbProductById.setProductDescription(product.getProductDescription());
		dbProductById.setProductCategory(product.getProductCategory());
		dbProductById.setProductPrice(product.getProductPrice());
		dbProductById.setProductStock(product.getProductStock());
		dbProductById.setIsActive(product.getIsActive());
		//discount logic
		dbProductById.setDiscount(product.getDiscount());
		Double discount =product.getProductPrice()*(product.getDiscount()/100.0);
		Double discountPrice= product.getProductPrice() - discount;
		dbProductById.setDiscountPrice(discountPrice);
		
		//save the uploaded image to disk FIRST, so a failed copy never leaves the
		//database pointing at an image file that does not exist (broken image)
		if(!file.isEmpty()) {
			try {
				
				File savefile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(savefile.getAbsolutePath()+File.separator+"product_image"+File.separator+file.getOriginalFilename());
				System.out.println("File save Path :"+path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
			} catch (Exception e) {
				e.printStackTrace();
				return null; //image copy failed -> abort the update instead of saving a broken image name
			}
		}
		
		Product updatedProduct = productRepository.save(dbProductById);
		return updatedProduct;
	}

	@Override
	public List<Product> findAllActiveProducts(String category) {
		List<Product> products = null;
		if(ObjectUtils.isEmpty(category)) {
			products = productRepository.findByIsActiveTrue();
		}else {
			products =productRepository.findByProductCategory(category);
		}
		
		return products;
	}

}
