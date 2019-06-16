package com.prs.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.prs.business.JsonResponse;
import com.prs.business.PurchaseRequest;
import com.prs.business.PurchaseRequestLineItem;
import com.prs.db.PurchaseRequestLineItemRepository;
import com.prs.db.PurchaseRequestRepository;

@RestController
@RequestMapping("/purchase-request-line-items")
public class PurchaseRequestLineItemController {
	
	@Autowired
	private PurchaseRequestLineItemRepository purchaseRequestLineItemRepo;
	
	@Autowired
	private PurchaseRequestRepository PurchaseRequestRepo;
	
	@GetMapping("/")
	public JsonResponse getAll(){
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(purchaseRequestLineItemRepo.findAll());
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@GetMapping("/{id}")
	public JsonResponse getById(@PathVariable int id){
		JsonResponse jr = null;
		try {
			Optional<PurchaseRequestLineItem> prli = purchaseRequestLineItemRepo.findById(id);
			if (prli.isPresent())
			jr = JsonResponse.getInstance(prli);
			else 
				jr = JsonResponse.getInstance("No PurchaseRequestLineItem found for id: "+id);
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@GetMapping("")
	public JsonResponse getByPurchaseRequestId(@RequestParam int purchaseRequestId) {
		JsonResponse jr = null;
		try {
			Optional<PurchaseRequestLineItem> prli = purchaseRequestLineItemRepo.findByPurchaseRequestId(purchaseRequestId);
			if (prli.isPresent())
				jr = JsonResponse.getInstance(prli);
			else
				jr = JsonResponse.getInstance("No purchaseRequestLineItem found for purchase request: "+purchaseRequestId);
				
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
		
	@PostMapping("/")
	public JsonResponse add(@RequestBody PurchaseRequestLineItem prli) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(purchaseRequestLineItemRepo.save(prli));
			updatePurchaseRequestTotal(prli);
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@PutMapping("/")
	public JsonResponse update(@RequestBody PurchaseRequestLineItem prli) {
		JsonResponse jr = null;
		// NOTE: May need to enhance exception handling if more than one exception type needs to be caught
		try {
			if (purchaseRequestLineItemRepo.existsById(prli.getId())){
			jr = JsonResponse.getInstance(purchaseRequestLineItemRepo.save(prli));
			updatePurchaseRequestTotal(prli);
		}
			else
				jr = JsonResponse.getInstance("PurchaseRequestLineItem id: "+prli.getId()+" does not exist and you are attempting to save it");
		}
		catch (Exception e) {
			e.printStackTrace();
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@DeleteMapping("/")
	public JsonResponse delete(@RequestBody PurchaseRequestLineItem prli) {
		JsonResponse jr = null;
		// NOTE: May need to enhance exception handling if more than one exception type needs to be caught
		try {
			if (purchaseRequestLineItemRepo.existsById(prli.getId())){
				purchaseRequestLineItemRepo.delete(prli);
				updatePurchaseRequestTotal(prli);
			jr = JsonResponse.getInstance("PurchaseRequestLineItem deleted");
		}
			else
				jr = JsonResponse.getInstance("PurchaseRequestLineItem id: "+prli.getId()+" does not exist and you are attempting to delete it");
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	public void updatePurchaseRequestTotal(PurchaseRequestLineItem prli) {
		double tempTotal = 0.0;
		PurchaseRequest pr = prli.getPurchaseRequest();
		Iterable<PurchaseRequestLineItem> prlit = purchaseRequestLineItemRepo.findByPurchaseRequest(pr);		
		for (PurchaseRequestLineItem x: prlit) {
		tempTotal += (x.getQuantity() * (x.getProduct().getPrice()));
		}
		prli.getPurchaseRequest().setTotal(tempTotal);
		PurchaseRequestRepo.save(pr);
	}
		
}
