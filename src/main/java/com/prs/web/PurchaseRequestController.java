package com.prs.web;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.prs.business.JsonResponse;
import com.prs.business.PurchaseRequest;
import com.prs.business.User;
import com.prs.db.PurchaseRequestRepository;

@RestController
@RequestMapping("/purchase-requests")
public class PurchaseRequestController<reasonForRejection> {
	
	@Autowired
	private PurchaseRequestRepository PurchaseRequestRepo;
	
	@GetMapping("/")
	public JsonResponse getAll(){
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(PurchaseRequestRepo.findAll());
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id){
		JsonResponse jr = null;
		try {
			Optional<PurchaseRequest> pr = PurchaseRequestRepo.findById(id);
			if (pr.isPresent())
			jr = JsonResponse.getInstance(pr);
			else 
				jr = JsonResponse.getInstance("No PurchaseRequest found for id: "+id);
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@GetMapping("")
	public JsonResponse getByUser(@RequestParam User user) {
		JsonResponse jr = null;
		try {
			Iterable<PurchaseRequest> pr = PurchaseRequestRepo.findByUser(user);
			if (pr!=null)
				jr = JsonResponse.getInstance(pr);
			else
				jr = JsonResponse.getInstance("No purchaseRequest found for User: "+user);
				
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	
	@PostMapping("/submit-new")
	public JsonResponse add(@RequestBody PurchaseRequest pr) {
		JsonResponse jr = null;
		pr.setStatus ("New");
		pr.setSubmittedDate(LocalDateTime.now());
		try {
			jr = JsonResponse.getInstance(PurchaseRequestRepo.save(pr));
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@PutMapping("/submit-review")
	public JsonResponse submitForReview(@RequestBody PurchaseRequest pr) {
		JsonResponse jr = null;
		pr.setSubmittedDate(LocalDateTime.now());
		if (pr.getTotal()<=50.00) {
			pr.setStatus("Approved");
		}
			else {pr.setStatus("Review");	
			}
		try {
			jr = JsonResponse.getInstance(PurchaseRequestRepo.save(pr));
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	
	@GetMapping("/list-review")
	public JsonResponse getReview() {
		JsonResponse jr = null;
		try {
			Iterable<PurchaseRequest> pr = PurchaseRequestRepo.findAllByStatus("Review");
			if (pr!=null)
				jr = JsonResponse.getInstance(pr);
			else
				jr = JsonResponse.getInstance("No purchaseRequest found");
				
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
			
		@PutMapping("/approve")
		public JsonResponse setApproved(@RequestBody PurchaseRequest pr) {
			JsonResponse jr = null;
			try {
			if (pr.getStatus().equals("Review"))
				pr.setStatus("Approved");
			else
					jr = JsonResponse.getInstance("No purchaseRequest in status 'Review' found");	
			} catch (Exception e) {
				jr = JsonResponse.getInstance(e);
			}
			return jr;
		}
		
		
//check out!	
		@PutMapping("/reject")
		public JsonResponse setRejected(@RequestBody PurchaseRequest pr, reasonForRejection rfr) {
			JsonResponse jr = null;
			try {
			if (pr.getStatus().equals("Review") && rfr !=null)
				pr.setStatus("Rejected");
			else
					jr = JsonResponse.getInstance("No purchaseRequest in status 'Review' with 'reasonForRejection' found");	
			} catch (Exception e) {
				jr = JsonResponse.getInstance(e);
			}
			return jr;
		}
		
	@PutMapping("/")
	public JsonResponse update(@RequestBody PurchaseRequest pr) {
		JsonResponse jr = null;
		// NOTE: May need to enhance exception handling if more than one exception type needs to be caught
		try {
			if (PurchaseRequestRepo.existsById(pr.getId())){
			jr = JsonResponse.getInstance(PurchaseRequestRepo.save(pr));
		}
			else
				jr = JsonResponse.getInstance("PurchaseRequest id: "+pr.getId()+" does not exist and you are aattomtping to save it");
		}
		catch (Exception e) {
			e.printStackTrace();
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@DeleteMapping("/")
	public JsonResponse delete(@RequestBody PurchaseRequest pr) {
		JsonResponse jr = null;
		// NOTE: May need to enhance exception handling if more than one exception type needs to be caught
		try {
			if (PurchaseRequestRepo.existsById(pr.getId())){
				PurchaseRequestRepo.delete(pr);	
			jr = JsonResponse.getInstance("PurchaseRequest deleted");
		}
			else
				jr = JsonResponse.getInstance("PurchaseRequest id: "+pr.getId()+" does not exist and you are attempting to delete it");
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
			
	
}
