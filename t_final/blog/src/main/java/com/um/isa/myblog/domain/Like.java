package com.um.isa.myblog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Like.
 */
@Entity
@Table(name = "jhi_like")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Like implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "isliked", nullable = false)
    private Boolean isliked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "blog", "tag" }, allowSetters = true)
    private Post post;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Like id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsliked() {
        return this.isliked;
    }

    public Like isliked(Boolean isliked) {
        this.setIsliked(isliked);
        return this;
    }

    public void setIsliked(Boolean isliked) {
        this.isliked = isliked;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Like post(Post post) {
        this.setPost(post);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Like)) {
            return false;
        }
        return getId() != null && getId().equals(((Like) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Like{" +
            "id=" + getId() +
            ", isliked='" + getIsliked() + "'" +
            "}";
    }
}
