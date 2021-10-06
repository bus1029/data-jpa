package study.datajpa.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import study.datajpa.dto.MemberDto
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

  @GetMapping("/members")
  fun list(@PageableDefault(size = 5) pageable: Pageable): Page<Member> {
    return memberRepository.findAll(pageable)
  }

  @GetMapping("/members2")
  fun listWithDto(@PageableDefault(size = 5) pageable: Pageable): Page<MemberDto> {
    return memberRepository.findAll(pageable)
      .map { member -> MemberDto(member.id, member.username, "") }
  }

  @GetMapping("/members3")
  fun listWithMemberDto(@PageableDefault(size = 10) pageable: Pageable): Page<MemberDto> {
    return memberRepository.findAll(pageable)
      .map { member -> MemberDto(member) }
  }

  @PostConstruct
  fun init() {
    memberRepository.save(Member("userA"))
    for (i in 0..99) {
      memberRepository.save(Member("user$i", i, null))
    }
  }
}