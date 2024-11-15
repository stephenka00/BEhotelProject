package hotel.SanhDieu.Service;


import hotel.SanhDieu.Exception.InternalServerException;
import hotel.SanhDieu.Exception.ResourceNotFoundException;
import hotel.SanhDieu.Model.Room;
import hotel.SanhDieu.Repository.RoomRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomServiceInterface{

    @Autowired
    private RoomRepository roomRepository;


    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice)throws IOException, SQLException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if(!file.isEmpty()){
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            room.setPhoto(photoBlob);
        }
        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Room addRoomType(String roomType) {
        List<String> existingRoomTypes = roomRepository.findDistinctRoomTypes();
        if (existingRoomTypes.contains(roomType)) {
            // Option 1: Throw an exception
            throw new IllegalArgumentException("Room type already exists");
        }

        // Create a new Room entity with only roomType populated
        Room newRoomType = new Room();
        newRoomType.setRoomType(roomType);

        // Save and return the new room type
        return roomRepository.save(newRoomType);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
        Optional<Room> theRoom = roomRepository.findById(roomId);
        if(theRoom.isEmpty()){
            throw new ResourceNotFoundException("Room not found!");
        }
        Blob photoBlob = theRoom.get().getPhoto();
        if(photoBlob != null){
            return photoBlob.getBytes(1,(int) photoBlob.length());
        }
        return null;
    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> theRoom = roomRepository.findById(roomId);
        if(theRoom.isPresent()){
            roomRepository.deleteById(roomId);
        }
    }

    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) throws InternalServerException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        if(roomType != null) room.setRoomType(roomType);
        if(roomPrice != null) room.setRoomPrice(roomPrice);
        if(photoBytes != null && photoBytes.length > 0) {
            try{
                room.setPhoto(new SerialBlob(photoBytes));
            }catch(SQLException ex){
                throw new InternalServerException("Error updating room");
            }
        }
        return roomRepository.save(room);
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return Optional.of(roomRepository.findById(roomId).get());
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        return roomRepository.findAvailableRoomsByDateAndType(checkInDate,checkOutDate,roomType);
    }

}

