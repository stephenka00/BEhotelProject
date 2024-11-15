package hotel.SanhDieu.Controller;

import hotel.SanhDieu.Exception.UserAlreadyExistsException;
import hotel.SanhDieu.Model.User;
import hotel.SanhDieu.Response.JwtResponse;
import hotel.SanhDieu.Service.UserServiceInterface;
import hotel.SanhDieu.security.jwt.JwtUtils;
import org.springframework.security.core.GrantedAuthority;
import hotel.SanhDieu.request.LoginRequest;
import hotel.SanhDieu.security.user.HotelUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceInterface userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        try{
            userService.registerUser(user);
            return ResponseEntity.ok("Registration successfully");
        }catch(UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    //Login
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request){
        Authentication authentication =
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtTokenForUser(authentication);
        HotelUserDetails userDetails = (HotelUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.ok(new JwtResponse(
                userDetails.getId(),
                userDetails.getEmail(),
                jwt,
                roles));
    }

}
