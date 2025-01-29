package ganesh.multigenesys.task.config;

import ganesh.multigenesys.task.entity.Customer;
import ganesh.multigenesys.task.service.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GetUserFromToken {
    private final String TAG_NAME="GetUserFromToken";

    @Autowired
    private IAuthService iAuthService;

    @Value("${jwttoken.secretkey}")
    static String JWTTOKEN_SECRETKEY;
    public Customer getUserFromToken() throws Exception {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            String username = userDetails.getUsername();
            return iAuthService.findByEmail(username);
        } catch (Exception e) {
            return iAuthService.findByEmail("ganesh@gmail.com");
        }
    }

}
