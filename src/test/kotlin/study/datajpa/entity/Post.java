package study.datajpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Post {
  @Id
  @GeneratedValue
  private Long id;
  private String title;

  public Post(Long id, String title) {
    this.id = id;
    this.title = title;
  }

  public Post() {
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
