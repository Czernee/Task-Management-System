<<<<<<< HEAD
package com.example.task_management_system.repository;public interface CommentRepository {
=======
package com.example.task_management_system.repository;

import com.example.task_management_system.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
>>>>>>> ae658b6423c024ac97b8400fa519d537ebb0be1b
}
