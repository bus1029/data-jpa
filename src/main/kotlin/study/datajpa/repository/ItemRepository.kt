package study.datajpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import study.datajpa.entity.Item

interface ItemRepository : JpaRepository<Item, Long> {
}