package hotel.SanhDieu.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoom {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long bookingId;
        @Column(name = "check_in")
        private LocalDate checkInDate;
        @Column(name = "check_out")
        private LocalDate checkOutDate;
        @Column(name = "price")
        private BigDecimal totalPrice;
        @Column(name = "Guest_FullName")
        private String guestFullName;
        @Column(name = "Guest_email")
        private String guestEmail;
        @Column(name = "Num_Of_Adults")
        private int NumOfAdults;
        @Column(name = "Num_Of_Child")
        private int NumOfChildren;
        @Column(name = "Total_num_Guest")
        private int TotalNumOfGuest;
        @Column(name = "confirm_code")
        private String bookingConfirmationCode;
        @Column(name = "date_create")
        private LocalDateTime createdDate;



        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "room_id")
        private Room room;

        public void setRoom(Room room) {
                this.room = room;
        }

        public void calculateTotalNumberOfGuest(){
                this.TotalNumOfGuest = this.NumOfAdults + NumOfChildren;
        }
        // Getters and setters

        public void setNumOfChildren(int numOfChildren) {
                NumOfChildren = numOfChildren;
                calculateTotalNumberOfGuest();
        }

        public void setNumOfAdults(int numOfAdults) {
                NumOfAdults = numOfAdults;
                calculateTotalNumberOfGuest();
        }

        public void setBookingConfirmationCode(String bookingConfirmationCode) {
                this.bookingConfirmationCode = bookingConfirmationCode;
        }

        public LocalDate getCheckInDate() {
                return checkInDate;
        }

        public void setCheckInDate(LocalDate checkInDate) {
                this.checkInDate = checkInDate;
        }

        public LocalDate getCheckOutDate() {
                return checkOutDate;
        }

        public void setCheckOutDate(LocalDate checkOutDate) {
                this.checkOutDate = checkOutDate;
        }

        public Long getBookingId() {
                return bookingId;
        }

        public void setBookingId(Long bookingId) {
                this.bookingId = bookingId;
        }

        public BigDecimal getTotalPrice() {
                return totalPrice;
        }

        public void setTotalPrice(BigDecimal totalPrice) {
                this.totalPrice = totalPrice;
        }

        public String getGuestFullName() {
                return guestFullName;
        }

        public void setGuestFullName(String guestFullName) {
                this.guestFullName = guestFullName;
        }

        public String getGuestEmail() {
                return guestEmail;
        }

        public void setGuestEmail(String guestEmail) {
                this.guestEmail = guestEmail;
        }

        public int getNumOfAdults() {
                return NumOfAdults;
        }

        public int getNumOfChildren() {
                return NumOfChildren;
        }

        public int getTotalNumOfGuest() {
                return TotalNumOfGuest;
        }

        public void setTotalNumOfGuest(int totalNumOfGuest) {
                TotalNumOfGuest = totalNumOfGuest;
        }

        public String getBookingConfirmationCode() {
                return bookingConfirmationCode;
        }

        public LocalDateTime getCreatedDate() {
                return createdDate;
        }

        public void setCreatedDate(LocalDateTime createdDate) {
                this.createdDate = createdDate;
        }

        public Room getRoom() {
                return room;
        }
}


