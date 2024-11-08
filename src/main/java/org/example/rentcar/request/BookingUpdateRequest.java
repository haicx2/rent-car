package org.example.rentcar.request;

import lombok.Data;
import org.example.rentcar.enums.BookingStatus;

@Data
public class BookingUpdateRequest {
    private BookingStatus status;
}
