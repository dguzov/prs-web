package com.prs.db;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.PurchaseRequest;
import com.prs.business.User;

public interface PurchaseRequestRepository extends CrudRepository<PurchaseRequest, Integer> {
	Iterable<PurchaseRequest> findByUser(User user);
	Iterable<PurchaseRequest> findAllByStatus (String status);
	Optional<PurchaseRequest> findByStatus (String status);
}
