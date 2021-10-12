package study.datajpa.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import study.datajpa.entity.Item

@SpringBootTest
class ItemRepositoryTest {
  @Autowired lateinit var itemRepository: ItemRepository

  @Test
  fun save() {
    // 생성 당시에는 ID 가 생기지 않음, null인 상태
    val item = Item()
    itemRepository.save(item)
  }
}