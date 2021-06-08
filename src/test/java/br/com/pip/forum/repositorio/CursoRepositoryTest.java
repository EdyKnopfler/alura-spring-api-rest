package br.com.pip.forum.repositorio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import br.com.pip.forum.modelo.Curso;

//Teste de Repositório: transação, injeção de EntityManager...
@DataJpaTest

// https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/html/boot-features-testing.html
// "In-memory embedded databases generally work well for tests, since they are fast and do not require 
// any installation. If, however, you prefer to run tests against a real database you can use the 
// @AutoConfigureTestDatabase annotation..."
@AutoConfigureTestDatabase(replace = Replace.NONE)

// Nas Run Configurations poderia ter: -Dspring.profiles.active=dev,test
// Os profiles são carregados na ordem, o "test" só sobrescreve o nome do database.
// Ref.: https://devkico.itexto.com.br/?p=3157
@ActiveProfiles({"dev", "test"})
public class CursoRepositoryTest {
	
	private static final String nomeCurso = "HTML 5";
	
	@Autowired  // Olha o que ganhamos com as anotações do Spring :)
	private CursoRepository repository;
	
	@Autowired
	private TestEntityManager em;
	
	@BeforeEach
	public void cursoDeTeste() {
		Curso curso = new Curso();
		curso.setNome(nomeCurso);
		em.persist(curso);
	}

	@Test
	public void deveriaCarregarUmCursoAoBuscarPeloNome() {
		Curso curso = repository.findByNome(nomeCurso);
		assertNotNull(curso);
		assertEquals(nomeCurso, curso.getNome());
	}

	@Test
	public void naoDeveriaCarregarUmCursoInexistente() {
		Curso curso = repository.findByNome("mimimimimi");
		assertNull(curso);
	}

}
