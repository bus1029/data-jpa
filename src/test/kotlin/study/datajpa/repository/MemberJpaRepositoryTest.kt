package study.datajpa.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import study.datajpa.entity.Member

@SpringBootTest
@Transactional // 테스트가 끝나면 결과를 롤백
@Rollback(value = false) // Rollback 을 하지 않음
internal class MemberJpaRepositoryTest {
  @Autowired
  private lateinit var memberJpaRepository: MemberJpaRepository

  @Test
  fun testMember() {
    val member = Member("memberA")
    val savedMember = memberJpaRepository.save(member)
    val findMember = memberJpaRepository.find(savedMember.id)

    assertThat(findMember.id).isEqualTo(savedMember.id)
    assertThat(findMember.username).isEqualTo(savedMember.username)
    // repeatable-read 보장
    assertThat(findMember).isEqualTo(savedMember)
  }
}