package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.repository.PostsImgRepository;
import com.proyecto.fenixtech.repository.PostsRepository;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.PostsImg;
import com.proyecto.fenixtech.model.Users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostsImgService {
    @Autowired
    private PostsImgRepository postsImgRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UsersService usersService;



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

    @Transactional
    public void deleteFromPost(Integer postId, Integer imageId) {
        PostsImg img = postsImgRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada"));

        if (!img.getPost().getPostId().equals(postId)) {
            throw new IllegalArgumentException("La imagen no pertenece al post indicado");
        }

        Users currentUser = usersService.getCurrentUser();


        if (!currentUser.getRole().name().equals("ADMIN") &&
                !img.getPost().getAuthor().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("No puedes borrar imágenes de un post que no has escrito tú");
        }

        postsImgRepository.delete(img);
    }

}
