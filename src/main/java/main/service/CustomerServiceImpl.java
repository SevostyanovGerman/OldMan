package main.service;

import main.model.Customer;
import main.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	private final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public Customer get(Long id) {
		logger.debug("Getting customer with ID: {}", id);
		return customerRepository.getOne(id);
	}

	@Override
	public Customer getByEmail(String email) {
		logger.debug("Searching customer with Email: {}", email);
		return customerRepository.getCustomerByEmail(email);
	}

	@Override
	public Customer getByPhone(String phone) {
		logger.debug("Searching customer with Phone: {}", phone);
		return customerRepository.getCustomerByPhone(phone);
	}

	@Override
	public List <Customer> getByName(String name) {
		logger.debug("Getting list customers with Firstname: {}", name);
		return customerRepository.getAllByFirstNameContains(name);
	}

	@Override
	public List <Customer> getAll() {
		logger.debug("Getting customer list");
		return customerRepository.findAll();
	}

	@Override
	public List <Customer> searchCustomer(String searchWord) {
		logger.debug("Looking word {}", searchWord);
		return customerRepository.findBySearchWord(searchWord);
	}

	@Override
	public Customer save(Customer customer) {
		logger.debug("Save customer {}", customer.toString());
		return customerRepository.saveAndFlush(customer);
	}

	@Override
	public Customer update(Customer customer) {
		logger.debug("Update customer {}", customer.toString());
		return customerRepository.saveAndFlush(customer);
	}
}
