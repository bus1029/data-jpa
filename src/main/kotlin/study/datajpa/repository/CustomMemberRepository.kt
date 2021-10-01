package study.datajpa.repository

import study.datajpa.entity.Member

interface CustomMemberRepository {
  fun findMemberCustom(): List<Member>
}