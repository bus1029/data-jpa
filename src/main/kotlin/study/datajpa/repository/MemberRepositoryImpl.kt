package study.datajpa.repository

import study.datajpa.entity.Member
import javax.persistence.EntityManager

class MemberRepositoryImpl constructor(private var entityManager: EntityManager): CustomMemberRepository {
  override fun findMemberCustom(): List<Member> {
    return entityManager.createQuery("select m from Member m", Member::class.java)
      .resultList
  }
}