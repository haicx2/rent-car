package org.example.rentcar.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {
    private String startDate;
    private String endDate;
    private String paymentMethod;
}
