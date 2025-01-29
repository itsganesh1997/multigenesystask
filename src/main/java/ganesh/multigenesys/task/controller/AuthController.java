package ganesh.multigenesys.task.controller;


import ganesh.multigenesys.task.config.TokenProvider;
import ganesh.multigenesys.task.entity.Customer;
import ganesh.multigenesys.task.entity.request.AdminRegisterRequest;
import ganesh.multigenesys.task.entity.request.CustomerRequest;
import ganesh.multigenesys.task.entity.request.LoginRequest;
import ganesh.multigenesys.task.entity.response.CustomResponseMessage;
import ganesh.multigenesys.task.entity.response.LoginResponse;
import ganesh.multigenesys.task.service.IAuthService;
import ganesh.multigenesys.task.service.ITaskService;
import io.swagger.annotations.Api;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@Api(value = "Customer Authentication ", description = "Customer Authentication ", tags = {"Customer authentication "})
public class AuthController {
    @Autowired
    private ITaskService iTaskService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IAuthService iAuthService;
    @Autowired
    private TokenProvider jwtTokenUtil;


    @PostMapping("/adminRegister")
    public ResponseEntity<?> adminRegister(@RequestBody AdminRegisterRequest registerRequest) {
        try {
            return new ResponseEntity<>(new CustomResponseMessage(iAuthService.adminRegister(registerRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomResponseMessage(e.getMessage(), -1), HttpStatus.OK);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login1(@RequestBody LoginRequest loginRequest, HttpServletRequest request) throws Exception {
        try {
            final Authentication authentication = authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            final UserDetails candidateDetails = iAuthService.loadUserByUsername(loginRequest.getEmail());
            Customer user = iAuthService.findByEmail(candidateDetails.getUsername());

            if (!user.getIsActive() || user.getIsDeleted()) {
                throw new Exception("User Not Active ");
            }
            String token = "";
            try {
                token = jwtTokenUtil.generateToken(authentication);
            } catch (Exception e) {
                e.printStackTrace();
            }

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            loginResponse.setCustomerId(user.getCustomerId());
            loginResponse.setName(user.getName());
            loginResponse.setRole(user.getRole().toString());

            System.out.println("token: " + loginResponse.getToken());

            return new ResponseEntity<>(new CustomResponseMessage(loginResponse, 0), HttpStatus.OK);
        } catch (InvalidCredentialsException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponseMessage(e.getMessage(), -1), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CustomResponseMessage(e.getMessage(), -1), HttpStatus.UNAUTHORIZED);
        }
    }

    private Authentication authenticate(String username, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Please Check Username and Password");
        }
    }


}
