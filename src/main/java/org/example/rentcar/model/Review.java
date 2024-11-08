package org.example.rentcar.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double rating;
    private String comment;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd-HH-mm")
    private LocalDateTime created;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private Car car;
}
