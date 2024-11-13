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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private CustomerDto customer;
    private CarDto car;
    private String paymentMethod = "WALLET";
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private String bookingNo;
    private Double bill;
}
