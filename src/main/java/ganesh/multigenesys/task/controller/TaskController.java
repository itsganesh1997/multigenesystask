package ganesh.multigenesys.task.controller;

import ganesh.multigenesys.task.config.GetUserFromToken;
import ganesh.multigenesys.task.entity.request.CustomerRequest;
import ganesh.multigenesys.task.entity.request.OrderPlaceRequest;
import ganesh.multigenesys.task.entity.response.CustomResponseMessage;
import ganesh.multigenesys.task.repository.CustomerRepository;
import ganesh.multigenesys.task.repository.OrdersRepository;
import ganesh.multigenesys.task.service.IAuthService;
import ganesh.multigenesys.task.service.ITaskService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/task")
@Api(value = "Customer Tasks ", description = "Customer Tasks ", tags = {"Customer Tasks "})
public class TaskController {
    @Autowired
    private ITaskService iTaskService;
    @Autowired
    private GetUserFromToken getUserFromToken;


    @PostMapping("/saveOrUpdateCustomer")
    public ResponseEntity<?> saveOrUpdateCustomer(@RequestBody CustomerRequest request) {
        try {
            return new ResponseEntity<>(new CustomResponseMessage(iTaskService.saveOrUpdateCustomer(request, getUserFromToken.getUserFromToken()), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomResponseMessage(e.getMessage(), -1), HttpStatus.OK);
        }
    }

    @GetMapping("/getAllCustomers")
    public ResponseEntity<?> getAllCustomers(@RequestParam(defaultValue = "0", required = false) Integer pageNo,
                                             @RequestParam(defaultValue = "30", required = false) Integer pageSize) {
        try {
            Pageable pageable = PageRequest.of(Optional.ofNullable(pageNo).orElse(0), Optional.ofNullable(pageSize).orElse(50));

            return new ResponseEntity<>(new CustomResponseMessage(iTaskService.getAllCustomers(pageable, getUserFromToken.getUserFromToken()), 0), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponseMessage(e.getMessage(), -1), HttpStatus.OK);
        }
    }

    @DeleteMapping("/deleteCustomerById")
    public ResponseEntity<?> deleteCustomerById(@RequestParam("customerId") Long customerId) {
        try {
            return new ResponseEntity<>(new CustomResponseMessage(iTaskService.deleteCustomerById(customerId, getUserFromToken.getUserFromToken()), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomResponseMessage(e.getMessage(), -1), HttpStatus.OK);
        }
    }

    @GetMapping("/getCustomerByCustomerId")
    public ResponseEntity<?> getCustomerByCustomerId(@RequestParam Long customerId) {
        try {
            return new ResponseEntity<>(new CustomResponseMessage(iTaskService.getCustomerByCustomerId(customerId, getUserFromToken.getUserFromToken()), 0), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponseMessage(e.getMessage(), -1), HttpStatus.OK);
        }
    }

    //ORDERS API

    @PostMapping("/placeOrders")
    public ResponseEntity<?> placeOrders(@RequestBody List<OrderPlaceRequest> requestList) {
        try {
            return new ResponseEntity<>(new CustomResponseMessage(iTaskService.placeOrders(requestList, getUserFromToken.getUserFromToken()), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomResponseMessage(e.getMessage(), -1), HttpStatus.OK);
        }
    }

    @PutMapping("/UpdateOrderStatus")
    public ResponseEntity<?> UpdateOrderStatus(@RequestParam Long orderId, @RequestParam String status) {
        try {
            return new ResponseEntity<>(new CustomResponseMessage(iTaskService.UpdateOrderStatus(orderId, status, getUserFromToken.getUserFromToken()), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomResponseMessage(e.getMessage(), -1), HttpStatus.OK);
        }
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<?> getAllOrders(@RequestParam(defaultValue = "0", required = false) Integer pageNo,
                                          @RequestParam(defaultValue = "30", required = false) Integer pageSize) {
        try {
            Pageable pageable = PageRequest.of(Optional.ofNullable(pageNo).orElse(0), Optional.ofNullable(pageSize).orElse(50));

            return new ResponseEntity<>(new CustomResponseMessage(iTaskService.getAllOrders(pageable, getUserFromToken.getUserFromToken()), 0), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponseMessage(e.getMessage(), -1), HttpStatus.OK);
        }
    }


    @GetMapping("/getOrdersByCustomerId")
    public ResponseEntity<?> getOrdersByCustomerId(@RequestParam Long customerId) {
        try {
            return new ResponseEntity<>(new CustomResponseMessage(iTaskService.getOrdersByCustomerId(customerId, getUserFromToken.getUserFromToken()), 0), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponseMessage(e.getMessage(), -1), HttpStatus.OK);
        }
    }


    @GetMapping("/getAllOrdersByDates")
    public ResponseEntity<?> getAllOrdersByDates(@RequestParam String startDate,
                                                 @RequestParam String endDate) {
        try {
            return new ResponseEntity<>(new CustomResponseMessage(iTaskService.getAllOrdersByDates(startDate, endDate, getUserFromToken.getUserFromToken()), 0), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponseMessage(e.getMessage(), -1), HttpStatus.OK);
        }
    }

    @GetMapping("/getTotalAmountSpentByCustomer")
    public ResponseEntity<?> getTotalAmountSpentByCustomer(@RequestParam Long customerId) {
        try {
            return new ResponseEntity<>(new CustomResponseMessage(iTaskService.getTotalAmountSpentByCustomer(customerId, getUserFromToken.getUserFromToken()), 0), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponseMessage(e.getMessage(), -1), HttpStatus.OK);
        }
    }

}
