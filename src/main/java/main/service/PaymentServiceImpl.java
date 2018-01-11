package main.service;

import java.util.List;
import javax.transaction.Transactional;
import main.model.Payment;
import main.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

	private final static Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

	private PaymentRepository paymentRepository;

	@Autowired
	public PaymentServiceImpl(PaymentRepository paymentRepository) {
		this.paymentRepository = paymentRepository;
	}

	@Override
	public Payment get(Long id) {
		return paymentRepository.getOne(id);
	}

	@Override
	public Payment getByName(String name) {
		return paymentRepository.findByName(name);
	}

	@Override
	public void save(Payment payment) {
		paymentRepository.save(payment);
	}

	@Override
	public void delete(Payment payment) {
		logger.debug("Delete product: {}", payment.toString());
		paymentRepository.delete(payment);
	}

	@Override
	public List<Payment> getAll() {
		return paymentRepository.findAll();
	}
}
