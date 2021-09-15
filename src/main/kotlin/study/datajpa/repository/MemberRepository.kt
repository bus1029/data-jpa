package study.datajpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import study.datajpa.dto.MemberDto
import study.datajpa.entity.Member

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
}