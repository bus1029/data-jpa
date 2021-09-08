package study.datajpa.repository

import org.assertj.core.api.Assertions
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

    assertThat(findMember?.id).isEqualTo(savedMember.id)
    assertThat(findMember?.username).isEqualTo(savedMember.username)
    // repeatable-read 보장
    assertThat(findMember).isEqualTo(savedMember)
  }

  @Test
  fun crud() {
    val member1 = Member("member1")
    val member2 = Member("member2")
    memberJpaRepository.save(member1)
    memberJpaRepository.save(member2)

    val findMember1 = memberJpaRepository.find(member1.id)
    val findMember2 = memberJpaRepository.find(member2.id)
    assertThat(findMember1).isEqualTo(member1)
    assertThat(findMember2).isEqualTo(member2)

    val members = memberJpaRepository.findAll()
    assertThat(members?.size).isEqualTo(2)

    val count = memberJpaRepository.count()
    assertThat(members?.size?.toLong()).isEqualTo(count)

    memberJpaRepository.delete(member1)
    memberJpaRepository.delete(member2)
    val afterDeleteCount = memberJpaRepository.count()
    assertThat(afterDeleteCount).isEqualTo(0)
  }
}