package main.service;

import main.model.Product;

import java.util.List;

public interface ProductService {

	List<Product> getAll();

	Product get(Long idProduct);

	Product getByProductName(String productName);

	void save(Product product);

	void delete(Product product);
}
