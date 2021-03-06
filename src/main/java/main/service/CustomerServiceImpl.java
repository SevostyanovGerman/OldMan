package main.service;

import java.util.List;
import main.model.Customer;
import main.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

	private final static Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	private CustomerRepository customerRepository;

	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

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
	public List<Customer> getByName(String name) {
		logger.debug("Getting list customers with Firstname: {}", name);
		return customerRepository.getAllByFirstNameContains(name);
	}

	@Override
	public List<Customer> getAll() {
		logger.debug("Getting customer list");
		return customerRepository.getAllByDeleted(false);
	}

	@Override
	public List<Customer> searchCustomer(String searchWord) {
		logger.debug("Looking word {}", searchWord);
		return customerRepository.findBySearchWord(searchWord);
	}

	@Override
	public Boolean checkEmail(String email) {
		Customer customer = customerRepository.getCustomerByEmail(email);
		if (customer != null) {
			return false;
		} else {
			return true;
		}
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
