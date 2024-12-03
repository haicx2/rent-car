package org.example.rentcar.event;

import lombok.Getter;
import lombok.Setter;
import org.example.rentcar.model.User;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private User user;
    public RegistrationCompleteEvent(User user) {
        super(user);
        this.user = user;
    }
}
