package study.datajpa.repository

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import study.datajpa.entity.Member

@SpringBootTest
@Transactional // 테스트가 끝나면 결과를 롤백
@Rollback(value = false) // Rollback 을 하지 않음
class MemberRepositoryTest {
  @Autowired
  private lateinit var memberRepository: MemberRepository

  @Test
  fun testMember() {
    println("memberRepository = ${memberRepository.javaClass}")
    val member = Member("memberA")
    val savedMember = memberRepository.save(member)
    // Can return null
    val findMember = memberRepository.findByIdOrNull(savedMember.id)

    /*
      1. 지정된 값이 null 이 아닌 경우에 코드를 실행해야 하는 경우
      2. Nullable 객체를 다른 Nullable 객체로 변환하는 경우
      3. 단일 지역 변수의 범위를 제한하는 경우
     */
    findMember?.let {
      assertThat(it.id).isEqualTo(savedMember.id)
      assertThat(it.username).isEqualTo(savedMember.username)
      // repeatable-read 보장
      assertThat(it).isEqualTo(savedMember)
    }
  }

  @Test
  fun crud() {
    var member1 = Member("member11")
    var member2 = Member("member22")
    // 위에서 선언한 member 들을 사용하면 내부 id가 -1로 설정되어 있음
    member1 = memberRepository.save(member1)
    member2 = memberRepository.save(member2)

    val findMember1 = memberRepository.findByIdOrNull(member1.id)
    val findMember2 = memberRepository.findByIdOrNull(member2.id)
    assertThat(findMember1).isEqualTo(member1)
    assertThat(findMember2).isEqualTo(member2)

    val members = memberRepository.findAll()
    assertThat(members.size).isEqualTo(2)

    val count = memberRepository.count()
    assertThat(members.size.toLong()).isEqualTo(count)

    memberRepository.delete(member1)
    memberRepository.delete(member2)
    val afterDeleteCount = memberRepository.count()
    assertThat(afterDeleteCount).isEqualTo(0)
  }
}