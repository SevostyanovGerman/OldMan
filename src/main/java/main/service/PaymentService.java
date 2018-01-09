package main.service;

import java.util.List;
import main.model.Payment;

public interface PaymentService {

	Payment get(Long id);

	Payment getByName(String name);

	void save(Payment payment);

	List<Payment> getAll();
}
