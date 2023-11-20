package com.um.isa.myblog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.um.isa.myblog.IntegrationTest;
import com.um.isa.myblog.domain.Like;
import com.um.isa.myblog.repository.LikeRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LikeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LikeResourceIT {

    private static final Boolean DEFAULT_ISLIKED = false;
    private static final Boolean UPDATED_ISLIKED = true;

    private static final String ENTITY_API_URL = "/api/likes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LikeRepository likeRepository;

    @Mock
    private LikeRepository likeRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLikeMockMvc;

    private Like like;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Like createEntity(EntityManager em) {
        Like like = new Like().isliked(DEFAULT_ISLIKED);
        return like;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Like createUpdatedEntity(EntityManager em) {
        Like like = new Like().isliked(UPDATED_ISLIKED);
        return like;
    }

    @BeforeEach
    public void initTest() {
        like = createEntity(em);
    }

    @Test
    @Transactional
    void createLike() throws Exception {
        int databaseSizeBeforeCreate = likeRepository.findAll().size();
        // Create the Like
        restLikeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(like)))
            .andExpect(status().isCreated());

        // Validate the Like in the database
        List<Like> likeList = likeRepository.findAll();
        assertThat(likeList).hasSize(databaseSizeBeforeCreate + 1);
        Like testLike = likeList.get(likeList.size() - 1);
        assertThat(testLike.getIsliked()).isEqualTo(DEFAULT_ISLIKED);
    }

    @Test
    @Transactional
    void createLikeWithExistingId() throws Exception {
        // Create the Like with an existing ID
        like.setId(1L);

        int databaseSizeBeforeCreate = likeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLikeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(like)))
            .andExpect(status().isBadRequest());

        // Validate the Like in the database
        List<Like> likeList = likeRepository.findAll();
        assertThat(likeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIslikedIsRequired() throws Exception {
        int databaseSizeBeforeTest = likeRepository.findAll().size();
        // set the field null
        like.setIsliked(null);

        // Create the Like, which fails.

        restLikeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(like)))
            .andExpect(status().isBadRequest());

        List<Like> likeList = likeRepository.findAll();
        assertThat(likeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLikes() throws Exception {
        // Initialize the database
        likeRepository.saveAndFlush(like);

        // Get all the likeList
        restLikeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(like.getId().intValue())))
            .andExpect(jsonPath("$.[*].isliked").value(hasItem(DEFAULT_ISLIKED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLikesWithEagerRelationshipsIsEnabled() throws Exception {
        when(likeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLikeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(likeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLikesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(likeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLikeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(likeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLike() throws Exception {
        // Initialize the database
        likeRepository.saveAndFlush(like);

        // Get the like
        restLikeMockMvc
            .perform(get(ENTITY_API_URL_ID, like.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(like.getId().intValue()))
            .andExpect(jsonPath("$.isliked").value(DEFAULT_ISLIKED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingLike() throws Exception {
        // Get the like
        restLikeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLike() throws Exception {
        // Initialize the database
        likeRepository.saveAndFlush(like);

        int databaseSizeBeforeUpdate = likeRepository.findAll().size();

        // Update the like
        Like updatedLike = likeRepository.findById(like.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLike are not directly saved in db
        em.detach(updatedLike);
        updatedLike.isliked(UPDATED_ISLIKED);

        restLikeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLike.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLike))
            )
            .andExpect(status().isOk());

        // Validate the Like in the database
        List<Like> likeList = likeRepository.findAll();
        assertThat(likeList).hasSize(databaseSizeBeforeUpdate);
        Like testLike = likeList.get(likeList.size() - 1);
        assertThat(testLike.getIsliked()).isEqualTo(UPDATED_ISLIKED);
    }

    @Test
    @Transactional
    void putNonExistingLike() throws Exception {
        int databaseSizeBeforeUpdate = likeRepository.findAll().size();
        like.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, like.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(like))
            )
            .andExpect(status().isBadRequest());

        // Validate the Like in the database
        List<Like> likeList = likeRepository.findAll();
        assertThat(likeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLike() throws Exception {
        int databaseSizeBeforeUpdate = likeRepository.findAll().size();
        like.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(like))
            )
            .andExpect(status().isBadRequest());

        // Validate the Like in the database
        List<Like> likeList = likeRepository.findAll();
        assertThat(likeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLike() throws Exception {
        int databaseSizeBeforeUpdate = likeRepository.findAll().size();
        like.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(like)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Like in the database
        List<Like> likeList = likeRepository.findAll();
        assertThat(likeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLikeWithPatch() throws Exception {
        // Initialize the database
        likeRepository.saveAndFlush(like);

        int databaseSizeBeforeUpdate = likeRepository.findAll().size();

        // Update the like using partial update
        Like partialUpdatedLike = new Like();
        partialUpdatedLike.setId(like.getId());

        restLikeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLike.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLike))
            )
            .andExpect(status().isOk());

        // Validate the Like in the database
        List<Like> likeList = likeRepository.findAll();
        assertThat(likeList).hasSize(databaseSizeBeforeUpdate);
        Like testLike = likeList.get(likeList.size() - 1);
        assertThat(testLike.getIsliked()).isEqualTo(DEFAULT_ISLIKED);
    }

    @Test
    @Transactional
    void fullUpdateLikeWithPatch() throws Exception {
        // Initialize the database
        likeRepository.saveAndFlush(like);

        int databaseSizeBeforeUpdate = likeRepository.findAll().size();

        // Update the like using partial update
        Like partialUpdatedLike = new Like();
        partialUpdatedLike.setId(like.getId());

        partialUpdatedLike.isliked(UPDATED_ISLIKED);

        restLikeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLike.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLike))
            )
            .andExpect(status().isOk());

        // Validate the Like in the database
        List<Like> likeList = likeRepository.findAll();
        assertThat(likeList).hasSize(databaseSizeBeforeUpdate);
        Like testLike = likeList.get(likeList.size() - 1);
        assertThat(testLike.getIsliked()).isEqualTo(UPDATED_ISLIKED);
    }

    @Test
    @Transactional
    void patchNonExistingLike() throws Exception {
        int databaseSizeBeforeUpdate = likeRepository.findAll().size();
        like.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, like.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(like))
            )
            .andExpect(status().isBadRequest());

        // Validate the Like in the database
        List<Like> likeList = likeRepository.findAll();
        assertThat(likeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLike() throws Exception {
        int databaseSizeBeforeUpdate = likeRepository.findAll().size();
        like.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(like))
            )
            .andExpect(status().isBadRequest());

        // Validate the Like in the database
        List<Like> likeList = likeRepository.findAll();
        assertThat(likeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLike() throws Exception {
        int databaseSizeBeforeUpdate = likeRepository.findAll().size();
        like.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(like)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Like in the database
        List<Like> likeList = likeRepository.findAll();
        assertThat(likeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLike() throws Exception {
        // Initialize the database
        likeRepository.saveAndFlush(like);

        int databaseSizeBeforeDelete = likeRepository.findAll().size();

        // Delete the like
        restLikeMockMvc
            .perform(delete(ENTITY_API_URL_ID, like.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Like> likeList = likeRepository.findAll();
        assertThat(likeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
