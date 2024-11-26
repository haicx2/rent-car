package org.example.rentcar.utils;

public class UrlMapping {
    public static final String API = "/api/v1";
    public static final String USER = API + "/user";
    public static final String CAR = API + "/car";
    public static final String REVIEW = API + "/review";
    public static final String GET_BY_ID = "/get/{id}";
    public static final String GET_ALL = "/get/all";
    public static final String GET_BY_EMAIL = "/get/email/{email}";
    public static final String UPDATE_BY_ID = "/update/{id}";
    public static final String DELETE_BY_ID = "/delete/{id}";
    public static final String REGISTER = "/register";
    public static final String ADD = "/add";
    public static final String GET_BY_OWNER_ID = "/owner/get/{ownerId}";
    public static final String UPDATE_IMAGE_BY_ID = "/update/image/{carId}";
    public static final String BOOKING = API + "/booking";
    public static final String GET_BY_CUSTOMER_ID = "/get/{customerId}";
    public static final String GET_BY_CAR_ID = "/get/{carId}";
    public static final String CHANGE_PASSWORD = "/{userId}/change-password" ;
    public static final String BOOKING_APPROVED = "/{bookingId}/approve";
    public static final String BOOKING_CANCELED = "/{bookingId}/cancel";
    public static final String BOOKING_COMPLETED = "/{bookingId}/complete";
    public static final String BOOKING_REJECTED = "/{bookingId}/reject";
    public static final String LOGIN = "/login";
    public static final String AUTH = API + "/auth";
}
