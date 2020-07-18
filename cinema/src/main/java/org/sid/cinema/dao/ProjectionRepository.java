package org.sid.cinema.dao;


import java.util.Date;

import org.sid.cinema.entities.Film;
import org.sid.cinema.entities.Projection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
@RepositoryRestResource
@CrossOrigin("*")
public interface ProjectionRepository  extends JpaRepository<Projection, Long>{
	 public Page<Projection> findByPrix(double mc, Pageable page);
	 public Page<Projection> findByFilm(Film filmname,Pageable page);
	 public Page<Projection> findBydateProjection(Date d,Pageable page);
}
