package ganesh.multigenesys.task.entity.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrdersRequest {
    private Long orderId;
    private LocalDate orderDate;
    private Double orderAmount;
    private String status;

}
