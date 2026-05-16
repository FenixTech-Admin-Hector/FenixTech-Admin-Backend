package com.proyecto.fenixtech.admin.community;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.fenixtech.model.Comments;

public interface AdminCommentsRepository extends JpaRepository<Comments, Integer> {
    Page<Comments> findByPost_PostId(Integer postId, Pageable pageable);
}