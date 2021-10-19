package study.datajpa.dto

import study.datajpa.entity.Member

data class MemberDto(var id: Long = 0, var username: String = "", var teamName: String = "") {
  constructor(member: Member) : this() {
    this.id = member.id!!
    this.username = member.username
    this.teamName = member.team?.name.toString()
  }
}
