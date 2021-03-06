package main.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;
import main.model.Order;
import main.model.Status;
import main.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	Order findByIdAndDeleted(Long id, Boolean deleted);

	Set<Order> findAllByStatusAndDeleted(Status status, Boolean deleted);

	List<Order> findAllByDeleted(Boolean deleted);

	List<Order> findAllByCustomerFirstNameContainsAndDeleted(String name, Boolean deleted);

	List<Order> findAllByDeletedAndNumberContains(Boolean deleted, String number);

	List<Order> findAllByDeletedAndManagerFirstNameContains(Boolean deleted, String name);

	List<Order> findAllByDeletedAndStatusId(Boolean deleted, Long id);

	List<Order> findAllByDeletedAndStatusIdAndNumberContains(Boolean deleted, Long statusId, String number);

	@Query("SELECT o FROM Order o WHERE ( " + "LOWER(o.number) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.customer.firstName) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.customer.secName) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.manager.firstName) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.paymentString) LIKE LOWER(CONCAT(:searchTerm, '%')) OR "
		+ "LOWER(o.manager.secName) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.status.name) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.paymentType.name) LIKE LOWER(CONCAT('%',:searchTerm, '%')) "
		+ ") AND o.created between STR_TO_DATE(:startDate, '%Y-%m-%d %H:%i:%s')  and STR_TO_DATE"
		+ "(:endDate, '%Y-%m-%d %H:%i:%s') ")
	List<Order> findBySearchTerm(@Param("searchTerm") String searchTerm, @Param("startDate") Date startDate,
		@Param("endDate") Date endDate);

	@Query("SELECT o FROM Order o WHERE o.created BETWEEN :startDate AND :endDate")
	List<Order> findOrdersByRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Query(
		"SELECT o FROM Order o WHERE " + "( o.manager = :user OR " + "o.master = :user OR " + "o.designer = :user) AND "
			+ "o.status = :status AND " + "o.deleted = :deleted ")
	Set<Order> findByUser(@Param("user") User user, @Param("status") Status status, @Param("deleted") boolean deleted);

	@Query("SELECT  date_format(o.created, :dwm) as created ,  AVG(o.price) as price  FROM " + "Order o "
		+ "WHERE o.deleted=0 AND o.payment=1 AND o.created between STR_TO_DATE(:startDate, '%Y-%m-%d %H:%i:%s')  and  "
		+ "STR_TO_DATE(:endDate, '%Y-%m-%d %H:%i:%s')  "
		+ "GROUP BY date_format(o.created, :dwm) ORDER BY date_format(o.created, :dwm)")
	List<Object> priceAvgByMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
		@Param("dwm") String dwm);

	@Query(value =
		"SELECT  date_format(o.created, :dwm) as created , sum(o.price * o.payment) as price ,  sum(o.price), "
			+ "sum(o.price) as annotation FROM orders o "
			+ "WHERE o.deleted=0 and o.created between STR_TO_DATE(:startDate, " + "'%Y-%m-%d " + "%H:%i:%s')  and  "
			+ "STR_TO_DATE(:endDate, '%Y-%m-%d %H:%i:%s')  " + "GROUP BY "
			+ "date_format(o.created, :dwm) ORDER BY date_format(o.created, :dwm)", nativeQuery = true)
	List<Object> sumPriceOrders(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
		@Param("dwm") String dwm);

	@Query("select o.delivery.city, count(o.delivery.city) from Order o "
		+ "  where o.deleted =0 and o.payment=1 AND o.created between STR_TO_DATE(:startDate, '%Y-%m-%d %H:%i:%s')  and  "
		+ "STR_TO_DATE(:endDate, '%Y-%m-%d %H:%i:%s')" + " group by o.delivery.city" + " order by o" + ".delivery.city")
	List<Object> statisticGeo(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Query("SELECT  date_format(o.creationDate, '%Y-%m') as created ,  count(o.creationDate) FROM Customer o "
		+ "WHERE o.deleted=0 AND o.creationDate between STR_TO_DATE(:startDate, '%Y-%m-%d %H:%i:%s')  and  STR_TO_DATE(:endDate, '%Y-%m-%d %H:%i:%s')  "
		+ "GROUP BY " + "date_format(o.creationDate, '%Y-%m') ORDER BY date_format(o.creationDate, '%Y-%m')")
	List<Object> statisticNewCustomers(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	///Поисковый запрос для Dashbard Boss
	@Query("SELECT o FROM Order o WHERE "
		+ "( :searchTerm is null or  (LOWER(o.number) LIKE LOWER(CONCAT('%',:searchTerm, '%')) " + "OR "
		+ "LOWER(o.customerFirstNameString) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.customerSecNameString) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.masterFirstNameString) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.masterSecNameString) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.designerFirstNameString) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.paymentString) LIKE LOWER(CONCAT(:searchTerm, '%')) OR "
		+ "LOWER(o.designerSecNameString) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.manager.firstName) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.manager.secName) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.status.name) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.paymentTypeString) LIKE LOWER(CONCAT('%',:searchTerm, '%')) ))" + "AND o.deleted = false AND"
		+ "( :min is  null or  o.price >= :min)  AND ( :max is  null or  o.price <= :max) " + " AND o.created between "
		+ "STR_TO_DATE(:startDate, '%Y-%m-%d %H:%i:%s')  " + "and " + "STR_TO_DATE(:endDate, '%Y-%m-%d %H:%i:%s') ")
	Page<Order> filterForDashboardBoss(@Param("searchTerm") String searchTerm, @Param("min") Double min,
		@Param("max") Double max, @Param("startDate") Date startDate, @Param("endDate") Date endDate,
		org.springframework.data.domain.Pageable pageable);

	//// Поисковый запрос для Dashboard manager
	@Query("SELECT o FROM Order o WHERE "
		+ "( :searchTerm is null or  (LOWER(o.number) LIKE LOWER(CONCAT('%',:searchTerm, " + "'%')) OR "
		+ "LOWER(o.customerFirstNameString) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.customerSecNameString) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.manager.firstName) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.paymentString) LIKE LOWER(CONCAT(:searchTerm, '%')) OR "
		+ "LOWER(o.manager.secName) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.status.name) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR "
		+ "LOWER(o.paymentTypeString) LIKE LOWER(CONCAT('%',:searchTerm, '%')) ))"
		+ "AND o.deleted = false AND o.manager = :user AND"
		+ "( :min is  null or  o.price >= :min)  AND ( :max is  null or  o.price <= :max) " + " AND o.created between "
		+ "STR_TO_DATE(:startDate, '%Y-%m-%d %H:%i:%s')  " + "and " + "STR_TO_DATE(:endDate, '%Y-%m-%d %H:%i:%s') ")
	Page<Order> filterForDashboard(@Param("user") User user, @Param("searchTerm") String searchTerm,
		@Param("min") Double min, @Param("max") Double max, @Param("startDate") Date startDate,
		@Param("endDate") Date endDate, org.springframework.data.domain.Pageable pageable);
}
