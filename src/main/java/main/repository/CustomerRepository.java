package main.repository;

import java.util.List;
import main.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query("SELECT c FROM Customer c WHERE   c.deleted = false "
		+ "AND ( (c.firstName) LIKE UPPER(CONCAT('%', :searchWord, '%')) OR "
		+ "UPPER(c.secName) LIKE UPPER(CONCAT('%', :searchWord, '%')) OR "
		+ "UPPER(c.email) LIKE UPPER(CONCAT('%', :searchWord, '%')) OR "
		+ "UPPER(c.phone) LIKE UPPER(CONCAT('%', :searchWord, '%')) OR "
		+ "UPPER(c.creationDate) LIKE UPPER(CONCAT('%', :searchWord, '%')))")
	List<Customer> findBySearchWord(@Param("searchWord") String searchWord);

	Customer getCustomerByEmail(String email);

	Customer getCustomerByPhone(String phone);

	List<Customer> getAllByFirstNameContains(String name);

	Customer getByEmail(String email);

	List<Customer> getAllByDeleted(boolean deleted);
}
