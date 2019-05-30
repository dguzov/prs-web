package com.prs;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.prs.business.Vendor;
import com.prs.db.VendorRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class VendorTests {
	
	@Autowired
	private VendorRepository vendorRepo;
	
	@Test
	public void testVendorGetAll() {
		Iterable<Vendor> vendors = vendorRepo.findAll();
		assertNotNull(vendors);
	}
	
	@Test
	public void testVendorAddAndDelete() {
		Vendor v = new Vendor("NE-001", "NewEgg", "streeeet", "citycity", "CA", 
				"45454", "9998887777", "dsdsdsd@gmail.com", true);
		assertNotNull(vendorRepo.save(v));
		//assert that lastName is "lastName"
		assertEquals("NE-001", v.getCode());
		//delete the user
		vendorRepo.delete(v);
		//confirm user deletion by getting the user by id
		assertFalse(vendorRepo.findById(v.getId()).isPresent());
	}

}
