package com.fitpass.user.service;

import com.fitpass.user.dto.UserRequest;
import com.fitpass.gym.entity.Gym;
import com.fitpass.user.entity.Role;
import com.fitpass.user.entity.User;
import com.fitpass.gym.repository.GymRepository;
import com.fitpass.user.repository.RoleRepository;
import com.fitpass.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GymRepository gymRepository;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            GymRepository gymRepository) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.gymRepository = gymRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + id
                        ));
    }

    public User create(UserRequest request) {

        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Role not found with id: "
                                        + request.roleId()
                        ));

        Gym gym = null;

        if (request.gymId() != null) {
            gym = gymRepository.findById(request.gymId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Gym not found with id: "
                                            + request.gymId()
                            ));
        }

        User user = new User();

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setPhone(request.phone());
        user.setStatus(request.status());

        user.setRole(role);
        user.setGym(gym);

        return userRepository.save(user);
    }

    public User update(Long id, UserRequest request) {

        User existingUser = findById(id);

        Role role = roleRepository.findById(request.roleId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Role not found with id: "
                                        + request.roleId()
                        ));

        Gym gym = null;

        if (request.gymId() != null) {
            gym = gymRepository.findById(request.gymId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Gym not found with id: "
                                            + request.gymId()
                            ));
        }

        existingUser.setFirstName(request.firstName());
        existingUser.setLastName(request.lastName());
        existingUser.setEmail(request.email());
        existingUser.setPassword(request.password());
        existingUser.setPhone(request.phone());
        existingUser.setStatus(request.status());

        existingUser.setRole(role);
        existingUser.setGym(gym);

        return userRepository.save(existingUser);
    }

    public void delete(Long id) {

        User user = findById(id);

        userRepository.delete(user);
    }
}