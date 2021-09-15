package study.datajpa.repository

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import study.datajpa.entity.Member
import study.datajpa.entity.Team

@SpringBootTest
@Transactional // 테스트가 끝나면 결과를 롤백
@Rollback(value = false) // Rollback 을 하지 않음
class MemberRepositoryTest {
  @Autowired
  private lateinit var memberRepository: MemberRepository
  @Autowired
  private lateinit var teamRepository: TeamRepository

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

  @Test
  fun findByUsernameAndAgeGreaterThan() {
    val m1 = Member("AAA", 10, null)
    val m2 = Member("BBB", 20, null)
    memberRepository.save(m1)
    memberRepository.save(m2)

    val members = memberRepository.findByUsernameAndAgeGreaterThan("BBB", 15)
    assertThat(members.size).isEqualTo(1)
  }

  @Test
  fun findByUsernameUsingNamedQuery() {
    val m1 = Member("AAA", 10, null)
    val m2 = Member("BBB", 20, null)
    memberRepository.save(m1)
    memberRepository.save(m2)

    val members = memberRepository.findByUsername("BBB")
    assertThat(members.size).isEqualTo(1)
  }

  @Test
  fun findByUsernameUsingQuery() {
    val m1 = Member("AAA", 10, null)
    val m2 = Member("BBB", 20, null)
    memberRepository.save(m1)
    memberRepository.save(m2)

    val members = memberRepository.findUser("BBB", 20)
    assertThat(members.size).isEqualTo(1)
  }

  @Test
  fun findUsernameList() {
    val m1 = Member("AAA", 10, null)
    val m2 = Member("BBB", 20, null)
    memberRepository.save(m1)
    memberRepository.save(m2)

    val members = memberRepository.findUsernameList()
    members.forEach {
      println("username = $it")
    }
  }

  @Test
  fun findMemberDto() {
    var teamA = Team("teamA")
    //ID 가 발급된 teamA 를 사용해야 하기 때문에
    teamA = teamRepository.save(teamA)

    val m1 = Member("AAA", 10, teamA)
    val m2 = Member("BBB", 20, teamA)
    memberRepository.save(m1)
    memberRepository.save(m2)

    val memberDtos = memberRepository.findMemberDto()
    memberDtos.forEach {
      println("memberDto = $it")
    }
  }

  @Test
  fun findByNames() {
    val m1 = Member("AAA", 10, null)
    val m2 = Member("BBB", 20, null)
    memberRepository.save(m1)
    memberRepository.save(m2)

    val members = memberRepository.findByNames(mutableListOf("AAA", "BBB"))
    members.forEach {
      println("memberDto = $it")
    }
  }

  @Test
  fun testReturnType() {
    val m1 = Member("AAA", 10, null)
    val m2 = Member("BBB", 20, null)
    memberRepository.save(m1)
    memberRepository.save(m2)

    val findListByUsername = memberRepository.findListByUsername("AAA")
    findListByUsername.forEach {
      println("findMember = ${it}")
    }

    // 없는 데이터지만 빈 리스트를 반환
    val findListByUsernameEmpty = memberRepository.findListByUsername("asdf")
    println("findListByUsernameEmpty size = ${findListByUsernameEmpty.size}")

    val findMemberByUsername = memberRepository.findMemberByUsername("AAA")
    println("findMemberByUsername = $findMemberByUsername")

    // Single Result의 경우 결과가 없으면 null 반환 -> Spring-data-jpa 가 감싸서 처리
    // JPA 의 기본 동작은 SingleResult 로 검색했을 때 값이 없을 경우 Exception 반환
    val findMemberByUsernameNull = memberRepository.findMemberByUsername("asdf")
    println("findMemberByUsername = $findMemberByUsernameNull")

    // 같은 데이터가 2 개 이상일 경우 Exception 이 터지지만 Spring 이 해당 Exception 을 한번 감싸서 다시 던짐
    val findOptionalByUsername = memberRepository.findOptionalByUsername("AAA")
    println("findOptionalByUsername = $findOptionalByUsername")
  }
}