package study.datajpa.repository

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import study.datajpa.dto.MemberDto
import study.datajpa.entity.Member
import study.datajpa.entity.Team
import study.datajpa.repository.specification.MemberSpec
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@SpringBootTest
@Transactional // 테스트가 끝나면 결과를 롤백
@Rollback(value = false) // Rollback 을 하지 않음
class MemberRepositoryTest {
  @Autowired
  private lateinit var memberRepository: MemberRepository
  @Autowired
  private lateinit var teamRepository: TeamRepository
  @PersistenceContext
  private lateinit var em: EntityManager

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

  @Test
  fun paging() {
    memberRepository.save(Member("member1", 10, null))
    memberRepository.save(Member("member2", 10, null))
    memberRepository.save(Member("member3", 10, null))
    memberRepository.save(Member("member4", 10, null))
    memberRepository.save(Member("member5", 10, null))

    val age = 10
    val offset = 1
    val limit = 3

    val pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"))
    // 반환 타입에 따라서 Total Count 쿼리를 날릴지 말지 결정
    val page = memberRepository.findByAge(age, pageRequest)
    val memberDto: Page<MemberDto> = page.map { member ->
      MemberDto(member.id, member.username, "")
    }
    val content = page.content
    val totalCount = page.totalElements

    content.forEach {
      println("member = $it")
    }

    assertThat(content.size).isEqualTo(3)
    assertThat(totalCount).isEqualTo(5L)
    assertThat(page.number).isEqualTo(0)
    assertThat(page.totalPages).isEqualTo(2)
    assertThat(page.isFirst).isTrue
    assertThat(page.hasNext()).isTrue

    val slice = memberRepository.findSliceByAge(age, pageRequest)
    val sliceContent = slice.content

    // 카운트 쿼리가 날아가지 않음
    sliceContent.forEach {
      println("member = $it")
    }

    assertThat(content.size).isEqualTo(3)
    assertThat(page.number).isEqualTo(0)
    assertThat(page.isFirst).isTrue
    assertThat(page.hasNext()).isTrue
  }

  @Test
  fun bulkUpdate() {
    memberRepository.save(Member("member1", 10, null))
    memberRepository.save(Member("member2", 19, null))
    memberRepository.save(Member("member3", 20, null))
    memberRepository.save(Member("member4", 21, null))
    memberRepository.save(Member("member5", 40, null))

    val resultCount = memberRepository.bulkAgePlus(20)
    assertThat(resultCount).isEqualTo(3)
  }

  @Test
  fun findMemberLazy() {
    // given
    // member1 -> teamA
    // member2 -> teamB
    var teamA = Team("teamA")
    var teamB = Team("teamB")
    teamA = teamRepository.save(teamA)
    teamB = teamRepository.save(teamB)

    val memberA = Member("memberA", 10, teamA)
    val memberB = Member("memberB", 20, teamB)
    memberRepository.save(memberA)
    memberRepository.save(memberB)

    em.flush()
    em.clear()

    val members = memberRepository.findAll()
    members.forEach {
      println("member = ${it}")
      println("teamClass = ${it.team?.javaClass}")
      println("team = ${it.team}")
    }
  }

  @Test
  fun findMemberFetchJoin() {
    // given
    // member1 -> teamA
    // member2 -> teamB
    var teamA = Team("teamA")
    var teamB = Team("teamB")
    teamA = teamRepository.save(teamA)
    teamB = teamRepository.save(teamB)

    val memberA = Member("memberA", 10, teamA)
    val memberB = Member("memberB", 20, teamB)
    memberRepository.save(memberA)
    memberRepository.save(memberB)

    em.flush()
    em.clear()

    val members = memberRepository.findMemberFetchJoin()
    members.forEach {
      println("member = ${it}")
      println("teamClass = ${it.team?.javaClass}")
      println("team = ${it.team}")
    }
  }

  @Test
  fun queryHint() {
    val memberA = memberRepository.save(Member("memberA", 10, null))
    em.flush()
    em.clear()
    // 영속성 컨텍스트가 비워지기 때문에 쿼리 발행

//    val findMember = memberRepository.findByIdOrNull(memberA.id)
//    // Dirty Check 기능을 위해 원본 데이터인 memberA 외에 변경된 member2 도 갖고 있게 됨
//    findMember?.username = "member2"
//    em.flush()

    // 오직 조회만을 위해 사용하고 싶다면?
    // Read-only 이기 때문에 내부적으로 스냅샷을 만들지 않는 방향으로 최적화를 진행
    val findMember = memberRepository.findReadOnlyByUsername("memberA")
    findMember.username = "member2"
    em.flush()
  }

  @Test
  fun lock() {
    val memberA = memberRepository.save(Member("memberA", 10, null))
    em.flush()
    em.clear()

    val members = memberRepository.findLockByUsername("memberA")
  }

  @Test
  fun callCustomMemberRepository() {
    val findMemberCustom = memberRepository.findMemberCustom()
  }


  @Test
  fun jpaEventBaseEntity() {
    var member = Member("member1")
    member = memberRepository.save(member)

    Thread.sleep(100);
    member.username = "member2" // @Prepersist

    em.flush()
    em.clear()

    val member1 = memberRepository.findByIdOrNull(member.id)

    println("Created Date:  ${member1?.createdDate}")
    println("Updated Date: ${member1?.lastModifiedDate}")
    println("Created By: ${member1?.createdBy}")
    println("Modified By: ${member1?.lastModifiedBy}")
  }

  @Test
  fun specBasic() {
    val teamA = Team("teamA")
    em.persist(teamA)

    val member1 = Member("m1", 0, teamA)
    val member2 = Member("m2", 0, teamA)
    em.persist(member1)
    em.persist(member2)

    em.flush()
    em.clear()

    val specification = MemberSpec.username("m1").and(MemberSpec.teamName("teamA"))
    val members = memberRepository.findAll(specification)
    assertThat(members.size).isEqualTo(1)
  }
}