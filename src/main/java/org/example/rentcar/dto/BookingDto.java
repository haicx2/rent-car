package org.example.rentcar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.rentcar.enums.BookingStatus;


import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BookingDto {
    private long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String customerName;
    private long customerId;
    private long carId;
    private String carName;
    private String paymentMethod = "WALLET";
    private BookingStatus status;
    private String bookingNo;
    private Double bill;
}
