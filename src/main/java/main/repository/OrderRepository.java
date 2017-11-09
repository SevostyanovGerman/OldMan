package main.repository;

import main.model.Order;
import main.model.Status;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository <Order, Long> {

	Order findByIdAndDeleted(Long id, Boolean deleted);
	Set<Order> findAllByStatusAndDeleted(Status status, Boolean deleted);
	List<Order> findAllByDeleted(Boolean deleted);
	List<Order> findAllByCustomerFirstNameContainsAndDeleted(String name, Boolean deleted);
	List<Order> findAllByDeletedAndNumberContains(Boolean deleted, String number);
	List<Order> findAllByDeletedAndManagerFirstNameContains(Boolean deleted, String name);
	List<Order> findAllByDeletedAndStatusId(Boolean deleted, Long id);
	List<Order> findAllByDeletedAndStatusIdAndNumberContains(Boolean deleted, Long statusId, String number);
	@Query("SELECT o FROM Order o WHERE " + "LOWER(o.payment) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
		"LOWER(o.dateRecieved) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
		"LOWER(o.dateTransferred) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
		"LOWER(o.deliveryType) LIKE LOWER(CONCAT('%',:searchTerm, '%'))")
	List<Order> findBySearchTerm(@Param("searchTerm") String searchTerm);

	@Query("SELECT o FROM Order o WHERE o.created BETWEEN :startDate AND :endDate")
	List<Order> findOrdersByRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Query("SELECT o FROM Order o WHERE " + "( o.manager = :user OR " +
		"o.master = :user OR " +
		"o.designer = :user) AND " +
		"o.status = :status AND " +
		"o.deleted = :deleted ")
	Set<Order> findByUser(@Param("user") User user, @Param("status") Status status, @Param("deleted") boolean deleted);
}
