package main.service;

import java.util.List;
import javax.transaction.Transactional;
import main.model.Product;
import main.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private final static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	private ProductRepository productRepository;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public List<Product> getAll() {
		logger.debug("Getting list of products.");
		List<Product> listProducts = productRepository.findAll();
		if (listProducts.size() > 0) {
			logger.debug("The resulting list");
		} else {
			logger.debug("The list is empty");
		}
		return listProducts;
	}

	@Override
	public Product get(Long idProduct) {
		logger.debug("Searching product with id: {}", idProduct);
		return productRepository.getOne(idProduct);
	}

	@Override
	public Product getByProductName(String productName) {
		logger.debug("Searching product with name: {}", productName);
		Product searchingProduct = productRepository.getByProductName(productName);
		if (searchingProduct == null) {
			logger.debug("Product {} not found", productName);
		}
		return searchingProduct;
	}

	@Override
	public void save(Product product) {
		logger.debug("Save product: {}", product.toString());
		productRepository.saveAndFlush(product);
	}

	@Override
	public void delete(Product product) {
		logger.debug("Delete product: {}", product.toString());
		productRepository.delete(product);
	}
}
