package study.datajpa.entity

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@SpringBootTest
@Transactional
internal class MemberTest {
  @PersistenceContext
  lateinit var em: EntityManager

  @Test
  fun testEntity() {
    val teamA = Team("teamA")
    val teamB = Team("teamB")
    em.persist(teamA)
    em.persist(teamB)

    val memberA = Member("memberA", 10, teamA)
    val memberB = Member("memberB", 20, teamA)
    val memberC = Member("memberC", 30, teamB)
    val memberD = Member("memberD", 40, teamB)

    em.persist(memberA)
    em.persist(memberB)
    em.persist(memberC)
    em.persist(memberD)
    em.flush()
    em.clear()

    val members = em.createQuery("select m from Member m", Member::class.java).resultList
    members.forEach { member ->
      println("member = ${member}")
      println("member.team = ${member.team}")
    }
  }
}