package study.datajpa.controller

import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import study.datajpa.entity.Member
import study.datajpa.repository.MemberRepository
import javax.annotation.PostConstruct

@RestController
class MemberController constructor(private var memberRepository: MemberRepository) {
  @GetMapping("/members/{id}")
  fun findMember(@PathVariable id: Long): String? {
    val member = memberRepository.findByIdOrNull(id)
    return member?.username
  }

  @GetMapping("/members2/{id}")
  fun findMemberWithConverter(@PathVariable("id") member: Member): String? {
    return member.username
  }

  @PostConstruct
  fun init() {
    memberRepository.save(Member("userA"))
  }
}