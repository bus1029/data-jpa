package study.datajpa.repository

import org.springframework.stereotype.Repository
import study.datajpa.entity.Member
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class MemberJpaRepository {
  // 자동으로 EntityManager 를 주입해줌
  @PersistenceContext
  private lateinit var em: EntityManager

  fun save(member: Member): Member {
    em.persist(member)
    return member
  }

  fun find(id: Long): Member {
    return em.find(Member::class.java, id)
  }
}