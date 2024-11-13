package org.example.rentcar.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.rentcar.enums.BookingStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;
    private String paymentMethod = "WALLET";
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private String bookingNo;
    private Double bill;

    public void setAppointmentNo() {
        this.bookingNo = String.valueOf(new Random().nextLong()).substring(1,11);
    }
    public double getOverAllPrice() {
        int rentDays = 0;
        if (this.startDate != null && this.endDate != null && !this.startDate.isAfter(this.endDate)) {
            rentDays = (int) ChronoUnit.DAYS.between(this.startDate, this.endDate);
        } else {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        double price = rentDays*this.car.getBasePrice();
        if(price > this.customer.getWallet()){
            throw new IllegalArgumentException("Nigga you broke.");
        }
        return price;
    }
}
