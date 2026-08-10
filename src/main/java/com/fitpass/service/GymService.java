package com.fitpass.service;

import com.fitpass.entity.Gym;
import com.fitpass.repository.GymRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GymService {

    private final GymRepository gymRepository;

    public GymService(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }

    public List<Gym> findAll() {
        return gymRepository.findAll();
    }

    public Gym findById(Long id) {
        return gymRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Gym not found with id: " + id));
    }

    public Gym save(Gym gym) {
        return gymRepository.save(gym);
    }

    public Gym update(Long id, Gym gym) {

        Gym existingGym = findById(id);

        existingGym.setName(gym.getName());
        existingGym.setDescription(gym.getDescription());
        existingGym.setAddress(gym.getAddress());
        existingGym.setCity(gym.getCity());
        existingGym.setPhone(gym.getPhone());
        existingGym.setEmail(gym.getEmail());
        existingGym.setOpeningTime(gym.getOpeningTime());
        existingGym.setClosingTime(gym.getClosingTime());
        existingGym.setStatus(gym.getStatus());

        return gymRepository.save(existingGym);
    }

    public void delete(Long id) {
        Gym gym = findById(id);
        gymRepository.delete(gym);
    }
}