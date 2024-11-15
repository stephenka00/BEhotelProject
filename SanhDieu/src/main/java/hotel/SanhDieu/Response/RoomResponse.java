package hotel.SanhDieu.Response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import java.util.Base64;
import java.util.List;

@Data
@NoArgsConstructor
public class RoomResponse {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked;
    private String photo;
    private List<BookingResponse> bookings; // Danh sách đặt phòng

    public RoomResponse(BigDecimal roomPrice, String roomType, Long id) {
        this.roomPrice = roomPrice;
        this.roomType = roomType;
        this.id = id;
    }

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice
           , boolean isBooked, byte[] photoBytes,List<BookingResponse> bookings) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.bookings = bookings;
        this.isBooked = isBooked;
        this.photo = photoBytes != null ? Base64.getEncoder().encodeToString(photoBytes) : null;
    }

    public Long getRoomId() {
        return id;
    }

    public void setRoomId(Long roomId) {
        this.id = roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public BigDecimal getPricePerNight() {
        return roomPrice;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.roomPrice = pricePerNight;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<BookingResponse> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingResponse> bookings) {
        this.bookings = bookings;
    }
}
