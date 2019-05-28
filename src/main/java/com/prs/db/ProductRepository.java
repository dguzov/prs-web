package com.prs.db;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {
	//Optional<User> findByUserName (String userName);
}
