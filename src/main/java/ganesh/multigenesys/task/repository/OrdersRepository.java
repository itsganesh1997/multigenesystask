package ganesh.multigenesys.task.repository;

import ganesh.multigenesys.task.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Order,Long> {
    boolean existsByOrderId(Long orderId);

    Order findByOrderId(Long orderId);

    @Query(value = "SELECT o FROM Order o ORDER BY o.orderDate DESC",nativeQuery = false)
    Page<Order> getAllOrders(Pageable pageable);

    List<Order> findByCustomerCustomerId(Long customerId);

    boolean existsByCustomerCustomerId(Long customerId);

    @Query(value = "select * from orders where order_date between (:startDate1) and (:endDate1)",nativeQuery = true)
    List<Order> getOrderByDateFilter(LocalDate startDate1, LocalDate endDate1);

    @Query("SELECT COALESCE(SUM(o.orderAmount), 0) FROM Order o WHERE o.customer.customerId = :customerId")
    Double getTotalSpentAmountByCustomerId(@Param("customerId") Long customerId);

}
