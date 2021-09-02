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
class MemberRepositoryTest {
  @Autowired
  private lateinit var memberRepository: MemberRepository

  @Test
  fun testMember() {
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
      Assertions.assertThat(it.id).isEqualTo(savedMember.id)
      Assertions.assertThat(it.username).isEqualTo(savedMember.username)
      // repeatable-read 보장
      Assertions.assertThat(it).isEqualTo(savedMember)
    }
  }
}