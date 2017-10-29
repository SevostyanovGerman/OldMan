package main.service;

import main.model.Payment;

import java.util.List;

public interface PaymentService {

	Payment getByName(String name);
	public void save(Payment payment);
	List<Payment> getAll();
}
