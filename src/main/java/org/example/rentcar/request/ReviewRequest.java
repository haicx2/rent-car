package org.example.rentcar.request;

import lombok.Data;

@Data
public class ReviewRequest {
    private double rating;
    private String comment;
}
