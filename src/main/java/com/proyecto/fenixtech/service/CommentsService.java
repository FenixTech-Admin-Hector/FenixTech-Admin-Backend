package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.dto.CommentsRequest;
import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Comments;
import com.proyecto.fenixtech.model.Posts;
import com.proyecto.fenixtech.model.Users;
import com.proyecto.fenixtech.repository.CommentsRepository;
import com.proyecto.fenixtech.repository.PostsRepository;
import com.proyecto.fenixtech.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersService usersService;


    @Transactional(readOnly = true)
    public Page<Comments> findCommentsByPostId(Integer postId, Pageable pageable) {
        postsRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id: " + postId));
        return commentsRepository.findByPost_PostId(postId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Comments> findCommentsByUserId(Integer userId) {
        usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + userId));
        return commentsRepository.findByAuthor_UserId(userId);
    }

    @Transactional(readOnly = true)
    public Long countAllComments() {
        return commentsRepository.count();
    }

    @Transactional
    public Comments save(CommentsRequest dto) {

        Users currentUser = usersService.getCurrentUser();

        Posts post = postsRepository.findById(dto.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("El post no existe"));

        Comments comment = new Comments();
        comment.setBody(dto.getBody());
        comment.setPost(post);
        comment.setAuthor(currentUser);

        return commentsRepository.save(comment);
    }

    @Transactional
    public void deleteByUser(Integer id, Integer userId) {
        Comments comment = commentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado"));

        Users currentUser = usersService.getCurrentUser();


        if (!currentUser.getRole().name().equals("ADMIN") &&
                !comment.getAuthor().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("No puedes borrar un comentario ajeno");
        }
        
        commentsRepository.delete(comment);
    }

}
