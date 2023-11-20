package com.um.isa.myblog.domain;

import static com.um.isa.myblog.domain.BlogTestSamples.*;
import static com.um.isa.myblog.domain.PostTestSamples.*;
import static com.um.isa.myblog.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.um.isa.myblog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Post.class);
        Post post1 = getPostSample1();
        Post post2 = new Post();
        assertThat(post1).isNotEqualTo(post2);

        post2.setId(post1.getId());
        assertThat(post1).isEqualTo(post2);

        post2 = getPostSample2();
        assertThat(post1).isNotEqualTo(post2);
    }

    @Test
    void blogTest() throws Exception {
        Post post = getPostRandomSampleGenerator();
        Blog blogBack = getBlogRandomSampleGenerator();

        post.setBlog(blogBack);
        assertThat(post.getBlog()).isEqualTo(blogBack);

        post.blog(null);
        assertThat(post.getBlog()).isNull();
    }

    @Test
    void tagTest() throws Exception {
        Post post = getPostRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        post.setTag(tagBack);
        assertThat(post.getTag()).isEqualTo(tagBack);

        post.tag(null);
        assertThat(post.getTag()).isNull();
    }
}
