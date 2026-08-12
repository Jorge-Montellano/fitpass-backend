package com.fitpass.gym.repository;
import com.fitpass.gym.entity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymRepository extends JpaRepository<Gym, Long>{
}
