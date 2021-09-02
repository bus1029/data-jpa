package study.datajpa.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Member constructor(var username: String = "") {
  @Id @GeneratedValue
  var id: Long = -1
    private set
}