package org.example.rentcar.data;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.rentcar.model.Role;
import org.example.rentcar.repository.RoleRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Transactional
@RequiredArgsConstructor
public class DefaultDataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final RoleRepository roleRepository;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles =  Set.of("ROLE_CUSTOMER", "ROLE_OWNER");
        createDefaultRoleIfNotExits(defaultRoles);
    }

    private void createDefaultRoleIfNotExits(Set<String> roles){
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role:: new).forEach(roleRepository::save);
    }
}
