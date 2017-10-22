package main.repository;

import main.model.Order;
import main.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OrderRepository extends JpaRepository <Order, Long> {
	Order findByIdAndDeleted(Long id, int deleted);
	List <Order> findAllByStatusAndDeleted(Status status, int deleted);
	List <Order> findAllByDeleted(int deleted);
	List <Order> findAllByCustomerFirstNameContainsAndDeleted(String name, int deleted);
	List <Order> findAllByDeletedAndNumberContains(int deleted, String number);
	List <Order> findAllByDeletedAndManagerFirstNameContains(int deleted, String name);
	List <Order> findAllByDeletedAndStatusId(int deleted, Long id);
	List <Order> findAllByDeletedAndStatusIdAndNumberContains(int deleted, Long statusId, String number);
	@Query("SELECT o FROM Order o WHERE " + "LOWER(o.payment) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
		"LOWER(o.dateRecieved) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
		"LOWER(o.dateTransferred) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
		"LOWER(o.deliveryType) LIKE LOWER(CONCAT('%',:searchTerm, '%'))")
	List <Order> findBySearchTerm(@Param("searchTerm") String searchTerm);
}
