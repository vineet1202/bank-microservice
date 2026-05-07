package com.vineet.accounts.service;

import com.vineet.accounts.dto.CustomerDto;

public interface AccountService {
	
	void createAccount(CustomerDto customerDto);
	
	CustomerDto fetchAccount(String mobileNumber);
	
	boolean updateAccount(CustomerDto customerDto);
	
	boolean deleteAccount(String mobileNumber);
}
