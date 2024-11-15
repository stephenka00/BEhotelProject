package hotel.SanhDieu.Service;

import hotel.SanhDieu.Model.User;
import hotel.SanhDieu.Response.JwtResponse;
import hotel.SanhDieu.request.LoginRequest;

import java.util.List;

public interface UserServiceInterface {
    User registerUser(User user);

    List<User> getUsers();

    void deleteUser(String email);

    User getUser(String email);
}
