package study.datajpa.repository

import org.springframework.stereotype.Repository
import study.datajpa.entity.Member
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.TypedQuery

@Repository
class MemberJpaRepository {
  // 자동으로 EntityManager 를 주입해줌
  @PersistenceContext
  private lateinit var em: EntityManager

  fun save(member: Member): Member {
    em.persist(member)
    return member
  }

  fun find(id: Long): Member? {
    return em.find(Member::class.java, id)
  }

  fun delete(member: Member) {
    em.remove(member)
  }

  fun findAll(): MutableList<Member>? {
    return em.createQuery("select m from Member m", Member::class.java)
      .resultList
  }

  fun count(): Long? {
    return em.createQuery("select count(m) from Member m", Long::class.javaObjectType)
      .singleResult
  }

  fun findByUsernameAndAgeGreaterThan(username: String, age: Int): MutableList<Member>? {
    return em.createQuery("select m from Member m where m.username = :username and m.age > :age", Member::class.java)
      .setParameter("username", username)
      .setParameter("age", age)
      .resultList
  }
}