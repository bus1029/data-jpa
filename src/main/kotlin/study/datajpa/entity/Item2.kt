package study.datajpa.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Id

@Entity
@EntityListeners(AuditingEntityListener::class)
class Item2 constructor(@Id private var id: String? = null) : Persistable<String> {
  @CreatedDate
  private var createdDate: LocalDateTime? = null

  override fun getId(): String? {
    return id
  }

  override fun isNew(): Boolean {
    return createdDate == null
  }
}