package com.fitpass.repository;
import com.fitpass.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>{
    @Override
    @EntityGraph(attributePaths = {"role", "gym"})
    List<User> findAll();

    @Override
    @EntityGraph(attributePaths = {"role", "gym"})
    Optional<User> findById(Long id);

}
