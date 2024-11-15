package hotel.SanhDieu.Controller;

import hotel.SanhDieu.Exception.InvalidBookingRequestException;
import hotel.SanhDieu.Exception.ResourceNotFoundException;
import hotel.SanhDieu.Model.BookedRoom;
import hotel.SanhDieu.Model.Room;
import hotel.SanhDieu.Response.BookingResponse;
import hotel.SanhDieu.Response.RoomResponse;
import hotel.SanhDieu.Service.BookingServiceInterface;
import hotel.SanhDieu.Service.RoomServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;



@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingServiceInterface bookingService;

    private final RoomServiceInterface roomService;

    @GetMapping("/all-bookings")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<BookingResponse>>getAllBookings(){
        List<BookedRoom> bookings = bookingService.getAllBooking();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for(BookedRoom booking:bookings){
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        try{
            BookedRoom booking = bookingService.findByBookingConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(booking);
            return ResponseEntity.ok(bookingResponse);
        }catch (ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId,
                                         @RequestBody BookedRoom bookingRequest){
        try{
            String confirmationCode = bookingService.saveBooking(roomId,bookingRequest);
            return ResponseEntity.ok(
                    "Room booked successfully ! Your booking confirmation code is :"+ confirmationCode);
        }catch(InvalidBookingRequestException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(Long bookingId){
        bookingService.cancelBooking(bookingId);
    }

    private BookingResponse getBookingResponse(BookedRoom booking) {
        Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
        RoomResponse room = new RoomResponse(
                theRoom.getRoomPrice(),
                theRoom.getRoomType(),
                theRoom.getId());
       return new BookingResponse(booking.getBookingId(),booking.getCheckInDate(),booking.getCheckOutDate(),
                booking.getGuestFullName(),booking.getGuestEmail()
               ,booking.getNumOfAdults(), booking.getNumOfChildren(),
               booking.getTotalNumOfGuest(),booking.getBookingConfirmationCode(), room);
    }
}
