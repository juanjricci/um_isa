package com.um.isa.myblog.repository;

import com.um.isa.myblog.domain.Like;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Like entity.
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    default Optional<Like> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Like> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Like> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select jhiLike from Like jhiLike left join fetch jhiLike.post", countQuery = "select count(jhiLike) from Like jhiLike")
    Page<Like> findAllWithToOneRelationships(Pageable pageable);

    @Query("select jhiLike from Like jhiLike left join fetch jhiLike.post")
    List<Like> findAllWithToOneRelationships();

    @Query("select jhiLike from Like jhiLike left join fetch jhiLike.post where jhiLike.id =:id")
    Optional<Like> findOneWithToOneRelationships(@Param("id") Long id);
}
