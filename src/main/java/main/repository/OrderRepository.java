package main.repository;

import main.model.Order;
import main.model.Status;
import main.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	Order findByIdAndDeleted(Long id, Boolean deleted);
	Set<Order> findAllByStatusAndDeleted(Status status, Boolean deleted);
	List<Order> findAllByDeleted(Boolean deleted);
	List<Order> findAllByCustomerFirstNameContainsAndDeleted(String name, Boolean deleted);
	List<Order> findAllByDeletedAndNumberContains(Boolean deleted, String number);
	List<Order> findAllByDeletedAndManagerFirstNameContains(Boolean deleted, String name);
	List<Order> findAllByDeletedAndStatusId(Boolean deleted, Long id);
	List<Order> findAllByDeletedAndStatusIdAndNumberContains(Boolean deleted, Long statusId,
															 String number);

	//	@Query("SELECT o FROM Order o WHERE " + "LOWER(o.payment) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
//		   "LOWER(o.dateRecieved) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
//		   "LOWER(o.dateTransferred) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
//		   "LOWER(o.deliveryType) LIKE LOWER(CONCAT('%',:searchTerm, '%'))")
//
	@Query("SELECT o FROM Order o WHERE " +
		   "LOWER(o.number) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
		   "LOWER(o.paymentType.name) LIKE LOWER(CONCAT('%',:searchTerm, '%'))")
	List<Order> findBySearchTerm(@Param("searchTerm") String searchTerm);
	//, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Query("SELECT o FROM Order o WHERE o.created BETWEEN :startDate AND :endDate")
	List<Order> findOrdersByRange(@Param("startDate") Date startDate,
								  @Param("endDate") Date endDate);

	@Query("SELECT o FROM Order o WHERE " + "( o.manager = :user OR " + "o.master = :user OR " +
		   "o.designer = :user) AND " + "o.status = :status AND " + "o.deleted = :deleted ")
	Set<Order> findByUser(@Param("user") User user, @Param("status") Status status,
						  @Param("deleted") boolean deleted);

	@Query("SELECT o FROM Order o WHERE " + "( o.manager = :user OR " + "o.master = :user OR " +
		   "o.designer = :user) AND " + "o.status = :status AND " + "o.deleted = :deleted and o" +
		   ".created between STR_TO_DATE(:startDate, '%Y-%m-%d %H:%i:%s')  and  STR_TO_DATE" +
		   "(:endDate, '%Y-%m-%d %H:%i:%s')  ")
	Set<Order> findByUserAndDate(@Param("user") User user, @Param("status") Status status,
								 @Param("deleted") boolean deleted,
								 @Param("startDate") Date startDate,
								 @Param("endDate") Date endDate);


	@Query(
		"SELECT  date_format(o.created, :dwm) as created ,  AVG(o.price) as price FROM Order o " +
		"WHERE o.deleted=0 AND o.payment=1 AND o.created between STR_TO_DATE(:startDate, '%Y-%m-%d %H:%i:%s')  and  " +
		"STR_TO_DATE(:endDate, '%Y-%m-%d %H:%i:%s')  " +
		"GROUP BY date_format(o.created, :dwm) ORDER BY date_format(o.created, :dwm)")
	List<Object> priceAvgByMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
								 @Param("dwm") String dwm);

	@Query(value =
			   "SELECT  date_format(o.created, :dwm) as created , sum(o.price * o.payment) as price,  sum(o.price) FROM orders o " +
			   "WHERE o.deleted=0 and o.created between STR_TO_DATE(:startDate, " + "'%Y-%m-%d " +
			   "%H:%i:%s')  and  " + "STR_TO_DATE(:endDate, '%Y-%m-%d %H:%i:%s')  " + "GROUP BY " +
			   "date_format(o.created, :dwm) ORDER BY date_format(o.created, :dwm)",
		   nativeQuery = true)
	List<Object> sumPriceOrders(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
								@Param("dwm") String dwm);

	@Query("select o.delivery.city, count(o.delivery.city) from Order o " +
		   "  where o.deleted =0 and o.payment=1 group by o.delivery.city order by o" +
		   ".delivery.city")
	List<Object> statisticGeo();

	@Query(
		"SELECT  date_format(o.creationDate, '%Y-%m') as created ,  count(o.creationDate) FROM Customer o " +
		"WHERE o.deleted=0 AND o.creationDate between STR_TO_DATE(:startDate, '%Y-%m-%d %H:%i:%s')  and  STR_TO_DATE(:endDate, '%Y-%m-%d %H:%i:%s')  " +
		"GROUP BY " +
		"date_format(o.creationDate, '%Y-%m') ORDER BY date_format(o.creationDate, '%Y-%m')")
	List<Object> statisticNewCustomers(@Param("startDate") Date startDate,
									   @Param("endDate") Date endDate);

	@Query("SELECT o FROM Order o WHERE " + "( o.manager = :user OR " + "o.master = :user OR " +
		   "o.designer = :user) AND " + "o.status = :status AND " +
		   "o.deleted = false AND o.price >= :min  AND o.price <= " +
		   ":max AND o.created between STR_TO_DATE(:startDate, '%Y-%m-%d %H:%i:%s')  and STR_TO_DATE(:endDate, '%Y-%m-%d %H:%i:%s') ")
	List<Order> filterByPrice(@Param("min") Double min, @Param("max") Double max,
							  @Param("user") User user, @Param("status") Status status,
							  @Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Query("SELECT o FROM Order o WHERE " + "( o.manager = :user OR " + "o.master = :user OR " +
		   "o.designer = :user) AND " + "o.status = :status AND " +
		   "o.deleted = false AND o.price >= :min  AND o.created between STR_TO_DATE(:startDate, '%Y-%m-%d %H:%i:%s')  and STR_TO_DATE(:endDate, '%Y-%m-%d %H:%i:%s') ")
	List<Order> filterByPriceMin(@Param("min") Double min, @Param("user") User user,
								 @Param("status") Status status, @Param("startDate") Date startDate,
								 @Param("endDate") Date endDate);

	@Query("SELECT o FROM Order o WHERE " + "( o.manager = :user OR " + "o.master = :user OR " +
		   "o.designer = :user) AND " + "o.status = :status AND " +
		   "o.deleted = false AND o.price <= " +
		   ":max   AND o.created between STR_TO_DATE(:startDate, '%Y-%m-%d %H:%i:%s')  and STR_TO_DATE(:endDate, '%Y-%m-%d %H:%i:%s') ")
	List<Order> filterByPriceMax(@Param("max") Double max, @Param("user") User user,
								 @Param("status") Status status, @Param("startDate") Date startDate,
								 @Param("endDate") Date endDate);

	@Query("SELECT o FROM Order o WHERE o.deleted = false AND o.price >= :min  AND o.price <= " +
		   ":max  AND o.created between STR_TO_DATE(:startDate, '%Y-%m-%d %H:%i:%s')  and STR_TO_DATE(:endDate, '%Y-%m-%d %H:%i:%s') ")
	List<Order> filterByPriceForBoss(@Param("min") Double min, @Param("max") Double max,
									 @Param("startDate") Date startDate,
									 @Param("endDate") Date endDate);

	@Query(
		"SELECT o FROM Order o WHERE o.deleted = false AND o.price >= :min AND o.created between STR_TO_DATE(:startDate, '%Y-%m-%d %H:%i:%s')  and STR_TO_DATE(:endDate, '%Y-%m-%d %H:%i:%s')  ")
	List<Order> filterByPriceMinBoss(@Param("min") Double min, @Param("startDate") Date startDate,
									 @Param("endDate") Date endDate);

	@Query("SELECT o FROM Order o WHERE o.deleted = false AND o.price <= " +
		   ":max  AND o.created between STR_TO_DATE(:startDate, '%Y-%m-%d %H:%i:%s')  and STR_TO_DATE(:endDate, '%Y-%m-%d %H:%i:%s') ")
	List<Order> filterByPriceMaxBoss(@Param("max") Double max, @Param("startDate") Date startDate,
									 @Param("endDate") Date endDate);
}
