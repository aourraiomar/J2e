package org.sid.cinema.dao;

import org.sid.cinema.entities.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
@RepositoryRestResource
@CrossOrigin("*")

public interface FilmRepository  extends JpaRepository<Film, Long>{
	
	public Page<Film> findByTitreContains(String keyFilm, Pageable page);
}
