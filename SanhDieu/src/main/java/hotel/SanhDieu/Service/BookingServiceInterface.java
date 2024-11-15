package hotel.SanhDieu.Service;

import hotel.SanhDieu.Model.BookedRoom;

import java.util.List;

public interface BookingServiceInterface {
    void cancelBooking(Long bookingId);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> getAllBooking();
}
