package com.vineet.accounts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vineet.accounts.dto.CustomerDto;
import com.vineet.accounts.dto.ResponseDto;
import com.vineet.accounts.service.AccountServiceImpl;

@RestController
@RequestMapping("/api")
public class AccountsController {
	
	@Autowired
	private AccountServiceImpl accountService;
	
	@PostMapping("/create")
	public ResponseEntity<ResponseDto> createAccount(@RequestBody CustomerDto customerDto){
		accountService.createAccount(customerDto);
		return new ResponseEntity<>(new ResponseDto("201", "Account created Successfully"), HttpStatus.CREATED);
	}
	
	@GetMapping("/fetch")
	public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam("mobile") String mobileNumber){
		CustomerDto customerDto = accountService.fetchAccount(mobileNumber);
		return new ResponseEntity<>(customerDto, HttpStatus.OK);
	}
	
	@PostMapping("/update")
	public ResponseEntity<ResponseDto> updateAccount(@RequestBody CustomerDto customerDto){
		boolean isUpdated = accountService.updateAccount(customerDto);
		if(isUpdated) {
			return new ResponseEntity<>(new ResponseDto("", ""), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new ResponseDto("", ""), HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam String mobileNumber){
		boolean isDeleted = accountService.deleteAccount(mobileNumber);
		if(isDeleted) {
			return new ResponseEntity<>(new ResponseDto("", ""), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new ResponseDto("", ""), HttpStatus.EXPECTATION_FAILED);
		}
	}
	
}
