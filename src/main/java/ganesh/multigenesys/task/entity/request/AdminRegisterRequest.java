package ganesh.multigenesys.task.entity.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRegisterRequest {

    //private Long customerId;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
}
