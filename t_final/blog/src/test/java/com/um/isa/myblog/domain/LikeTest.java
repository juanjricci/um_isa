package com.um.isa.myblog.domain;

import static com.um.isa.myblog.domain.LikeTestSamples.*;
import static com.um.isa.myblog.domain.PostTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.um.isa.myblog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LikeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Like.class);
        Like like1 = getLikeSample1();
        Like like2 = new Like();
        assertThat(like1).isNotEqualTo(like2);

        like2.setId(like1.getId());
        assertThat(like1).isEqualTo(like2);

        like2 = getLikeSample2();
        assertThat(like1).isNotEqualTo(like2);
    }

    @Test
    void postTest() throws Exception {
        Like like = getLikeRandomSampleGenerator();
        Post postBack = getPostRandomSampleGenerator();

        like.setPost(postBack);
        assertThat(like.getPost()).isEqualTo(postBack);

        like.post(null);
        assertThat(like.getPost()).isNull();
    }
}
