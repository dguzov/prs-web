package com.prs;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.prs.business.Product;
import com.prs.business.PurchaseRequest;
import com.prs.business.PurchaseRequestLineItem;
import com.prs.db.ProductRepository;
import com.prs.db.PurchaseRequestLineItemRepository;
import com.prs.db.PurchaseRequestRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PurchaseRequestLineItemTests {
	
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private PurchaseRequestRepository purchaseRequestRepo;
	@Autowired
	private PurchaseRequestLineItemRepository purchaseRequestLineItemRepo;
	
	@Test
	public void testPurchaseRequestLineItemGetAll() {
		Iterable<PurchaseRequestLineItem> purchaseRequestLineItem = purchaseRequestLineItemRepo.findAll();
		assertNotNull(purchaseRequestLineItem);
	}
	
	@Test
	public void testPurchaseRequestLineItemsAddAndDelete() {
		
		Iterable<Product> products = productRepo.findAll();
		assertNotNull(products);
		Product p = products.iterator().next();
		
		Iterable<PurchaseRequest> purchaseRequests = purchaseRequestRepo.findAll();
		assertNotNull(purchaseRequests);
		PurchaseRequest pr = purchaseRequests.iterator().next();
		
		PurchaseRequestLineItem prli = new PurchaseRequestLineItem(pr, p, 7);
		assertNotNull(purchaseRequestLineItemRepo.save(prli));
		//assert that 7 is quantity
		assertEquals(7, prli.getQuantity());
		//delete
		purchaseRequestLineItemRepo.delete(prli);
		//confirm the deletion by getting the product by id
		assertFalse(purchaseRequestLineItemRepo.findById(prli.getId()).isPresent());
	}
	
}
