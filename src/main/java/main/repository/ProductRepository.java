package main.repository;

import java.util.List;
import main.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	Product getByProductNameAndDeleted(String productName, boolean deleted);
	List<Product> findAllByDeleted(boolean deleted);
}
