package ganesh.multigenesys.task.entity.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private Long customerId;
    private String name;
    private String role;
}
