package org.example.rentcar.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.rentcar.enums.BookingStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
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
    @JsonFormat(pattern = "yyyy-MM-dd-HH-mm")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd-HH-mm")
    private LocalDateTime endDate;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;
    private String paymentMethod;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private String bookingNo;

    public void setAppointmentNo() {
        this.bookingNo = String.valueOf(new Random().nextLong()).substring(1,11);
    }
}
