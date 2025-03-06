<<<<<<< HEAD
package com.example.task_management_system.repository;public interface UserRepository {
=======
package com.example.task_management_system.repository;

import com.example.task_management_system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
>>>>>>> ae658b6423c024ac97b8400fa519d537ebb0be1b
}
