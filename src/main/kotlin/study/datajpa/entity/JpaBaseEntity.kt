package study.datajpa.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

@MappedSuperclass
class JpaBaseEntity {
  @Column(updatable = false)
  lateinit var createdDate: LocalDateTime
  lateinit var updateDate: LocalDateTime

  @PrePersist
  fun prePersist() {
    val now = LocalDateTime.now()
    createdDate = now
    updateDate = now
  }

  @PreUpdate
  fun preUpdate() {
    updateDate = LocalDateTime.now()
  }
}