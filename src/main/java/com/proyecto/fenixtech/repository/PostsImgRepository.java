package com.proyecto.fenixtech.repository;
import com.proyecto.fenixtech.model.PostsImg;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsImgRepository extends JpaRepository<PostsImg, Integer>{
    List<PostsImg> findByPost_PostId(Integer postId);
}
