package hotel.SanhDieu.Response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class JwtResponse {
//    private int statusCode;
//    private String message;
//
//    private String token;
//    private List<String> roles;
//    private String expirationTime;
//    private String bookingConfirmationCode;
//
//
//    private RoomResponse room;
//    private BookingResponse booking;
//    private List<UserResponse> userList;
//    private List<RoomResponse> roomList;
//    private List<BookingResponse> bookingList;
    private Long id;
    private String email;
    private String token;
    private String type = "Bearer";
    private List<String> roles;

    public JwtResponse(Long id, String email, String token, List<String> roles) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.roles = roles;
    }
}
