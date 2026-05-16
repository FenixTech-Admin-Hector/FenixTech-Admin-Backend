package com.proyecto.fenixtech.admin.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.fenixtech.exception.ResourceNotFoundException;
import com.proyecto.fenixtech.model.Comments;
import com.proyecto.fenixtech.model.Posts;

@Service
public class AdminCommunityService {

    @Autowired
    private AdminPostsRepository adminPostsRepository;

    @Autowired
    private AdminCommentsRepository adminCommentsRepository;

    // --- LÓGICA DE POSTS ---

    @Transactional(readOnly = true)
    public List<Posts> findAllPosts() {
        return adminPostsRepository.findAll();
    }

    @Transactional
    public void deletePost(Integer id) {
        Posts post = adminPostsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id: " + id));
        
        // El borrado en cascada (imágenes y comentarios) se gestiona automáticamente por Hibernate
        // si la relación @OneToMany en la entidad Posts tiene cascade = CascadeType.ALL.
        adminPostsRepository.delete(post);
    }

    // --- LÓGICA DE COMENTARIOS ---

    @Transactional(readOnly = true)
    public Page<Comments> findCommentsByPostId(Integer postId, Pageable pageable) {
        // Aseguramos que el post existe antes de buscar sus comentarios
        adminPostsRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con id: " + postId));
        
        return adminCommentsRepository.findByPost_PostId(postId, pageable);
    }

    @Transactional
    public void deleteComment(Integer id) {
        Comments comment = adminCommentsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con id: " + id));
        
        adminCommentsRepository.delete(comment);
    }
}