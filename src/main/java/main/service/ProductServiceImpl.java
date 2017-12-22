package main.service;

import main.model.Product;
import main.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public List<Product> getAll() {
		return productRepository.findAll();
	}

	@Override
	public Product get(Long idProduct) {
		return productRepository.getOne(idProduct);
	}

	@Override
	public void save(Product product) {
		productRepository.saveAndFlush(product);
	}

	@Override
	public void delete(Product product) {
		productRepository.delete(product);
	}
}
