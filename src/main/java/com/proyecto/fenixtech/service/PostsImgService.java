package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.PostsImgRepository;
import com.proyecto.fenixtech.repository.PostsRepository;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.PostsImg;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostsImgService {
    @Autowired
    private PostsImgRepository postsImgRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Transactional(readOnly = true)
    public List<PostsImg> findAllPostsImg() {
        return postsImgRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PostsImg findById(Integer id) {
        return postsImgRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen de post no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<PostsImg> findByPostId(Integer postId) {
        postsRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id: " + postId));

        return postsImgRepository.findByPost_PostId(postId);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return postsImgRepository.count();
    }

    @Transactional
    public PostsImg save(PostsImg postsImg) {
        if (postsImg.getPost() == null || postsImg.getPost().getPostId() == null) {
            throw new IllegalArgumentException("La imagen debe estar asociada a un post válido con ID.");
        }

        postsRepository.findById(postsImg.getPost().getPostId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El post con ID " + postsImg.getPost().getPostId() + " no existe"));

        return postsImgRepository.save(postsImg);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!postsImgRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe la imagen con id: " + id + " para eliminar");
        }
        postsImgRepository.deleteById(id);
    }

    @Transactional
    public PostsImg update(Integer id, PostsImg postsImg) {
        if (postsImg.getPost() == null || postsImg.getPost().getPostId() == null) {
            throw new IllegalArgumentException("La imagen debe estar asociada a un post válido con ID.");
        }

        PostsImg existingImg = postsImgRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la imagen con ID: " + id));

        existingImg.setImageUrl(postsImg.getImageUrl());

        return postsImgRepository.save(existingImg);
    }
}
