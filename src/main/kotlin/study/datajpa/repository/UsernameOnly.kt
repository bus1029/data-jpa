package study.datajpa.repository

import org.springframework.beans.factory.annotation.Value

interface UsernameOnly {
//  @Value("#{target.username + ' ' + target.age}")
  fun getUsername(): String?
}