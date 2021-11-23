package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Post;
import study.datajpa.entity.PostRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SaveAndMergeTest {
  @Autowired
  private PostRepository postRepository;
  @PersistenceContext
  private EntityManager entityManager;

  @Test
  @Transactional
  void saveAndMergeTest() {
    // 1
    Post post = new Post();
    post.setTitle("jpa");
    entityManager.persist(post);

    // 2
    assertThat(entityManager.contains(post)).isTrue();

    // 3
    Post post2 = new Post();
    post2.setId(post.getId());
    post2.setTitle("hibernate");
//    Post updatedPost = postRepository.save(post2);// merge
    Post updatedPost = entityManager.merge(post2);

    // 4
    assertThat(entityManager.contains(post2)).isFalse();
    assertThat(entityManager.contains(updatedPost)).isTrue();
    assertThat(post2 == updatedPost);

    postRepository.findAll();
  }
}
