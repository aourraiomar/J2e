package org.sid.cinema.dao;

import java.util.List;

import org.sid.cinema.entities.Cinema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
@RepositoryRestResource
@CrossOrigin("*")

public interface CinemaRepository  extends JpaRepository<Cinema, Long>{
	
	public Page<Cinema>  findByNameContains(String name,Pageable pageable);
	
	public List<Cinema> findByNameContains(String keyCinema);

}
