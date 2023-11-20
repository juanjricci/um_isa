package com.um.isa.myblog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolationException;

public class CustomBlogTests {

    // Test the constructor
    @Test
    void testConstructor() {
        // Create a new blog object
        Blog blog = new Blog();

        // Assert that the blog object has the expected properties
        assertThat(blog.getId()).isNull();
        assertThat(blog.getName()).isNull();
        assertThat(blog.getHandle()).isNull();
    }

    // test that setters and getters are working correctly
    @Test
    void testSettersAndGetters() {
        // Create a new blog object
        Blog blog = new Blog();

        // Set the blog properties
        blog.setId(1L);
        blog.setName("My Blog Post");
        blog.setHandle("my-blog-handle");

        // Assert that the blog properties were set correctly
        assertThat(blog.getId()).isEqualTo(1L);
        assertThat(blog.getName()).isEqualTo("My Blog Post");
        assertThat(blog.getHandle()).isEqualTo("my-blog-handle");

        // Get the blog properties
        Long id = blog.getId();
        String name = blog.getName();
        String handle = blog.getHandle();

        // Assert that the blog properties were retrieved correctly
        assertThat(id).isEqualTo(1L);
        assertThat(name).isEqualTo("My Blog Post");
        assertThat(handle).isEqualTo("my-blog-handle");
    }

    // test if error displays when title has 3 character or less 
    @Test
    void testTitleValidation() {
        // Create a new blog object
        Blog blog = new Blog();

        // Set the blog properties
        blog.setName("");

        // Assert that the validation constraint for title is violated
        Exception exception = assertThrows(ConstraintViolationException.class, () -> blog.validate());
        assertThat(exception.getMessage()).contains("Title must not be empty or have 3 or more than characters");
    }
    
}
