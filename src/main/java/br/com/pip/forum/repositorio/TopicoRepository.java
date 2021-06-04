package br.com.pip.forum.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pip.forum.modelo.Topico;

// Interface não precisa ter anotação!
public interface TopicoRepository extends JpaRepository<Topico, Long> {

	List<Topico> findByCursoNome(String nomeCurso);

	//@Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso")
	//List<Topico> consultarPorNomeDoCurso(@Param("nomeCurso") String nomeCurso);
	
}
