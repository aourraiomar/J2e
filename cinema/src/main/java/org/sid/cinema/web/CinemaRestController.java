package org.sid.cinema.web;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.sid.cinema.dao.FilmRepository;
import org.sid.cinema.dao.TicketRepository;
import org.sid.cinema.entities.Film;
import org.sid.cinema.entities.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;


@RestController
@CrossOrigin("*")
public class CinemaRestController {
	
	@Autowired
	FilmRepository filmrepositeroy;
	@Autowired
	TicketRepository ticketrepository;
@GetMapping(path="/imageFilm/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
public byte[] image(@PathVariable (name="id") Long id) throws IOException {
	Film f=filmrepositeroy.findById(id).get();
	String photoName=f.getPhoto();
	File file=new File(System.getProperty("user.home")+"/Desktop/Media/imagej2e/"+photoName);
	 Path path = Paths.get(file.toURI());
	return Files.readAllBytes(path);
	}
@CrossOrigin(origins = "http://localhost:4200")
@PostMapping("/payerTickets")
@Transactional
	public List<Ticket> payerTickets(@RequestBody TicketForm ticketForm){
		List<Ticket> listTickets=new ArrayList<>();
		ticketForm.getTickets().forEach(idTicket->{
			Ticket ticket =ticketrepository.findById(idTicket).get();
			ticket.setNomClient(ticketForm.getNomClient());
			ticket.setReserve(true);
			ticket.setCodePayement(ticketForm.getCodePayment());
			ticketrepository.save(ticket);
			listTickets.add(ticket);
		});
		return listTickets;
	}

}
@Data
class TicketForm{
	private String nomClient;
	private Integer codePayment;
	private List<Long> tickets=new ArrayList<>();
}