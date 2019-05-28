package com.prs;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.prs.business.User;
import com.prs.db.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserTests {
	
	@Autowired
	private UserRepository userRepo;
	
	@Test
	public void testUserGetAll() {
		Iterable<User> users = userRepo.findAll();
		assertNotNull(users);
	}
	
	@Test
	public void testUserAddAndDelete() {
		User u = new User("userNameTest", "pasword", "firstName", "lastName", "phoneNumber", "email", true, true);
		assertNotNull(userRepo.save(u));
		//assert that lastName is "lastName"
		assertEquals("lastName", u.getLastName());
		//delete the user
		userRepo.delete(u);
		//confirm user deletion by getting the user by id
		assertFalse(userRepo.findById(u.getId()).isPresent());
	}
}
