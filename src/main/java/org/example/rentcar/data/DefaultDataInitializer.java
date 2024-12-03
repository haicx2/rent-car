package org.example.rentcar.data;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.rentcar.model.CarOwner;
import org.example.rentcar.model.Customer;
import org.example.rentcar.model.Role;
import org.example.rentcar.model.User;
import org.example.rentcar.repository.RoleRepository;
import org.example.rentcar.repository.UserRepository;
import org.example.rentcar.service.role.RoleService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@Transactional
@RequiredArgsConstructor
public class DefaultDataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles =  Set.of("ROLE_CUSTOMER", "ROLE_OWNER");
        createDefaultRoleIfNotExits(defaultRoles);
        createDefaultUsersIfNotExists();
    }

    private void createDefaultRoleIfNotExits(Set<String> roles){
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role:: new).forEach(roleRepository::save);
    }

    private void createDefaultUsersIfNotExists() {
        createDefaultUsers("OWNER", CarOwner.class, "Business Owner", "1234 Business St", 1000.00);
        createDefaultUsers("CUSTOMER", Customer.class, "Regular Customer", "5678 Main St", 500000.00);
    }

    private <T extends User> void createDefaultUsers(String roleName, Class<T> userClass, String namePrefix, String defaultAddress, double defaultWallet) {
        Role userRole = roleService.getRoleByName("ROLE_" + roleName);
        for (int i = 1; i <= 10; i++) {
            String defaultEmail = roleName.toLowerCase() + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }

            T user;
            try {
                user = userClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error instantiating user class: " + userClass.getSimpleName(), e);
            }

            user.setName(namePrefix + " " + i);
            user.setPhone("1234567890");
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("password" + i));
            user.setRole(roleName);
            user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
            user.setAddress(defaultAddress);
            user.setWallet(defaultWallet);
            user.setBirthday(LocalDate.of(2000, 1, 1));
            user.setEnabled(true);

            userRepository.save(user);
            System.out.println("Default " + roleName.toLowerCase() + " user " + i + " created successfully.");
        }
    }
}
