package ganesh.multigenesys.task.service;

import ganesh.multigenesys.task.entity.Customer;
import ganesh.multigenesys.task.entity.Role;
import ganesh.multigenesys.task.entity.request.AdminRegisterRequest;
import ganesh.multigenesys.task.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService implements UserDetailsService, IAuthService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Customer user = customerRepository.findByUsername(s);

        if (user == null) {
            throw new UsernameNotFoundException("Login not enable for this user or Invalid username");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                getAuthority(user));

    }

    private Collection<? extends GrantedAuthority> getAuthority(Customer user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + "ADMIN"));
        return authorities;
    }

    @Override
    public Customer findByEmail(String username) throws Exception {
        Customer user = customerRepository.getByCustomerByUsername(username, false);

        if (user == null) {
            throw new Exception("Username Invalid");
        }
        return user;
    }

    @Override
    public Object adminRegister(AdminRegisterRequest registerRequest) {
        try {
            Customer user = new Customer();
            user.setEmail(registerRequest.getEmail());
            user.setPhoneNumber(registerRequest.getPhoneNumber());
            user.setPassword(bcryptEncoder.encode(registerRequest.getPassword()));
            user.setName(registerRequest.getName());
            user.setRole(Role.ADMIN);
            user.setIsActive(true);
            user.setIsDeleted(false);
            customerRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Admin Register";

    }
}
