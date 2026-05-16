package com.proyecto.fenixtech.admin.community;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.fenixtech.model.Posts;

public interface AdminPostsRepository extends JpaRepository<Posts, Integer> {
}