package main.service;

import java.util.List;
import main.model.Customer;

public interface CustomerService {

	Customer get(Long id);

	Customer getByEmail(String email);

	Customer getByPhone(String phone);

	Customer save(Customer customer);

	Customer update(Customer customer);

	List<Customer> getByName(String name);

	List<Customer> getAll();

	List<Customer> searchCustomer(String searchWord);

	Boolean checkEmail(String email);
}
