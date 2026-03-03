package com.proyecto.fenixtech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.fenixtech.model.Posts;

public interface PostsRepository extends JpaRepository<Posts, Integer> {

    List<Posts> findByAuthor_UserId(Integer id);

    List<Posts> findTop5ByOrderByCreatedAtDesc();
} 