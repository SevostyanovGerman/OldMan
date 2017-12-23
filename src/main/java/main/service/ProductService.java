package main.service;

import main.model.Product;

import java.util.List;

public interface ProductService {

	List<Product> getAll();
	Product get(Long idProduct);
	Product getByName(String name);
	void save(Product product);
	void delete(Product product);
}
