package study.datajpa.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Item {
  @Id @GeneratedValue
  var id: Long? = null
}