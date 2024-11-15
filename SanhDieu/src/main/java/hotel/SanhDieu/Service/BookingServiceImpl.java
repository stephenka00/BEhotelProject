package hotel.SanhDieu.Service;


import hotel.SanhDieu.Exception.InvalidBookingRequestException;
import hotel.SanhDieu.Exception.ResourceNotFoundException;
import hotel.SanhDieu.Model.BookedRoom;
import hotel.SanhDieu.Model.Room;
import hotel.SanhDieu.Repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingServiceInterface{
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomServiceInterface roomService;

    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw new InvalidBookingRequestException("Check-in date must come first before check-out date");
        }
        Room room = roomService.getRoomById(roomId).get();
        List<BookedRoom> existingBookings = room.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRequest,existingBookings);
        if(roomIsAvailable){
            room.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        }else{
            throw new InvalidBookingRequestException("Sorry, this room is not available for selected dates");
        }

        return bookingRequest.getBookingConfirmationCode();
    }

    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking -> bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                    || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                    || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                    && bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate()))
                    || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                    && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                    || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                    && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                    || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                    && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                        && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );

    }

    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No booking found with booking code: " + confirmationCode));
    }

    @Override
    public List<BookedRoom> getAllBooking() {
        return bookingRepository.findAll();
    }


}
