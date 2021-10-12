package study.datajpa.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import study.datajpa.entity.Item
import study.datajpa.entity.Item2

@SpringBootTest
class ItemRepositoryTest2 {
  @Autowired
  lateinit var itemRepository: ItemRepository2

  @Test
  fun save() {
    // ID 를 지정해줬기 때문에 persist 가 호출되지 않고 merge 가 호출됨
    val item = Item2("A")
    itemRepository.save(item)
  }
}