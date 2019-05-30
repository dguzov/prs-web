package com.prs;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.prs.business.Product;
import com.prs.business.Vendor;
import com.prs.db.ProductRepository;
import com.prs.db.VendorRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProductTests {
	
	@Autowired
	private VendorRepository vendorRepo;
	@Autowired
	private ProductRepository productRepo;
	
	@Test
	public void testProductGetAll() {
		Iterable<Product> products = productRepo.findAll();
		assertNotNull(products);
	}
	
	@Test
	public void testProductAddAndDelete() {
		Iterable<Vendor> vendors = vendorRepo.findAll();
		assertNotNull(vendors);
		Vendor v = vendors.iterator().next();
		Product p = new Product(v, "partNumberTest", "name", 19.99, "unitTest", "photopathTest");
		assertNotNull(productRepo.save(p));
		//assert that partNumberTest is "partNumberTest"
		assertEquals("partNumberTest", p.getPartNumber());
		//delete the partNUmberTest
		productRepo.delete(p);
		//confirm partNumberTest deletion by getting the product by id
		assertFalse(productRepo.findById(p.getId()).isPresent());
	}
}
