package com.um.isa.myblog.web.rest;

import com.um.isa.myblog.domain.Like;
import com.um.isa.myblog.repository.LikeRepository;
import com.um.isa.myblog.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.um.isa.myblog.domain.Like}.
 */
@RestController
@RequestMapping("/api/likes")
@Transactional
public class LikeResource {

    private final Logger log = LoggerFactory.getLogger(LikeResource.class);

    private static final String ENTITY_NAME = "like";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LikeRepository likeRepository;

    public LikeResource(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    /**
     * {@code POST  /likes} : Create a new like.
     *
     * @param like the like to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new like, or with status {@code 400 (Bad Request)} if the like has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Like> createLike(@Valid @RequestBody Like like) throws URISyntaxException {
        log.debug("REST request to save Like : {}", like);
        if (like.getId() != null) {
            throw new BadRequestAlertException("A new like cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Like result = likeRepository.save(like);
        return ResponseEntity
            .created(new URI("/api/likes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /likes/:id} : Updates an existing like.
     *
     * @param id the id of the like to save.
     * @param like the like to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated like,
     * or with status {@code 400 (Bad Request)} if the like is not valid,
     * or with status {@code 500 (Internal Server Error)} if the like couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Like> updateLike(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Like like)
        throws URISyntaxException {
        log.debug("REST request to update Like : {}, {}", id, like);
        if (like.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, like.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Like result = likeRepository.save(like);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, like.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /likes/:id} : Partial updates given fields of an existing like, field will ignore if it is null
     *
     * @param id the id of the like to save.
     * @param like the like to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated like,
     * or with status {@code 400 (Bad Request)} if the like is not valid,
     * or with status {@code 404 (Not Found)} if the like is not found,
     * or with status {@code 500 (Internal Server Error)} if the like couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Like> partialUpdateLike(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Like like
    ) throws URISyntaxException {
        log.debug("REST request to partial update Like partially : {}, {}", id, like);
        if (like.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, like.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Like> result = likeRepository
            .findById(like.getId())
            .map(existingLike -> {
                if (like.getIsliked() != null) {
                    existingLike.setIsliked(like.getIsliked());
                }

                return existingLike;
            })
            .map(likeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, like.getId().toString())
        );
    }

    /**
     * {@code GET  /likes} : get all the likes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of likes in body.
     */
    @GetMapping("")
    public List<Like> getAllLikes(@RequestParam(required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Likes");
        if (eagerload) {
            return likeRepository.findAllWithEagerRelationships();
        } else {
            return likeRepository.findAll();
        }
    }

    /**
     * {@code GET  /likes/:id} : get the "id" like.
     *
     * @param id the id of the like to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the like, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Like> getLike(@PathVariable Long id) {
        log.debug("REST request to get Like : {}", id);
        Optional<Like> like = likeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(like);
    }

    /**
     * {@code DELETE  /likes/:id} : delete the "id" like.
     *
     * @param id the id of the like to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long id) {
        log.debug("REST request to delete Like : {}", id);
        likeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
