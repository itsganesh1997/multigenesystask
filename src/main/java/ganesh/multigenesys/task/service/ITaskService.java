package ganesh.multigenesys.task.service;

import ganesh.multigenesys.task.entity.Customer;
import ganesh.multigenesys.task.entity.request.CustomerRequest;
import ganesh.multigenesys.task.entity.request.OrderPlaceRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITaskService {
    Object saveOrUpdateCustomer(CustomerRequest request, Customer userFromToken) throws Exception;

    Object getAllCustomers(Pageable pageable, Customer userFromToken) throws Exception;

    Object deleteCustomerById(Long customerId, Customer userFromToken) throws Exception;

    Object getCustomerByCustomerId(Long customerId, Customer userFromToken) throws Exception;

    Object placeOrders(List<OrderPlaceRequest> requestList, Customer userFromToken) throws Exception;

    Object UpdateOrderStatus(Long orderId, String status, Customer userFromToken) throws Exception;

    Object getAllOrders(Pageable pageable, Customer userFromToken) throws Exception;

    Object getOrdersByCustomerId(Long customerId, Customer userFromToken) throws Exception;

    Object getAllOrdersByDates(String startDate, String endDate, Customer userFromToken) throws Exception;

    Object getTotalAmountSpentByCustomer(Long customerId, Customer userFromToken) throws Exception;
}
