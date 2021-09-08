package study.datajpa.repository

import org.springframework.stereotype.Repository
import study.datajpa.entity.Team
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class TeamJpaRepository {
  @PersistenceContext
  private lateinit var em: EntityManager

  fun save(team: Team): Team {
    em.persist(team)
    return team
  }

  fun delete(team: Team) {
    em.remove(team)
  }

  fun findAll(): MutableList<Team>? {
    return em.createQuery("select t from Team t", Team::class.java)
      .resultList
  }

  fun findById(id: Long): Team? {
    return em.find(Team::class.java, id)
  }

  fun count(): Long? {
    return em.createQuery("select count(t) from Team t", Long::class.javaObjectType)
      .singleResult
  }
}