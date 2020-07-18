package org.sid.cinema.services;

import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.sid.cinema.dao.CategorieRepository;
import org.sid.cinema.dao.CinemaRepository;
import org.sid.cinema.dao.FilmRepository;
import org.sid.cinema.dao.PlaceRepository;
import org.sid.cinema.dao.ProjectionRepository;
import org.sid.cinema.dao.SalleRepository;
import org.sid.cinema.dao.SeanceRepository;
import org.sid.cinema.dao.TicketRepository;
import org.sid.cinema.dao.VilleRepository;
import org.sid.cinema.entities.Categorie;
import org.sid.cinema.entities.Cinema;
import org.sid.cinema.entities.Film;
import org.sid.cinema.entities.Place;
import org.sid.cinema.entities.Projection;
import org.sid.cinema.entities.Salle;
import org.sid.cinema.entities.Seance;
import org.sid.cinema.entities.Ticket;
import org.sid.cinema.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CinemaInitServiceImpl  implements ICinemaInitService{
	@Autowired
	private VilleRepository villeRepository;
	@Autowired
	private CinemaRepository cinemaRepository;
	@Autowired
	private SalleRepository salleRepository;
	@Autowired
	private PlaceRepository placeRepository;
	@Autowired
	private SeanceRepository seanceRepository;
	@Autowired
	private FilmRepository filmRepository;
	@Autowired
	private ProjectionRepository projectionRepository;
	@Autowired
	private TicketRepository ticketRepository;
	@Autowired
	private CategorieRepository categorieRepository;
	
	@Override
	public void initVilles() {
		// TODO Auto-generated method stub
		Stream.of("Casablanca","Marrakesh","Rabat","Tanger").forEach(nameVille->{
		Ville ville =new Ville();
		ville.setName(nameVille);
		villeRepository.save(ville);
		});
	}

	@Override
	public void initCinemas() {
		// TODO Auto-generated method stub
		villeRepository.findAll().forEach(v->{
			Stream.of("Megarama","IMax","Founoun","ChAHrazad").forEach(nameCinema->{
				Cinema cinema=new Cinema();
				cinema.setName(nameCinema);
				cinema.setNombreSalles(10+(int)(Math.random()*10));
				cinema.setVille(v);
				cinemaRepository.save(cinema);
			});
		});
	}

	@Override
	public void initSalles() {
		// TODO Auto-generated method stub
		villeRepository.findAll().forEach(v->{
			v.getCinemas().forEach(cinema->{
				for(int i=0;i< cinema.getNombreSalles();i++) {
					Salle salle = new Salle();
					salle.setName("Salle "+(i+1));
					salle.setCinema(cinema);
					salle.setNombrePlace(15+(int)(Math.random()*30));
					salleRepository.save(salle);
				}
			});
		});
		
	}

	@Override
	public void initPlaces() {
		// TODO Auto-generated method stub
		salleRepository.findAll().forEach(salle->{
			for(int i=0;i<salle.getNombrePlace();i++) {
				Place place=new Place();
				place.setNumero(i+1);
				place.setSalle(salle);
				placeRepository.save(place);
			}
		});
		
	}

	@Override
	public void initSeances() {
		// TODO Auto-generated method stub
		DateFormat dateformat=new SimpleDateFormat("HH:mm");
		Stream.of("12:00","15:00","17:00","19:00","21:00").forEach(s->{
			Seance seance = new Seance();
			try {
				seance.setHeureDebut(dateformat.parse(s));
				seanceRepository.save(seance);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	@Override
	public void initCategories() {
		// TODO Auto-generated method stub
		Stream.of("Histoire","Actions","Fiction","Drama").forEach(cat->{
			Categorie categorie = new Categorie();
			categorie.setName(cat);
			categorieRepository.save(categorie);
		});
	}

	@Override
	public void initfilms() {
		double[] duree=new double[] {1,1.5,2.5,2,3};
		List<Categorie> categorie=categorieRepository.findAll();
		// TODO Auto-generated method stub
		Stream.of("Game of thrones","seigneur des anneaux","spider man","iron man","cat woman").forEach(fil->{
			Film film=new Film();
			film.setTitre(fil.replace(" ", ""));
			film.setDuree(duree[new Random().nextInt(duree.length)]);
			film.setPhoto(fil.replace(" ", "")+".jpg");
			film.setCategorie(categorie.get(new Random().nextInt(categorie.size())));
			filmRepository.save(film);
		});	 
	}

	@Override
	
	public void initProjections() {
		double[] prix=new double[] {30,50,60,70,90,100};
		List<Film> films=filmRepository.findAll();
		villeRepository.findAll().forEach(ville->{
			ville.getCinemas().forEach(cinema->{
				cinema.getSalles().forEach(salle->{
					int index=new Random().nextInt(films.size());
					//filmRepository.findAll().forEach(film->{
					Film film =films.get(index);
					seanceRepository.findAll().forEach(seance->{
						int year = 2020;
					
// generate a year between 1900 and 2010;
								int dayOfYear = randBetween(1,365);
// generate a number between 1 and 365 (or 366 if you need to handle leap year);
								Calendar calendar = Calendar.getInstance();
								calendar.set(Calendar.YEAR, year);
								calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
									
								Date randomDoB = calendar.getTime();
								
						Projection projection=new Projection();
						projection.setDateProjection(randomDoB);
						projection.setFilm(film);
						projection.setPrix(prix[new Random().nextInt(prix.length)]);
						projection.setSalle(salle);
						projection.setSeance(seance);
						projectionRepository.save(projection);
					});	
					//});
				});
			});
		});
		
	}
	@Override
	public void initTickets() {
		projectionRepository.findAll().forEach(p->{
			p.getSalle().getPlaces().forEach(place->{
				Ticket ticket =new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				ticket.setProjection(p);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
			});
		});
	}
	 public static int randBetween(int start, int end) {
	        return start + (int)Math.round(Math.random() * (end - start));
	    }
}
