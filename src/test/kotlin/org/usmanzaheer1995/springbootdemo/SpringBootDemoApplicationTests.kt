package org.usmanzaheer1995.springbootdemo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.test.context.TestPropertySource

@Import(TestSpringBootDemoApplication::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["spring.datasource.url=jdbc:tc:postgresql:16:///todos"])
class SpringBootDemoApplicationTests {

	@Test
	fun contextLoads() {
	}

}
