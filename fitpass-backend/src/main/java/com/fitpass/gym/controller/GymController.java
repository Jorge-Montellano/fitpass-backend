package com.fitpass.gym.controller;

import com.fitpass.gym.dto.GymRequest;
import com.fitpass.gym.dto.GymResponse;
import com.fitpass.gym.entity.Gym;
import com.fitpass.gym.service.GymService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/gyms")
public class GymController {

    private final GymService gymService;

    public GymController(GymService gymService) {
        this.gymService = gymService;
    }

    @GetMapping
    public List<GymResponse> findAll() {

        return gymService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public GymResponse findById(@PathVariable Long id) {

        return toResponse(gymService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GymResponse create(
            @Valid @RequestBody GymRequest request) {

        Gym gym = new Gym();

        gym.setName(request.name());
        gym.setDescription(request.description());
        gym.setAddress(request.address());
        gym.setCity(request.city());
        gym.setPhone(request.phone());
        gym.setEmail(request.email());
        gym.setOpeningTime(
                request.openingTime() != null
                        ? LocalTime.parse(request.openingTime())
                        : null
        );

        gym.setClosingTime(
                request.closingTime() != null
                        ? LocalTime.parse(request.closingTime())
                        : null
        );
        gym.setStatus(request.status());

        return toResponse(gymService.save(gym));
    }

    @PutMapping("/{id}")
    public GymResponse update(
            @PathVariable Long id,
            @Valid @RequestBody GymRequest request) {

        Gym gym = new Gym();

        gym.setName(request.name());
        gym.setDescription(request.description());
        gym.setAddress(request.address());
        gym.setCity(request.city());
        gym.setPhone(request.phone());
        gym.setEmail(request.email());
        gym.setOpeningTime(
                request.openingTime() != null
                        ? LocalTime.parse(request.openingTime())
                        : null
        );

        gym.setClosingTime(
                request.closingTime() != null
                        ? LocalTime.parse(request.closingTime())
                        : null
        );
        gym.setStatus(request.status());

        return toResponse(gymService.update(id, gym));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        gymService.delete(id);
    }

    private GymResponse toResponse(Gym gym) {

        return new GymResponse(
                gym.getId(),
                gym.getName(),
                gym.getDescription(),
                gym.getAddress(),
                gym.getCity(),
                gym.getPhone(),
                gym.getEmail(),

                gym.getOpeningTime() != null
                        ? gym.getOpeningTime().toString()
                        : null,

                gym.getClosingTime() != null
                        ? gym.getClosingTime().toString()
                        : null,

                gym.getStatus()
        );
    }
}