package main.service;

import java.util.List;
import main.model.Product;

public interface ProductService {

	List<Product> getAll();

	Product get(Long idProduct);

	Product getByProductName(String productName);

	void save(Product product);

	void delete(Product product);
}
