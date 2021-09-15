package study.datajpa.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import study.datajpa.dto.MemberDto
import study.datajpa.entity.Member
import java.util.*

interface MemberRepository : JpaRepository<Member, Long> {
  fun findByUsernameAndAgeGreaterThan(username: String, age: Int): MutableList<Member>
  @Query(name = "Member.findByUsername") // 생략 가능, 반환 값의 Member.메소드명 으로 Named Query를 먼저 검색, 없을 경우 메소드 쿼리 기능 실행
  // Named Parameter 를 JPQL 에 작성했을 때
  fun findByUsername(@Param("username") username: String): MutableList<Member>

  @Query("select m from Member m where m.username = :username and m.age = :age")
  fun findUser(@Param("username") username: String, @Param("age") age: Int): MutableList<Member>

  @Query("select m.username from Member m")
  fun findUsernameList(): MutableList<String>

  @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
  fun findMemberDto(): MutableList<MemberDto>

  @Query("select m from Member m where m.username in :names")
  fun findByNames(@Param("names") names: MutableList<String>): MutableList<Member>

  fun findListByUsername(username: String): MutableList<Member> // 컬렉션
  fun findMemberByUsername(username: String): Member? // 단건
  fun findOptionalByUsername(username: String): Optional<Member> // 단건 Optional

  fun findByAge(age: Int, pageable: Pageable): Page<Member>
  fun findSliceByAge(age: Int, pageable: Pageable): Slice<Member>

  @Query(value = "select m from Member m left join m.team t",
          countQuery = "select count(m) from Member m")
  fun findExceptCountByAge(age: Int, pageable: Pageable): Page<Member>

  @Modifying(clearAutomatically = true)
  @Query(value = "update Member m set m.age = m.age + 1 where m.age >= :age")
  fun bulkAgePlus(@Param("age") age: Int): Int

  @Query(value = "select m from Member m left join fetch m.team")
  fun findMemberFetchJoin(): MutableList<Member>

  @EntityGraph(attributePaths = ["team"])
  override fun findAll(): MutableList<Member>

  @EntityGraph(attributePaths = ["team"])
  @Query("select m from Member m")
  fun findMemberEntityGraph(): MutableList<Member>

//  @EntityGraph(attributePaths = ["team"])
  @EntityGraph("Member.all")
  fun findEntityGraphByUsername(@Param("username") username: String): MutableList<Member>
}