package se.mojujo.blogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.mojujo.blogservice.post.BlogPost;

import java.util.List;
import java.util.UUID;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, UUID> {

    List<BlogPost> findAllByUserId(UUID userId);
    List<BlogPost> findAllByUserIdOrderByCreatedDateDesc(UUID userId);
}
