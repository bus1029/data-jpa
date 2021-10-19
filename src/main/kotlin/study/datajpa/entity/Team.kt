package study.datajpa.entity

import javax.persistence.*

@Entity
class Team constructor(var name: String = "") {
  @Id @GeneratedValue
  @Column(name = "team_id")
  var id: Long? = null

  @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
  var members = mutableListOf<Member>()

  override fun toString(): String {
    return "Team(id=$id, name='$name')"
  }
}