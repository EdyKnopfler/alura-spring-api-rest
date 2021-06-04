package br.com.pip.forum.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pip.forum.modelo.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
	
	public Curso findByNome(String nome);

}
