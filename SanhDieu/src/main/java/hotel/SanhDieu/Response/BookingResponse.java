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

    public BookingResponse( Long bookingId,LocalDate checkOutDate, LocalDate checkInDate, String guestFullName
            , String guestEmail
            , int numOfAdults
            , int numOfChildren, int totalNumOfGuest, String bookingConfirmationCode, RoomResponse room) {
        this.checkOutDate = checkOutDate;
        this.id = bookingId;
        this.checkInDate = checkInDate;
        this.guestFullName = guestFullName;
        this.guestEmail = guestEmail;
        NumOfAdults = numOfAdults;
        NumOfChildren = numOfChildren;
        TotalNumOfGuest = totalNumOfGuest;
        this.bookingConfirmationCode = bookingConfirmationCode;
        this.room = room;
    }


}
