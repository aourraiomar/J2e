package org.sid.cinema.dao;


import org.sid.cinema.entities.Salle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
@RepositoryRestResource
@CrossOrigin("*")

public interface SalleRepository  extends JpaRepository<Salle, Long>{
	public Page<Salle>  findByNameContains(String name,Pageable pageable);

}
