package study.datajpa.repository

import org.assertj.core.api.Assertions
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
class TiberoTest {
  @Autowired
  private lateinit var memberRepository: MemberRepository

  @Test
  fun tiberoTest() {
    var member1 = Member("member11")
    var member2 = Member("member22")
    // 위에서 선언한 member 들을 사용하면 내부 id가 -1로 설정되어 있음
    member1 = memberRepository.save(member1)
    member2 = memberRepository.save(member2)

    val findMember1 = memberRepository.findByIdOrNull(member1.id)
    val findMember2 = memberRepository.findByIdOrNull(member2.id)
    Assertions.assertThat(findMember1).isEqualTo(member1)
    Assertions.assertThat(findMember2).isEqualTo(member2)
  }
}