package com.um.isa.myblog.domain;

import static com.um.isa.myblog.domain.BlogTestSamples.getBlogSample1;
import static com.um.isa.myblog.domain.BlogTestSamples.getBlogSample2;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.um.isa.myblog.web.rest.TestUtil;


class BlogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Blog.class);
        Blog blog1 = getBlogSample1();
        Blog blog2 = new Blog();
        assertThat(blog1).isNotEqualTo(blog2);

        blog2.setId(blog1.getId());
        assertThat(blog1).isEqualTo(blog2);

        blog2 = getBlogSample2();
        assertThat(blog1).isNotEqualTo(blog2);
    }
    
}
