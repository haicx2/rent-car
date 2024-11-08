package org.example.rentcar.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequest {
    @JsonFormat(pattern = "yyyy-MM-dd-HH-mm")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd-HH-mm")
    private LocalDateTime endDate;
    private String paymentMethod;
}
