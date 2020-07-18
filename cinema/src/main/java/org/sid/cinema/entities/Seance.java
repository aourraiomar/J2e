package org.sid.cinema.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.web.bind.annotation.CrossOrigin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Seance {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
	@Temporal(TemporalType.TIME)//garder que le temp(h/m/s)
private Date heureDebut;
	

}
