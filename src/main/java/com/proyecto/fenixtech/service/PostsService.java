package com.proyecto.fenixtech.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.fenixtech.dto.PostsRequestDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Posts;
import com.proyecto.fenixtech.model.PostsImg;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.repository.PostsRepository;
import com.proyecto.fenixtech.repository.UsersRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class PostsService {
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public Page<Posts> findAllPosts(Pageable pageable) {
        return postsRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Posts findById(Integer id) {
        return postsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Posts> findByUserId(Integer id) {
        usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return postsRepository.findByAuthor_UserId(id);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return postsRepository.count();
    }

    @Transactional
    public Posts save(PostsRequestDTO dto) {
        Users user = usersRepository.findByUserIdAndIsActiveTrue(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Posts post = new Posts();
        post.setTitle(dto.getTitle());
        post.setBody(dto.getBody());
        post.setAuthor(user);

        if (dto.getImagesUrls() != null) {
            for (String url : dto.getImagesUrls()) {
                PostsImg img = new PostsImg();
                img.setImageUrl(url);
                img.setPost(post);
                post.getPostImages().add(img);
            }
        }
        return postsRepository.save(post);
    }

    @Transactional
    public void deleteById(Integer id) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado"));

        if (!post.getAuthor().getEmail().equals(email)) {
            throw new AccessDeniedException("No tienes permiso: este post no te pertenece");
        }

        postsRepository.delete(post);
    }

    @Transactional
    public Posts update(Integer id, PostsRequestDTO dto) {
        String emailFromToken = SecurityContextHolder.getContext().getAuthentication().getName();

        Posts postUpdate = postsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id: " + id));

        if (!postUpdate.getAuthor().getEmail().equals(emailFromToken)) {
            throw new AccessDeniedException("No tienes permiso para editar este post. Solo el autor puede hacerlo.");
        }

        postUpdate.setTitle(dto.getTitle());
        postUpdate.setBody(dto.getBody());

        if (dto.getImagesUrls() != null) {
            postUpdate.getPostImages().clear();

            for (String url : dto.getImagesUrls()) {
                PostsImg img = new PostsImg();
                img.setImageUrl(url);
                img.setPost(postUpdate);
                postUpdate.getPostImages().add(img);
            }
        }

        // 6. Guardamos los cambios
        return postsRepository.save(postUpdate);
    }

}
