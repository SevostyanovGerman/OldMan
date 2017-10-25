package main.service;

import main.model.Customer;
import java.util.List;

public interface CustomerService {

	Customer get(Long id);
	Customer getByEmail(String email);
	Customer getByPhone(String phone);
	Customer save(Customer customer);
	Customer update(Customer customer);
	List <Customer> getByName(String name);
	List <Customer> getAll();
	List <Customer> searchCustomer(String searchWord);
}
