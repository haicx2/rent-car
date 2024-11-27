package org.example.rentcar.event.listener;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.rentcar.email.EmailService;
import org.example.rentcar.event.BookingApprovedEvent;
import org.example.rentcar.event.BookingCompleteEvent;
import org.example.rentcar.event.BookingDeclineEvent;
import org.example.rentcar.event.RegistrationCompleteEvent;
import org.example.rentcar.model.Booking;
import org.example.rentcar.model.User;
import org.example.rentcar.service.token.VerificationTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class NotificationEventListener implements ApplicationListener<ApplicationEvent> {
    private final EmailService emailService;
    private final VerificationTokenService verificationTokenService;
    @Value("${frontend.base.url}")
    private String frontendBaseUrl;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        Object source = event.getSource();
        if (source instanceof User) {
            RegistrationCompleteEvent registrationCompleteEvent = (RegistrationCompleteEvent) event;
            handleSendRegistrationVerificationEmail(registrationCompleteEvent);
        }

    }

    /*=================== Start user registration email verification ============================*/
    private void handleSendRegistrationVerificationEmail(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String vToken = UUID.randomUUID().toString();
        verificationTokenService.saveVerificationTokenForUser(vToken, user);
        String verificationUrl = frontendBaseUrl + "/email-verification?token=" + vToken;
        try {
            sendRegistrationVerificationEmail(user, verificationUrl);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendRegistrationVerificationEmail(User user, String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Verify Your Email";
        String senderName = "Auto Quest";
        String mailContent = "<p> Hi, " + user.getName() + ", </p>" +
                "<p>Thank you for registering with us," +
                "Please, follow the link below to complete your registration.</p>" +
                "<a href=\"" + url + "\">Verify your email</a>" +
                "<p> Thank you <br> Auto Quest Email Verification Service";
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }
    /*=================== End user registration email verification ============================*/
    /*======================== Start Approve Appointment notifications ===================================================*/

    private void handleBookingApprovedNotification(BookingApprovedEvent event) throws MessagingException, UnsupportedEncodingException {
        Booking booking = event.getBooking();
        User customer = booking.getCustomer();
        sendAppointmentApprovedNotification(customer, frontendBaseUrl);
    }

    private void sendAppointmentApprovedNotification(User user, String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Appointment Approved";
        String senderName = "Universal Pet Care Notification Service";
        String mailContent = "<p> Hi, " + user.getName() + ", </p>" +
                "<p>Your appointment has been approved:</p>" +
                "<a href=\"" + url + "\">Please, check the clinic portal to view appointment details " +
                "and veterinarian information.</a> <br/>" +
                "<p> Best Regards.<br> Universal Pet Care";
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }
    /*======================== End Approve Appointment notifications ===================================================*/


    /*======================== Start Decline Appointment notifications ===================================================*/

    private void handleAppointmentDeclinedNotification(BookingDeclineEvent event) throws MessagingException, UnsupportedEncodingException {
        Booking booking = event.getBooking();
        User customer = booking.getCustomer();
        sendAppointmentDeclinedNotification(customer, frontendBaseUrl);
    }

    private void sendAppointmentDeclinedNotification(User user, String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Appointment Not Approved";
        String senderName = "Universal Pet Care Notification Service";
        String mailContent = "<p> Hi, " + user.getName() + ", </p>" +
                "<p>We are sorry, your appointment was not approved at this time,<br/> " +
                "Please, kindly make a reschedule for another date. Thanks</p>" +
                "<a href=\"" + url + "\">Please, check the clinic portal to view appointment details.</a> <br/>" +
                "<p> Best Regards.<br> Universal Pet Care";
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }
    /*======================== End Decline Appointment notifications ===================================================*/
    /*======================== Start New Appointment booked notifications ===================================================*/

    private void handleAppointmentBookedNotification(BookingCompleteEvent event) throws MessagingException, UnsupportedEncodingException {
        Booking booking = event.getBooking();
        User customer = booking.getCustomer();
        User owner = booking.getCar().getOwner();
        sendBookingCompletedNotification(customer, frontendBaseUrl);
        sendBookingCompletedNotification(owner, frontendBaseUrl);
    }

    private void sendBookingCompletedNotification(User user, String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "New Appointment Notification";
        String senderName = "Universal Pet Care";
        String mailContent = "<p> Hi, " + user.getName() + ", </p>" +
                "<p>You have a new appointment schedule:</p>" +
                "<a href=\"" + url + "\">Please, check the clinic portal to view appointment details.</a> <br/>" +
                "<p> Best Regards.<br> Universal Pet Care Service";
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }
    /*======================== End New Appointment Booked notifications ===================================================*/
}
