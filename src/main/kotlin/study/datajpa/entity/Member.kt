package study.datajpa.entity

import javax.persistence.*

@Entity
@NamedQuery(
  name = "Member.findByUsername",
  query = "select m from Member m where m.username = :username"
)
@NamedEntityGraph(
  name = "Member.all",
  attributeNodes = [NamedAttributeNode("team")]
)
class Member constructor(var username: String = "") : BaseEntity() {
  @Id @GeneratedValue
  @Column(name = "member_id")
  var id: Long? = null
  var age: Int = -1

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  var team: Team? = null

  constructor(username: String, age: Int, team: Team?) : this(username) {
    this.username = username
    this.age = age
    team?.let {
      changeTeam(it)
    }
  }

  override fun toString(): String {
    return "Member(username='$username', id=$id, age=$age)"
  }

  fun changeTeam(team: Team) {
    this.team = team
    this.team!!.members.add(this)
  }
}