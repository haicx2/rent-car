package org.example.rentcar.event.listener;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.rentcar.email.EmailService;
import org.example.rentcar.event.*;
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
        switch (event.getClass().getSimpleName()) {

            case "RegistrationCompleteEvent":
                if (source instanceof User) {
                    handleSendRegistrationVerificationEmail((RegistrationCompleteEvent) event);
                }
                break;

            case "BookingApprovedEvent":
                if (source instanceof Booking) {
                    try {
                        handleBookingApprovedNotification((BookingApprovedEvent) event);
                    } catch (MessagingException | UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;

            case "BookingDeclineEvent":
                if (source instanceof Booking) {
                    try {
                        handleAppointmentDeclinedNotification((BookingDeclineEvent) event);
                    } catch (MessagingException | UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;

            case "BookingCompleteEvent":
                if (source instanceof Booking) {
                    try {
                        handleBookingCompletedNotification((BookingCompleteEvent) event);
                    } catch (MessagingException | UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;

            case "PasswordResetEvent":
                PasswordResetEvent passwordResetEvent = (PasswordResetEvent) event;
                handlePasswordResetRequest(passwordResetEvent);
                break;

            default:
                break;
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
        String senderName = "Booking Car Service";  // Changed here
        String mailContent = "<p> Hi, " + user.getName() + ", </p>" +
                "<p>Thank you for registering with us," +
                "Please, follow the link below to complete your registration.</p>" +
                "<a href=\"" + url + "\">Verify your email</a>" +
                "<p> Thank you <br> Booking Car Email Verification Service";  // Changed here
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
        String subject = "Booking Approved";  // Changed here
        String senderName = "Booking Car Notification Service";  // Changed here
        String mailContent = "<p> Hi, " + user.getName() + ", </p>" +
                "<p>Your booking has been approved:</p>" +
                "<a href=\"" + url + "\">Please, check the booking portal to view booking details " +
                "and car information.</a> <br/>" +
                "<p> Best Regards.<br> Booking Car Service";  // Changed here
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
        String subject = "Booking Not Approved";  // Changed here
        String senderName = "Booking Car Notification Service";  // Changed here
        String mailContent = "<p> Hi, " + user.getName() + ", </p>" +
                "<p>We are sorry, your booking was not approved at this time,<br/> " +
                "Please, kindly make a new booking for another date. Thanks</p>" +
                "<a href=\"" + url + "\">Please, check the booking portal to view booking details.</a> <br/>" +
                "<p> Best Regards.<br> Booking Car Service";  // Changed here
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }
    /*======================== End Decline Appointment notifications ===================================================*/
    /*======================== Start New Booking notifications ===================================================*/

    private void handleBookingCompletedNotification(BookingCompleteEvent event) throws MessagingException, UnsupportedEncodingException {
        Booking booking = event.getBooking();
        User customer = booking.getCustomer();
        User owner = booking.getCar().getOwner();
        sendBookingCompletedNotification(customer, frontendBaseUrl);
        sendBookingCompletedNotification(owner, frontendBaseUrl);
    }

    private void sendBookingCompletedNotification(User user, String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "New Booking Notification";  // Changed here
        String senderName = "Booking Car Service";  // Changed here
        String mailContent = "<p> Hi, " + user.getName() + ", </p>" +
                "<p>You have a new booking:</p>" +
                "<a href=\"" + url + "\">Please, check the booking portal to view booking details.</a> <br/>" +
                "<p> Best Regards.<br> Booking Car Service";  // Changed here
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }
    /*======================== End New Booking notifications ===================================================*/

    /*======================== Start password reset related notifications ===================================================*/

    private void handlePasswordResetRequest(PasswordResetEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        verificationTokenService.saveVerificationTokenForUser(token, user);
        String resetUrl = frontendBaseUrl + "/reset-password?token=" + token;
        try {
            sendPasswordResetEmail(user, resetUrl);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    private void sendPasswordResetEmail(User user, String resetUrl) throws MessagingException, UnsupportedEncodingException {
        String subject = "Password Reset Request";
        String senderName = "Universal Pet Care";
        String mailContent = "<p>Hi, " + user.getName() + ",</p>" +
                "<p>You have requested to reset your password. Please click the link below to proceed:</p>" +
                "<a href=\"" + resetUrl + "\">Reset Password</a><br/>" +
                "<p>If you did not request this, please ignore this email.</p>" +
                "<p>Best Regards.<br> Universal Pet Care</p>";
        emailService.sendEmail(user.getEmail(), subject, senderName, mailContent);
    }
    /*======================== End password reset related notifications ===================================================*/
}


