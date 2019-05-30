package com.prs;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.prs.business.PurchaseRequest;
import com.prs.business.User;
import com.prs.db.PurchaseRequestRepository;
import com.prs.db.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PurchaseRequestTests {
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PurchaseRequestRepository purchaseRequestRepo;
	
	@Test
	public void testPurchaseRequestGetAll() {
		Iterable<PurchaseRequest> purchaseRequest = purchaseRequestRepo.findAll();
		assertNotNull(purchaseRequest);
	}
	
	@Test
	public void testPurchaseRequestAddAndDelete() {
		Iterable<User> users = userRepo.findAll();
		assertNotNull(users);
		User u = users.iterator().next();
		PurchaseRequest pr = new PurchaseRequest(u, "descriptionTest", "justificationTest",
			LocalDateTime.now(), "deliveryModeTest", "statusTest", 19.99, LocalDateTime.now(), "reasonForRejectionTest");
		assertNotNull(purchaseRequestRepo.save(pr));
		//assert that partNumberTest is "partNumberTest"
		assertEquals("descriptionTest", pr.getDescription());
		//delete the partNUmberTest
		purchaseRequestRepo.delete(pr);
		//confirm partNumberTest deletion by getting the product by id
		assertFalse(purchaseRequestRepo.findById(pr.getId()).isPresent());
	}
}
