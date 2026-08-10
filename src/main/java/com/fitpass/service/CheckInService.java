package com.fitpass.service;

import com.fitpass.entity.CheckIn;
import com.fitpass.repository.CheckInRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckInService {

    private final CheckInRepository checkInRepository;

    public CheckInService(CheckInRepository checkInRepository) {
        this.checkInRepository = checkInRepository;
    }

    public List<CheckIn> findAll() {
        return checkInRepository.findAll();
    }

    public CheckIn findById(Long id) {
        return checkInRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("CheckIn not found with id: " + id));
    }

    public CheckIn save(CheckIn checkIn) {
        return checkInRepository.save(checkIn);
    }

    public CheckIn update(Long id, CheckIn checkIn) {

        CheckIn existingCheckIn = findById(id);

        existingCheckIn.setUser(checkIn.getUser());
        existingCheckIn.setGym(checkIn.getGym());
        existingCheckIn.setCheckInAt(checkIn.getCheckInAt());
        existingCheckIn.setCheckOutAt(checkIn.getCheckOutAt());

        return checkInRepository.save(existingCheckIn);
    }

    public void delete(Long id) {
        CheckIn checkIn = findById(id);
        checkInRepository.delete(checkIn);
    }
}