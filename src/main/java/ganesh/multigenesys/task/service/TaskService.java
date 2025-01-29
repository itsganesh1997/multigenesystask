package ganesh.multigenesys.task.service;

import ganesh.multigenesys.task.entity.Customer;
import ganesh.multigenesys.task.entity.Order;
import ganesh.multigenesys.task.entity.OrderStatus;
import ganesh.multigenesys.task.entity.Role;
import ganesh.multigenesys.task.entity.request.CustomerRequest;
import ganesh.multigenesys.task.entity.request.OrderPlaceRequest;
import ganesh.multigenesys.task.entity.response.PageDto;
import ganesh.multigenesys.task.repository.CustomerRepository;
import ganesh.multigenesys.task.repository.OrdersRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TaskService implements ITaskService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;


    @Override
    public Object saveOrUpdateCustomer(CustomerRequest request, Customer userFromToken) throws Exception {
        if (userFromToken.getRole().equals(Role.ADMIN)) {
            if (customerRepository.existsById(request.getCustomerId())) {
                Customer customer = new Customer();
                List<Long> customerIdList = new ArrayList<>();
                customerIdList.add(request.getCustomerId());
                if (customerRepository.existsByEmailAndIsDeletedAndCustomerIdNotIn(request.getEmail(), false, customerIdList)) {
                    throw new Exception("this Email is already exists");
                } else {
                    customer.setEmail(request.getEmail());
                }

                if (customerRepository.existsByPhoneNumberAndIsDeletedAndCustomerIdNotIn(request.getPhoneNumber(), false, customerIdList)) {
                    throw new Exception("this Phone Number is already exists");
                } else {
                    customer.setPhoneNumber(request.getPhoneNumber());
                }

                customer.setName(request.getName());
                //customer.setPassword(bcryptEncoder.encode(request.getPassword()));
                if (request.getRole().equalsIgnoreCase("ADMIN")) {
                    customer.setRole(Role.ADMIN);
                } else if (request.getRole().equalsIgnoreCase("USER")) {
                    customer.setRole(Role.USER);
                } else {
                }
                customerRepository.save(customer);
                return "Customer Details Updated";
            } else {
                Customer customer = new Customer();
                customer.setIsDeleted(false);
                customer.setIsActive(true);
                if (customerRepository.existsByEmailAndIsDeleted(request.getEmail(), false)) {
                    throw new Exception("this Email is already exists");
                } else {
                    customer.setEmail(request.getEmail());
                }

                if (customerRepository.existsByPhoneNumberAndIsDeleted(request.getPhoneNumber(), false)) {
                    throw new Exception("this Phone Number is already exists");
                } else {
                    customer.setPhoneNumber(request.getPhoneNumber());
                }

                customer.setName(request.getName());
                customer.setPassword(bcryptEncoder.encode(request.getPassword()));
                if (request.getRole().equalsIgnoreCase("ADMIN")) {
                    customer.setRole(Role.ADMIN);
                } else if (request.getRole().equalsIgnoreCase("USER")) {
                    customer.setRole(Role.USER);
                } else {
                }
                customerRepository.save(customer);
                return "Customer Details Saved";
            }

        } else {
            throw new Exception("Only admin Can Add User");
        }
    }

    @Override
    public Object getAllCustomers(Pageable pageable, Customer userFromToken) throws Exception {
        if (userFromToken.getRole().equals(Role.ADMIN)) {
            Page<Customer> customerPage = customerRepository.findAllByIsActiveAndIsDeletedOrderByCreatedAtDesc(true, false, pageable);
            return new PageDto(customerPage.getContent(), customerPage.getTotalPages(), customerPage.getTotalElements(), customerPage.getNumber());
        } else {
            throw new Exception("Only Admin Can View All Users");
        }
    }

    @Override
    public Object deleteCustomerById(Long customerId, Customer userFromToken) throws Exception {
        if (userFromToken.getRole().equals(Role.ADMIN)) {
            if (customerRepository.existsByCustomerIdAndIsDeleted(customerId, false)) {
                Customer customer = customerRepository.findByCustomerIdAndIsDeleted(customerId, false);
                customer.setIsActive(false);
                customer.setIsDeleted(true);
                customerRepository.save(customer);
                return "Customer Deleted";
            } else {
                throw new Exception("This Customer Id is not exist");
            }
        } else {
            throw new Exception("Only Admin Can Delete User");
        }
    }

    @Override
    public Object getCustomerByCustomerId(Long customerId, Customer userFromToken) throws Exception {

        if (userFromToken.getRole().equals(Role.ADMIN)) {
            return customerRepository.findById(customerId).orElseThrow(() -> new Exception("Customer Id does not exist"));
        }

        if (userFromToken.getRole().equals(Role.USER)) {
            if (!userFromToken.getCustomerId().equals(customerId)) {
                throw new Exception("You can only view your own profile");
            }
            return customerRepository.findById(customerId).orElseThrow(() -> new Exception("Customer Id does not exist"));
        }

        throw new Exception("Role dont have any access");

    }

    @Override
    public Object placeOrders(List<OrderPlaceRequest> requestList, Customer userFromToken) throws Exception {
        if (!userFromToken.getRole().equals(Role.USER)) {
            throw new Exception("Only Users can place orders");
        }
        if (requestList == null || requestList.isEmpty()) {
            throw new Exception("Order list cannot be empty");
        }

        Customer customer = customerRepository.findById(userFromToken.getCustomerId())
                .orElseThrow(() -> new Exception("Customer not found with Id: " + userFromToken.getCustomerId()));

        for (OrderPlaceRequest request : requestList) {

            if (ordersRepository.existsByOrderId(request.getOrderId())) {
                Order order = ordersRepository.findByOrderId(request.getOrderId());
                saveUpdateOrder(order, customer, request);
            } else {
                Order order = new Order();
                saveUpdateOrder(order, customer, request);
            }
        }
        return "Order placed successfully!";

    }

    private void saveUpdateOrder(Order order, Customer customer, OrderPlaceRequest request) {
        order.setOrderDate(request.getOrderDate());
        order.setOrderAmount(request.getOrderAmount());
        order.setCustomer(customer);
        if (request.getStatus().equalsIgnoreCase("PENDING")) {
            order.setStatus(OrderStatus.PENDING);
        } else if (request.getStatus().equalsIgnoreCase("COMPLETED")) {
            order.setStatus(OrderStatus.COMPLETED);
        } else if (request.getStatus().equalsIgnoreCase("CANCELLED")) {
            order.setStatus(OrderStatus.CANCELLED);
        } else {
        }

        ordersRepository.save(order);
    }

    @Override
    public Object UpdateOrderStatus(Long orderId, String status, Customer userFromToken) throws Exception {
        if (!userFromToken.getRole().equals(Role.ADMIN)) {
            throw new Exception("Only Admin can change status of orders");
        }
        Order order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found with Id: " + orderId));

        if (status.equalsIgnoreCase("PENDING")) {
            order.setStatus(OrderStatus.PENDING);
        } else if (status.equalsIgnoreCase("COMPLETED")) {
            order.setStatus(OrderStatus.COMPLETED);
        } else if (status.equalsIgnoreCase("CANCELLED")) {
            order.setStatus(OrderStatus.CANCELLED);
        } else {
        }
        ordersRepository.save(order); // Save the updated order status
        return "Order status updated successfully!";

    }

    @Override
    public Object getAllOrders(Pageable pageable, Customer userFromToken) throws Exception {
        if (!userFromToken.getRole().equals(Role.ADMIN)) {
            throw new Exception("Only Admin can fetch All orders");
        }
        Page<Order> ordersPage = ordersRepository.getAllOrders(pageable);
        return new PageDto(ordersPage.getContent(), ordersPage.getTotalPages(), ordersPage.getTotalElements(), ordersPage.getNumber());
    }

    @Override
    public Object getOrdersByCustomerId(Long customerId, Customer userFromToken) throws Exception {
        if (customerId == null && !ordersRepository.existsByCustomerCustomerId(customerId)) {
            throw new Exception("Id Not Exists");
        }
        if (userFromToken.getRole().equals(Role.ADMIN)) {
            log.info("in Admin");
            List<Order> orders = ordersRepository.findByCustomerCustomerId(customerId);
            return orders;
        }

        if (userFromToken.getRole().equals(Role.USER)) {
            log.info(" in User");

            if (!userFromToken.getCustomerId().equals(customerId)) {
                throw new Exception("You are not authorized to view these orders");
            }
            List<Order> orders = ordersRepository.findByCustomerCustomerId(customerId);
            return orders;
        }

        throw new Exception("Unauthorized access");

    }

    @Override
    public Object getAllOrdersByDates(String startDate, String endDate, Customer userFromToken) throws Exception {
        if (!userFromToken.getRole().equals(Role.ADMIN)) {
            throw new Exception("Only Admin can fetch orders");
        }

        if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            try {
                LocalDate startDate1 = LocalDate.parse(startDate);
                LocalDate endDate1 = LocalDate.parse(endDate);

                List<Order> orderList = new ArrayList<>();
                orderList = ordersRepository.getOrderByDateFilter(startDate1, endDate1);
                return orderList;
            } catch (DateTimeParseException e) {
                throw new Exception("Invalid date format. Use YYYY-MM-DD format");
            }
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Object getTotalAmountSpentByCustomer(Long customerId, Customer userFromToken) throws Exception {
        if (customerId == null && !ordersRepository.existsByCustomerCustomerId(customerId)) {
            throw new Exception("Id Not Exists");
        }
        if (userFromToken.getRole().equals(Role.ADMIN)) {
            log.info("Admin");
            Double totalSpentAmt = ordersRepository.getTotalSpentAmountByCustomerId(customerId);
            return "Total Spent Amount of User is: " + totalSpentAmt;
        }

        if (userFromToken.getRole().equals(Role.USER)) {
            log.info("User");

            if (!userFromToken.getCustomerId().equals(customerId)) {
                throw new Exception("You are not authorized to these");
            }
            Double totalSpentAmt = ordersRepository.getTotalSpentAmountByCustomerId(customerId);
            return "Total Spent Amount by you is: " + totalSpentAmt;
        }

        throw new Exception("Unauthorized access");

    }
}
