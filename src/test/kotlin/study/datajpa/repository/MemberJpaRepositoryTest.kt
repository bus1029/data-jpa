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
    val findMember = memberJpaRepository.find(savedMember.id!!)

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

    val findMember1 = memberJpaRepository.find(member1.id!!)
    val findMember2 = memberJpaRepository.find(member2.id!!)
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

  @Test
  fun findByUsernameAndAgeGreaterThan() {
    val m1 = Member("AAA", 10, null)
    val m2 = Member("BBB", 20, null)
    memberJpaRepository.save(m1)
    memberJpaRepository.save(m2)

    val members = memberJpaRepository.findByUsernameAndAgeGreaterThan("BBB", 15)
    assertThat(members?.size).isEqualTo(1)
  }

  @Test
  fun findByUsernameUsingNamedQuery() {
    val m1 = Member("AAA", 10, null)
    val m2 = Member("BBB", 20, null)
    memberJpaRepository.save(m1)
    memberJpaRepository.save(m2)

    val members = memberJpaRepository.findByUserName("BBB")
    assertThat(members?.size).isEqualTo(1)
  }

  @Test
  fun paging() {
    memberJpaRepository.save(Member("member1", 10, null))
    memberJpaRepository.save(Member("member2", 10, null))
    memberJpaRepository.save(Member("member3", 10, null))
    memberJpaRepository.save(Member("member4", 10, null))
    memberJpaRepository.save(Member("member5", 10, null))

    val age = 10
    val offset = 1
    val limit = 3

    val totalCount = memberJpaRepository.totalCount(age)
    val members = memberJpaRepository.findByPage(age, offset, limit)

    // 페이지 계산 공식 적용
    // totalPage = totalCount / size ...
    // 마지막 페이지...
    // 최초 페이지...

    assertThat(members?.size).isEqualTo(3)
    assertThat(totalCount).isEqualTo(5L)
  }

  @Test
  fun bulkUpdate() {
    memberJpaRepository.save(Member("member1", 10, null))
    memberJpaRepository.save(Member("member2", 19, null))
    memberJpaRepository.save(Member("member3", 20, null))
    memberJpaRepository.save(Member("member4", 21, null))
    memberJpaRepository.save(Member("member5", 40, null))

    val resultCount = memberJpaRepository.bulkAgePlus(20)
    assertThat(resultCount).isEqualTo(3)
  }
}