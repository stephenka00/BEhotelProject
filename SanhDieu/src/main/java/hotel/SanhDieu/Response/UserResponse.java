package hotel.SanhDieu.Response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String role;
    private List<BookingResponse> bookings = new ArrayList<>();
}
