package hotel.SanhDieu.Response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
//dùng để trả về thông tin phòng sau khi phòng đã được
// lưu trong cơ sở dữ liệu, bao gồm cả thông tin liên quan đến việc đã được đặt hay chưa.
public class BookingResponse {
    private Long id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private BigDecimal totalPrice;

    private String guestFullName;

    private String guestEmail;

    private int NumOfAdults;

    private int NumOfChildren;

    private int TotalNumOfGuest;

    private String bookingConfirmationCode;

    private LocalDateTime createdDate;
    private RoomResponse room;

    public BookingResponse(Long id, LocalDate checkInDate, LocalDate checkOutDate
            , String bookingConfirmationCode) {
        this.id = id;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
