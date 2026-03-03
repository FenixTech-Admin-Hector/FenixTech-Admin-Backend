package com.proyecto.fenixtech.repository;

import com.proyecto.fenixtech.model.Comments;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Integer> {

    Page<Comments> findByPost_PostId(Integer postId, Pageable pageable);

    List<Comments> findByAuthor_UserId(Integer userId);
    
}
