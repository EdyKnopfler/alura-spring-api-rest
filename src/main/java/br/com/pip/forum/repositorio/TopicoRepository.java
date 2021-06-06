package br.com.pip.forum.repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pip.forum.modelo.Topico;

// Interface não precisa ter anotação!
public interface TopicoRepository extends JpaRepository<Topico, Long> {

	Page<Topico> findByCursoNome(String nomeCurso, Pageable paginacao);

	//@Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso")
	//List<Topico> consultarPorNomeDoCurso(@Param("nomeCurso") String nomeCurso);
	
}
