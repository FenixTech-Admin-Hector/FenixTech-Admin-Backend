package com.proyecto.fenixtech.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.dto.PostsRequestDTO;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Posts;
import com.proyecto.fenixtech.model.PostsImg;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.model.enums.Rol;
import com.proyecto.fenixtech.repository.PostsRepository;
import com.proyecto.fenixtech.repository.UsersRepository;

@Service
public class PostsService {
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersService usersService;

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
        Users currentUser = usersService.getCurrentUser();
        
        if (!currentUser.getRole().equals(Rol.ADMIN) && !currentUser.getUserId().equals(id)) {
            throw new AccessDeniedException("No tienes permiso para ver los posts de otro usuario");
        }

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
        Users user = usersService.getCurrentUser();
        
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
        Users currentUser = usersService.getCurrentUser();

        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado"));

        if (!post.getAuthor().getUserId().equals(currentUser.getUserId()) && 
            !currentUser.getRole().equals(Rol.ADMIN)) {
            throw new AccessDeniedException("No tienes permiso para eliminar este post");
        }

        postsRepository.delete(post);
    }

    @Transactional
    public Posts update(Integer id, PostsRequestDTO dto) {
        Users currentUser = usersService.getCurrentUser();

        Posts postUpdate = postsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id: " + id));

        if (!postUpdate.getAuthor().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("Solo el autor puede editar este post");
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

        return postsRepository.save(postUpdate);
    }
}