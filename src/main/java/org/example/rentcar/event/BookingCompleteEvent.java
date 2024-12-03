package org.example.rentcar.event;

import lombok.Getter;
import lombok.Setter;
import org.example.rentcar.model.Booking;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class BookingCompleteEvent extends ApplicationEvent {
    private final Booking booking;

    public BookingCompleteEvent(Booking booking) {
        super(booking);
        this.booking = booking;
    }

}
