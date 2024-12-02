package org.example.rentcar.service.role;

import lombok.RequiredArgsConstructor;
import org.example.rentcar.exception.ResourceNotFoundException;
import org.example.rentcar.model.Role;
import org.example.rentcar.repository.RoleRepository;
import org.example.rentcar.utils.FeedBackMessage;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName).orElse(null);
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);

    }

    @Override
    public Set<Role> setUserRole(String role) {
        Set<Role> userRoles = new HashSet<>();
        roleRepository.findByName("ROLE_" + role)
                .ifPresentOrElse(userRoles::add, () -> {
                    throw new ResourceNotFoundException(FeedBackMessage.ROLE_NOT_FOUND);
                });
        return userRoles;
    }
}
