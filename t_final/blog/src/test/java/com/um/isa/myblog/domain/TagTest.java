package com.um.isa.myblog.domain;

import static com.um.isa.myblog.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.um.isa.myblog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tag.class);
        Tag tag1 = getTagSample1();
        Tag tag2 = new Tag();
        assertThat(tag1).isNotEqualTo(tag2);

        tag2.setId(tag1.getId());
        assertThat(tag1).isEqualTo(tag2);

        tag2 = getTagSample2();
        assertThat(tag1).isNotEqualTo(tag2);
    }
}
