package study.datajpa.repository.specification

import org.springframework.data.jpa.domain.Specification
import org.springframework.util.StringUtils
import study.datajpa.entity.Member
import study.datajpa.entity.Team
import javax.persistence.criteria.*

class MemberSpec {
  companion object {
    fun teamName(teamName: String): Specification<Member> {
      return object : Specification<Member> {
        override fun toPredicate(root: Root<Member>, query: CriteriaQuery<*>,
                                 criteriaBuilder: CriteriaBuilder): Predicate? {
          if (!StringUtils.hasLength(teamName)) {
            return null
          }

          val t: Join<Member, Team> = root.join("team", JoinType.INNER)
          return criteriaBuilder.equal(t.get<String>("name"), teamName)
        }
      }
    }

    fun username(username: String): Specification<Member> {
      return Specification<Member> { root, query, criteriaBuilder ->
        criteriaBuilder.equal(root.get<String>("username"), username)
      }
    }
  }
}