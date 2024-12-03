package org.example.rentcar.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long nationalId;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private String phone;
    private String email;
    private String password;
    private String address;
    private String drivingLicense;
    private double wallet;
    private String role;
    private boolean isEnabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.PERSIST,
            CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id",
            referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id",
    referencedColumnName = "id"))
    private Collection<Role> roles = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<VerificationToken> verificationTokens = new ArrayList<>();
    @Transient
    List<Review> reviews = new ArrayList<>();


}
