package ganesh.multigenesys.task.entity.request;

import ganesh.multigenesys.task.entity.Customer;
import ganesh.multigenesys.task.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
public class OrderPlaceRequest {
    private Long orderId;
    private LocalDate orderDate;
    private Double orderAmount;
    private String status;
    //private Long customerId;

}
