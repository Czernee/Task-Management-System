<<<<<<< HEAD
package com.example.task_management_system.repository;public class TaskRepository {
=======
package com.example.task_management_system.repository;

import com.example.task_management_system.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskRepository extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {
>>>>>>> ae658b6423c024ac97b8400fa519d537ebb0be1b
}
