package org.example.rentcar.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {
    @JsonFormat(pattern = "yyyy-MM-dd-HH-mm")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd-HH-mm")
    private LocalDate endDate;
    private String paymentMethod;
}
