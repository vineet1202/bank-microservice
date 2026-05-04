package com.vineet.accounts;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {
	
	@GetMapping("sayHello")
	public String HelloWorld() {
		return "Hi world";
	}
	
}
