package com.proyecto.fenixtech.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = { "follower", "following" })
@EqualsAndHashCode(exclude = { "follower", "following" })

@Schema(description = "Modelo de Seguidores", name = "Follows")
@Entity
@Table(name = "follows")
public class Follow implements Serializable {
    @Schema(description = "Identificador compuesto entre seguidores y seguidos", example= "1,2")
    @EmbeddedId
    private FollowsId id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("followerId")
    @JoinColumn(name = "follower_id", nullable = false)
    @JsonIgnoreProperties({ "company", "addresses", "reviews", "proposals", "orders", "cartItems", "posts", "comments", "following", "followers"})
    private Users follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("followingId")
    @JoinColumn(name = "following_id", nullable = false)
    @JsonIgnoreProperties({ "company", "addresses", "reviews", "proposals", "orders", "cartItems", "posts", "comments", "following", "followers"})
    private Users following;

    @JsonProperty("followerId")
    public void setFollowerId(Integer followerId) {
        this.follower = new Users();
        this.follower.setUserId(followerId);

        if (this.id == null) {
            this.id = new FollowsId();
        }
        this.id.setFollowerId(followerId);
    }

    @JsonProperty("followingId")
    public void setFollowingId(Integer followingId) {
        this.following = new Users();
        this.following.setUserId(followingId);

        if (this.id == null) {
            this.id = new FollowsId();
        }
        this.id.setFollowingId(followingId);
    }
    

}
