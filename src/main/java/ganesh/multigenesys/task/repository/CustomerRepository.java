package ganesh.multigenesys.task.repository;

import ganesh.multigenesys.task.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    boolean existsByEmailAndIsDeleted(String email, boolean b);

    boolean existsByPhoneNumberAndIsDeleted(String phoneNumber, boolean b);

    boolean existsByEmailAndIsDeletedAndCustomerIdNotIn(String email, boolean b, List<Long> customerIdList);

    boolean existsByPhoneNumberAndIsDeletedAndCustomerIdNotIn(String phoneNumber, boolean b, List<Long> customerIdList);

    @Query(value="select c.* from customer as c where lower(c.email)=:email and c.is_deleted=0", nativeQuery=true)
    Customer findByUsername(@Param("email") String email);

    @Query(value = "select c from Customer as c where c.email=:username and c.isDeleted=:b",nativeQuery = false)
    Customer getByCustomerByUsername(String username, boolean b);

    Page<Customer> findAllByIsActiveAndIsDeletedOrderByCreatedAtDesc(boolean t, boolean f ,Pageable pageable);

    boolean existsByCustomerIdAndIsDeleted(Long customerId, boolean b);

    Customer findByCustomerIdAndIsDeleted(Long customerId, boolean b);

    boolean existsByCustomerId(Long customerId);

    Customer findByCustomerId(Long customerId);
}
