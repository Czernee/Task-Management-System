<<<<<<< HEAD
package com.example.task_management_system.repository;public interface RoleRepository {
=======
package com.example.task_management_system.repository;

import com.example.task_management_system.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
>>>>>>> ae658b6423c024ac97b8400fa519d537ebb0be1b
}
