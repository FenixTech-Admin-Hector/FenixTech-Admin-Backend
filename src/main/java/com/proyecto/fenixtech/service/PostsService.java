package com.proyecto.fenixtech.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.fenixtech.dto.PostsDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Posts;
import com.proyecto.fenixtech.model.PostsImg;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.model.enums.Rol;
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
    public List<Posts> findRecentPosts() {
        return postsRepository.findTop5ByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public Long count() {
        return postsRepository.count();
    }

    @Transactional
    public Posts save(PostsDTO dto) {
        Users user = usersRepository.findByUserIdAndIsActiveTrueAndRoleNot(dto.getUserId(), Rol.ADMIN)
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
        if (!postsRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe el post con id: " + id + " para eliminar");
        }
        postsRepository.deleteById(id);
    }

    @Transactional
    public Posts update(Integer id, PostsDTO dto) {
        Posts postUpdate = postsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id: " + id));

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

        return postsRepository.save(postUpdate);
    }

}
