package com.fitpass.service;

import com.fitpass.dto.checkin.CheckInRequest;
import com.fitpass.entity.CheckIn;
import com.fitpass.entity.Gym;
import com.fitpass.entity.User;
import com.fitpass.repository.CheckInRepository;
import com.fitpass.repository.GymRepository;
import com.fitpass.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final UserRepository userRepository;
    private final GymRepository gymRepository;

    public CheckInService(
            CheckInRepository checkInRepository,
            UserRepository userRepository,
            GymRepository gymRepository) {

        this.checkInRepository = checkInRepository;
        this.userRepository = userRepository;
        this.gymRepository = gymRepository;
    }

    public List<CheckIn> findAll() {
        return checkInRepository.findAll();
    }

    public CheckIn findById(Long id) {
        return checkInRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "CheckIn not found with id: " + id
                        ));
    }

    public CheckIn create(CheckInRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: "
                                        + request.userId()
                        ));

        Gym gym = gymRepository.findById(request.gymId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Gym not found with id: "
                                        + request.gymId()
                        ));

        CheckIn checkIn = new CheckIn();

        checkIn.setUser(user);
        checkIn.setGym(gym);
        checkIn.setCheckInAt(request.checkInAt());
        checkIn.setCheckOutAt(request.checkOutAt());

        return checkInRepository.save(checkIn);
    }

    public CheckIn update(
            Long id,
            CheckInRequest request) {

        CheckIn existingCheckIn = findById(id);

        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: "
                                        + request.userId()
                        ));

        Gym gym = gymRepository.findById(request.gymId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Gym not found with id: "
                                        + request.gymId()
                        ));

        existingCheckIn.setUser(user);
        existingCheckIn.setGym(gym);
        existingCheckIn.setCheckInAt(request.checkInAt());
        existingCheckIn.setCheckOutAt(request.checkOutAt());

        return checkInRepository.save(existingCheckIn);
    }

    public void delete(Long id) {

        CheckIn checkIn = findById(id);

        checkInRepository.delete(checkIn);
    }

}