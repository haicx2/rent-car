package org.example.rentcar.event;

import lombok.Getter;
import lombok.Setter;
import org.example.rentcar.model.Booking;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class BookingApprovedEvent extends ApplicationEvent {
    private final Booking booking;

    public BookingApprovedEvent(Booking booking) {
        super(booking);
        this.booking = booking;
    }

}
