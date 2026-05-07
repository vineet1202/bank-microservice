package com.vineet.accounts.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vineet.accounts.dto.AccountsDto;
import com.vineet.accounts.dto.CustomerDto;
import com.vineet.accounts.entity.Accounts;
import com.vineet.accounts.entity.Customer;
import com.vineet.accounts.exception.CustomerAlreadyExistsException;
import com.vineet.accounts.exception.ResourceNotFoundException;
import com.vineet.accounts.mapper.AccountsMapper;
import com.vineet.accounts.mapper.CustomerMapper;
import com.vineet.accounts.repository.AccountsRepository;
import com.vineet.accounts.repository.CustomerRepository;

@Service
public class AccountServiceImpl implements AccountService{
	
	@Autowired
	private AccountsRepository accountsRepository;
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public void createAccount(CustomerDto customerDto) {
		Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
		Optional<Customer> optional = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
		Optional<Customer> optional2 = customerRepository.findByEmail(customerDto.getEmail());
		if(optional.isPresent()) {
			throw new CustomerAlreadyExistsException("Mobile Number already exists " + customerDto.getMobileNumber());
		}
		if(optional2.isPresent()) {
			throw new CustomerAlreadyExistsException("Email already exists " + customerDto.getEmail());
		}
		Customer savedCustomer = customerRepository.save(customer);
		
		accountsRepository.save(createNewAccount(savedCustomer));
	}
	
	private Accounts createNewAccount(Customer customer) {
		Accounts newAccount = new Accounts();
		newAccount.setCustomerId(customer.getCustomerId());
		long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
		
		newAccount.setAccountNumber(randomAccNumber);
		newAccount.setAccountType("Savings");
		newAccount.setBranchAddress("123 Main Street, New York");
		
		return newAccount;
	}

	@Override
	public CustomerDto fetchAccount(String mobileNumber) {
		Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
				() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
		
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
				() -> new ResourceNotFoundException("Accounts", "customerId", customer.getCustomerId().toString()));
		
		CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
		customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
		return customerDto;
		
	}

	@Override
	public boolean updateAccount(CustomerDto customerDto) {
		boolean isUpdated = false;
		AccountsDto accountsDto = customerDto.getAccountsDto();
		//find account by using Id then update it
		//find customer by Id and then update it
		
		if(accountsDto != null) {
			Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
					() -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString()));
			
			AccountsMapper.mapToAccounts(accountsDto, accounts);
			accounts = accountsRepository.save(accounts);
			
			Long customerId = accounts.getCustomerId();
			
			Customer customer = customerRepository.findById(customerId).orElseThrow(
					() -> new ResourceNotFoundException("Customer", "CustomerId", customerId.toString()));
					
			CustomerMapper.mapToCustomer(customerDto, customer);
			customerRepository.save(customer);
			isUpdated = true;
		}
		
		return isUpdated;
	}

	@Override
	public boolean deleteAccount(String mobileNumber) {
		
		//get the customer using mobileNumber
		
		//delete the account using customerId
		// then delete the customer by Id
		Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
				() -> new ResourceNotFoundException("Customer", "MoibleNumber", mobileNumber.toString()));
		
		accountsRepository.deleteByCustomerId(customer.getCustomerId());
		return true;
		
	}
	
	
}
