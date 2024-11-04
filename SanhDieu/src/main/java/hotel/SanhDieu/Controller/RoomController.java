package hotel.SanhDieu.Controller;


import hotel.SanhDieu.Exception.InternalServerException;
import hotel.SanhDieu.Exception.PhotoRetrievalException;
import hotel.SanhDieu.Exception.ResourceNotFoundException;
import hotel.SanhDieu.Model.BookedRoom;
import hotel.SanhDieu.Model.Room;
import hotel.SanhDieu.Response.RoomResponse;
import hotel.SanhDieu.Service.BookingServiceImpl;
import hotel.SanhDieu.Service.RoomServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RoomController {
    @Autowired
    private RoomServiceInterface roomService;
    @Autowired
    private BookingServiceImpl bookingService;

    // Phương thức thêm phòng mới với đầy đủ thông tin
    @PostMapping("/add/new-room")
    public ResponseEntity<RoomResponse> addNewRoom(
            @RequestParam("photo") MultipartFile photo
            , @RequestParam("roomType") String roomType
            , @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException {
        Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
        RoomResponse response = new RoomResponse(savedRoom.getPricePerNight(),
                savedRoom.getRoomType()
                , savedRoom.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/room/types")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    @PostMapping("/add/room-type")
    public ResponseEntity<String> addRoomType(@RequestBody Map<String, String> request) {
        String roomType = request.get("roomType");

        // Validate the input
        if (roomType == null || roomType.isEmpty()) {
            return ResponseEntity.badRequest().body("Room type cannot be empty");
        }

        try {
            // Call the service to add the room type
            Room newRoom = roomService.addRoomType(roomType);
            return ResponseEntity.ok(newRoom.getRoomType());
        } catch (IllegalArgumentException e) {
            // Handle the case where the room type already exists
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Handle any other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while adding the room type");
        }
    }
    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = new ArrayList<>();
        for(Room room : rooms){
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if(photoBytes != null && photoBytes.length > 0){
                String base64Photo = Base64.getEncoder().encodeToString(photoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setPhoto(base64Photo);
                roomResponses.add(roomResponse);
            }
        }
        return ResponseEntity.ok(roomResponses);
    }
    @DeleteMapping("/delete/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId){
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/update/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(Long roomId,@RequestParam(required = false) String roomType,
                                                   @RequestParam(required = false)BigDecimal roomPrice,
                                                   @RequestParam(required = false)MultipartFile photo) throws IOException, SQLException, InternalServerException {
        byte[]photoBytes = photo != null && !photo.isEmpty()
                ? photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);
        Blob photoBlob = photoBytes != null && photoBytes.length >0 ?
                new SerialBlob(photoBytes) : null;
        Room theRoom = roomService.updateRoom(roomId,roomType,roomPrice,photoBytes);
        theRoom.setPhoto(photoBlob);
        RoomResponse roomResponse = getRoomResponse(theRoom);
        return ResponseEntity.ok(roomResponse);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId) throws SQLException {
        Optional<Room> theRoom = roomService.getRoomById(roomId);
        return theRoom.map(room ->{
            RoomResponse roomResponse = getRoomResponse(room);
            return ResponseEntity.ok(Optional.of(roomResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }
    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());
        /*List<BookingResponse> bookingInfo = bookings
                .stream()
                .map(booking -> new BookingResponse(booking.getBookingId()
                        ,booking.getCheckInDate()
                        ,booking.getCheckOutDate()
                        ,booking.getBookingConfirmationCode())).toList();*/
        byte[] photoBytes = null;
        Blob photoBlob = room.getPhoto();
        if(photoBlob != null){
            try{
                photoBytes = photoBlob.getBytes(1,(int) photoBlob.length());

            }catch(SQLException e){
                throw new PhotoRetrievalException("False to retrieving photo");
            }
        }
        return new RoomResponse(room.getId()
                ,room.getRoomType()
                ,room.getPricePerNight()
                ,room.isBooked()
                ,photoBytes);
    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingService.getAllBookingsByRoomId(roomId);
    }
}
