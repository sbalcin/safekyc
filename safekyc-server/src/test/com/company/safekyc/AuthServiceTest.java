package com.company.safekyc;

import com.company.safekyc.service.AuthService;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthServiceTest {

	@Autowired
	AuthService authService;

	@Test
	public void testA_add_user_expected_behaviour() {

		SignUpRequest request = new SignUpRequest();
		request.setEmail("test@test.com");
		request.setPassword("test");

		ResponseEntity<?> responseEntity = authService.signUp(request);
		Assert.assertTrue(((SignUpResponse) responseEntity.getBody()).getResult().equals("success"));
	}

	@Test
	public void testB_login_expected_behaviour() {

		SignInRequest request = new SignInRequest();
		request.setEmail("test@test.com");
		request.setPassword("test");

		ResponseEntity<?> responseEntity = authService.signIn(request);
		Assert.assertTrue(((SignInResponse) responseEntity.getBody()).getResult().equals("success"));
	}




}