package hotel.SanhDieu.Service;

import hotel.SanhDieu.Exception.InternalServerException;
import hotel.SanhDieu.Exception.UserAlreadyExistsException;
import hotel.SanhDieu.Model.Role;
import hotel.SanhDieu.Model.User;
import hotel.SanhDieu.Repository.RoleRepository;
import hotel.SanhDieu.Repository.UserRepository;
import hotel.SanhDieu.Response.JwtResponse;
import hotel.SanhDieu.request.LoginRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @Override
    public User registerUser(User user) {
       if(userRepository.existsByEmail(user.getEmail())){
           throw new UserAlreadyExistsException(user.getEmail() + " already exists");
       }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singletonList(userRole));
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }


    @Transactional
    @Override
    public void deleteUser(String email) {
        User theUser = getUser(email);
        if(theUser != null){
            userRepository.deleteByEmail(email);
        }
    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
