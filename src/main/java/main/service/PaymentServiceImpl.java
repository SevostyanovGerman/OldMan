package main.service;

import main.model.Payment;
import main.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;

	@Override
	public Payment getByName(String name) {
		return paymentRepository.findByName(name);
	}

	@Override
	public void save(Payment payment) {
		paymentRepository.save(payment);
	}

	@Override
	public List<Payment> getAll() {
		return paymentRepository.findAll();
	}
}
