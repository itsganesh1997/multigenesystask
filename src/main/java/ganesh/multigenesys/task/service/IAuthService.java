package ganesh.multigenesys.task.service;

import ganesh.multigenesys.task.entity.Customer;
import ganesh.multigenesys.task.entity.request.AdminRegisterRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface IAuthService {

    UserDetails loadUserByUsername(String email);

    Customer findByEmail(String username) throws Exception;

    Object adminRegister(AdminRegisterRequest registerRequest);
}
