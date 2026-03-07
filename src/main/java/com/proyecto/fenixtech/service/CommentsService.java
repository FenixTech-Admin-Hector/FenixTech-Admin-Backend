package com.proyecto.fenixtech.service;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Comments;
import com.proyecto.fenixtech.repository.CommentsRepository;
import com.proyecto.fenixtech.repository.PostsRepository;
import com.proyecto.fenixtech.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Comments save(Comments comment) {
        usersRepository.findById(comment.getAuthor().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + comment.getAuthor().getUserId()));
        postsRepository.findById(comment.getPost().getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id: " + comment.getPost().getPostId()));
        return commentsRepository.save(comment);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!commentsRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comentario no encontrado con id: " + id);
        }
        commentsRepository.deleteById(id);
    }

    @Transactional
    public Comments update(Integer id, Comments comment) {
        Comments existingComment = commentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con id: " + id));
        
        existingComment.setBody(comment.getBody());

        return commentsRepository.save(existingComment);
    }
}
