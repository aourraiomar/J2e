package org.sid.cinema.dao;


import java.util.List;

import org.sid.cinema.entities.Ville;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
@RepositoryRestResource
@CrossOrigin("*")
	public interface VilleRepository  extends JpaRepository<Ville, Long>{
	@RestResource(exported = false)
	 public Page<Ville> findByNameContains(String name,Pageable pageable);
	@RestResource(exported = false)
	public List<Ville> findByNameContains(String name);

}
