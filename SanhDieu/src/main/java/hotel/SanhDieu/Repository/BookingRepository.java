package hotel.SanhDieu.Repository;

import hotel.SanhDieu.Model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookedRoom,Long> {
    Optional<BookedRoom> findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> findByRoomId(Long roomId);


}
