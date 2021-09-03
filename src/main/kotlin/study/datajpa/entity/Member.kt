package study.datajpa.entity

import javax.persistence.*

@Entity
class Member constructor(var username: String = "") {
  @Id @GeneratedValue
  @Column(name = "member_id")
  var id: Long = -1
  var age: Int = -1

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  lateinit var team: Team

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
    this.team.members.add(this)
  }
}