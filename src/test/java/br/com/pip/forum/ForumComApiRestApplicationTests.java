package br.com.pip.forum;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest  // Sobe a aplicação :)
@ActiveProfiles({"test"})  // O profile "test" apenas sobrescreve o nome do database em seu arquivo .properties
class ForumComApiRestApplicationTests {

	@Test
	void contextLoads() {
	}

}
