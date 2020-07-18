package org.sid.cinema.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.sid.cinema.entities.Salle;
import org.sid.cinema.dao.CategorieRepository;
import org.sid.cinema.dao.CinemaRepository;
import org.sid.cinema.dao.FilmRepository;
import org.sid.cinema.dao.ProjectionRepository;
import org.sid.cinema.dao.SalleRepository;
import org.sid.cinema.dao.VilleRepository;
import org.sid.cinema.entities.Categorie;
import org.sid.cinema.entities.Cinema;
import org.sid.cinema.entities.Film;
import org.sid.cinema.entities.Projection;
import org.sid.cinema.entities.Ville;
import org.sid.cinema.services.CinemaInitServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
@CrossOrigin("*")
@Controller
public class CinemaController {
	@Autowired
	FilmRepository filmRepository;
	@Autowired
	ProjectionRepository projectionRepository;
	@Autowired
	SalleRepository salleRepository;
	@Autowired
	CinemaInitServiceImpl cinemainit;
	@Autowired
	VilleRepository villeRepository;
	@Autowired
	CinemaRepository cinemaRepository;
	@Autowired
	CategorieRepository categorieRepository;

	// formulaire cinema
	@GetMapping(path = "/FormCinema")
	public String FormCinema(Model model, @RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "ville", defaultValue = "") String ville,
			@RequestParam(name = "nbr", defaultValue = "0") int nbr,
			@RequestParam(name = "longitude", defaultValue = "0") double longitude,
			@RequestParam(name = "latitude", defaultValue = "0") double latitude,
			@RequestParam(name = "altitude", defaultValue = "0") double altitude) {
		model.addAttribute("name", name);
		model.addAttribute("ville", ville);
		model.addAttribute("longitude", longitude);
		model.addAttribute("latitude", latitude);
		model.addAttribute("altitude", altitude);
		model.addAttribute("nbr", nbr);
		if (name != "" & ville != "" & nbr != 0) {
			return "redirect:/AddCinema?name=" + name + "&ville=" + ville + "&longitude=" + longitude + "&latitude="
					+ latitude + "&altitude=" + altitude + "&nbr=" + nbr;
		}
		return "/FormCinema";
	}

	// AddCinema
	@GetMapping(path = "/AddCinema")
	public String AddCinema(Model model, @RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "ville", defaultValue = "") String ville,
			@RequestParam(name = "nbr", defaultValue = "0") int nbr,
			@RequestParam(name = "longitude", defaultValue = "0") double longitude,
			@RequestParam(name = "latitude", defaultValue = "0") double latitude,
			@RequestParam(name = "altitude", defaultValue = "0") double altitude) {

		villeRepository.findByNameContains(ville).forEach(v -> {
			Cinema cinema = new Cinema();
			cinema.setName(name);
			cinema.setNombreSalles(nbr);
			cinema.setVille(v);
			cinema.setAltitude(altitude);
			cinema.setLatitude(latitude);
			cinema.setLongitude(longitude);
			cinemaRepository.save(cinema);
			model.addAttribute("name", name);
			model.addAttribute("ville", ville);
			model.addAttribute("longitude", longitude);
			model.addAttribute("latitude", latitude);
			model.addAttribute("altitude", altitude);
			model.addAttribute("nbr", nbr);

			for (int i = 0; i < nbr; i++) {
				Salle salle = new Salle();
				salle.setName("Salle " + (i + 1));
				salle.setNombrePlace(15 + (int) (Math.random() * 30));
				salle.setCinema(cinema);
				salleRepository.save(salle);
			}
		});

		return "/FormCinema";
	}

	// affichage de les Villes
	@GetMapping(path = "/ListVille")
	public String list(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "1") int size,
			@RequestParam(name = "keyword", defaultValue = "") String mc,
			@RequestParam(name = "keyAjout", defaultValue = "") String keyAjout) {
		Page<Ville> villes = villeRepository.findByNameContains(mc, PageRequest.of(page, size));
		model.addAttribute("ville", villes.getContent());
		model.addAttribute("pages", new int[villes.getTotalPages()]);
		model.addAttribute("currentpage", page);
		model.addAttribute("keyword", mc);
		model.addAttribute("keyAjout", keyAjout);
		return "ListVille";
	}

//suppression ville	
	@GetMapping(path = "/deleteVille")
	public String deleteVille(Long id, int page) {
		villeRepository.deleteById(id);
		return "redirect:/ListVille";
	}

	// Ajouter ville
	@GetMapping(path = "/AjouterVille")
	public String AjouterVille(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "keyAjout", defaultValue = "") String keyAjout,
			@RequestParam(name = "size", defaultValue = "1") int size) {

		if (!keyAjout.trim().isEmpty()) {
			Stream.of(keyAjout).forEach(nameVille -> {
				Ville ville = new Ville();
				ville.setName(nameVille);
				villeRepository.save(ville);

			});
		}

		// villeRepository.save(new
		// Ville(null,keyword,(double)Math.random()*50,(double)Math.random()*40,(double)Math.random()*60,));
		model.addAttribute("keyAjout", keyAjout);

		return "redirect:/ListVille?&keyAjout=" + keyAjout;
	}

	// Affichage de cinemas
	@GetMapping(path = "/AfficherCinemas")
	public String AfficherCinemas(Model model, @RequestParam(name = "KeyCinema", defaultValue = "") String KeyCinema,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "3") int size) {
		List<Ville> villes = villeRepository.findByNameContains(KeyCinema);
		villes.forEach(v -> {
			Collection<Cinema> cinemas = new ArrayList<Cinema>();
			cinemas = v.getCinemas();
			String villename = v.getName();
			model.addAttribute("villename", villename);
			model.addAttribute("cinemas", cinemas);
		});
		model.addAttribute("KeyCinema", KeyCinema);
		return "AfficherCinemas";
	}

	// affichage de les cinemas
	@GetMapping(path = "/ListCinema")
	public String listC(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "4") int size,
			@RequestParam(name = "keyword", defaultValue = "") String mc,
			@RequestParam(name = "keyAjout", defaultValue = "") String keyAjout,
			@RequestParam(name = "keyVille", defaultValue = "") String KeyVille) {

		Page<Cinema> cinemas = cinemaRepository.findByNameContains(mc, PageRequest.of(page, size));
		cinemas.forEach(c -> {
			Ville ville = new Ville();
			ville = c.getVille();
			String villename = ville.getName();
			model.addAttribute("villename", villename);
		});
		model.addAttribute("cinemas", cinemas.getContent());
		model.addAttribute("pages", new int[cinemas.getTotalPages()]);
		model.addAttribute("currentpage", page);
		model.addAttribute("keyword", mc);
		model.addAttribute("keyAjout", keyAjout);
		model.addAttribute("keyVille", KeyVille);

		return "AfficherCinemas";
	}

	// suppression ville
	@GetMapping(path = "/deleteCinema")
	public String deleteCinema(Long id,int page) {
		cinemaRepository.deleteById(id);
		return "redirect:/ListCinema";
	}

	// ajouter Cinema
	@GetMapping(path = "/AjouterCinema")
	public String AjouterCinema() {
		return "redirect:/FormCinema";
	}

	// SAlles
	@GetMapping(path = "/ListSalle")
	public String AfficherSalle(Model model, @RequestParam(name = "KeySalle", defaultValue = "") String KeySalle,
			@RequestParam(name = "KeyVille", defaultValue = "") String KeyVille,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "20") int size) {
		villeRepository.findAll().forEach(v -> {
			if (v.getName().equals(KeyVille)) {
				Collection<Salle> salle = new ArrayList<Salle>();
				v.getCinemas().forEach(c -> {
					if (c.getName().equals(KeySalle)) {
						salle.addAll(c.getSalles());

						model.addAttribute("salle", salle);
					}
				});

			}
		});
		model.addAttribute("KeyVille", KeyVille);
		return "ListSalle";
	}

	@GetMapping(path = "/deleteSalle")
	public String deleteSalle(Long id) {

		salleRepository.deleteById(id);
		return "redirect:/ListSalle";
	}

	@GetMapping(path = "/ListSalles")
	public String listSa(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "20") int size,
			@RequestParam(name = "keyword", defaultValue = "") String mc,
			@RequestParam(name = "keyAjout", defaultValue = "") String keyAjout) {
		Page<Salle> salle = salleRepository.findByNameContains(mc, PageRequest.of(page, size));
		model.addAttribute("salle", salle);
		model.addAttribute("pages", new int[salle.getTotalPages()]);
		model.addAttribute("currentpage", page);
		model.addAttribute("keyword", mc);
		model.addAttribute("keyAjout", keyAjout);
		return "ListSalle";
	}

	// Projection
	@GetMapping(path = "/ListProjection")
	public String listProjection(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "20") int size,
			@RequestParam(name = "keyword", defaultValue = "0.0") double keyword,
			@RequestParam(name = "keyFilm", defaultValue = "") String keyFilm) {

		if (keyword != 0.0) {
			Page<Projection> projection = projectionRepository.findByPrix(keyword, PageRequest.of(page, size));
			projection.forEach(p -> {
				DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
				Film film = p.getFilm();
				model.addAttribute("nomFilm", film.getTitre());
				model.addAttribute("currentpage", page);

				model.addAttribute("keyword", keyword);
				model.addAttribute("datep", dateformat.format(p.getDateProjection()));
				model.addAttribute("projection", projection.getContent());
				model.addAttribute("pages", new int[projection.getTotalPages()]);
			});
			/*
			 * }else if(!keydate.toString().equals("")) { Page<Projection> projection=
			 * projectionRepository.findBydateProjection(keydate,PageRequest.of(page,
			 * size)); projection.forEach(p->{
			 * model.addAttribute("projection",projection.getContent());
			 * model.addAttribute("pages", new int[projection.getTotalPages()]); DateFormat
			 * dateformat=new SimpleDateFormat("dd/MM/yyyy"); Film film=p.getFilm();
			 * model.addAttribute("nomFilm",film.getTitre());
			 * model.addAttribute("datep",dateformat.format(p.getDateProjection())); });
			 */
		} else if (!keyFilm.equals("")) {
			keyFilm = keyFilm.replaceAll(" ", "");

			System.out.println(keyFilm);
			model.addAttribute("keyFilm", keyFilm);
			Page<Film> films = filmRepository.findByTitreContains(keyFilm, PageRequest.of(page, size));

			films.forEach(f -> {
				Page<Projection> projection = projectionRepository.findByFilm(f, PageRequest.of(page, size));
				model.addAttribute("projection", projection.getContent());
				model.addAttribute("pages", new int[projection.getTotalPages()]);
				projection.forEach(p -> {
					DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
					Film film = p.getFilm();
					model.addAttribute("nomFilm", film.getTitre());
					model.addAttribute("currentpage", page);

					model.addAttribute("datep", dateformat.format(p.getDateProjection()));
				});
			});
			return "ListProjection";
		} else {
			Page<Projection> projection = projectionRepository.findAll(PageRequest.of(page, size));

			projection.forEach(p -> {
				DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
				Film film = p.getFilm();
				model.addAttribute("nomFilm", film.getTitre());
				model.addAttribute("currentpage", page);

				model.addAttribute("datep", dateformat.format(p.getDateProjection()));
				model.addAttribute("projection", projection.getContent());
				model.addAttribute("pages", new int[projection.getTotalPages()]);
			});
		}
		model.addAttribute("currentpage", page);

		return "ListProjection";
	}

	@GetMapping(path = "/chercherprix")
	public String chercherparPrix(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "20") int size,
			@RequestParam(name = "keyword", defaultValue = "0.0") double mc,
			@RequestParam(name = "keyFilm", defaultValue = "") String keyFilm) {
		if (mc != 0.0) {
			Page<Projection> projection = projectionRepository.findByPrix(mc, PageRequest.of(page, size));
			projection.forEach(p -> {
				DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
				Film film = p.getFilm();
				model.addAttribute("nomFilm", film.getTitre());
				model.addAttribute("currentpage", page);

				model.addAttribute("datep", dateformat.format(p.getDateProjection()));
				model.addAttribute("projection", projection.getContent());
				model.addAttribute("pages", new int[projection.getTotalPages()]);
			});
		}
		return "redirect:/ListProjection?keyword=" + mc;

	}

	@GetMapping(path = "/chercherfilm")
	public String chercherparFilm(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "20") int size,
			@RequestParam(name = "keyword", defaultValue = "0.0") double keyword,
			@RequestParam(name = "keyFilm", defaultValue = "") String keyFilm) {
		keyFilm = keyFilm.replaceAll(" ", "");
		if (!keyFilm.trim().equals("")) {
			System.out.println(keyFilm);
			model.addAttribute("keyFilm", keyFilm);
			Page<Film> films = filmRepository.findByTitreContains(keyFilm, PageRequest.of(page, size));
			films.forEach(f -> {
				Page<Projection> projection = projectionRepository.findByFilm(f, PageRequest.of(page, size));
				projection.forEach(p -> {
					DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
					Film film = p.getFilm();
					model.addAttribute("currentpage", page);

					model.addAttribute("nomFilm", film.getTitre());
					model.addAttribute("projection", projection.getContent());
					model.addAttribute("pages", new int[projection.getTotalPages()]);
					model.addAttribute("datep", dateformat.format(p.getDateProjection()));
				});
			});
		}
		return "redirect:/ListProjection?keyFilm=" + keyFilm;
	}

	@GetMapping(path = "/chercherdate")
	public String chercherparDate(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "20") int size,
			@RequestParam(name = "keyword", defaultValue = "0.0") double keyword,
			@RequestParam(name = "keyFilm", defaultValue = "") String keyFilm) {
		keyFilm = keyFilm.replaceAll(" ", "");
		if (!keyFilm.trim().equals("")) {
			System.out.println(keyFilm);
			model.addAttribute("keyFilm", keyFilm);
			Page<Film> films = filmRepository.findByTitreContains(keyFilm, PageRequest.of(page, size));
			films.forEach(f -> {
				Page<Projection> projection = projectionRepository.findByFilm(f, PageRequest.of(page, size));
				projection.forEach(p -> {
					DateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
					Film film = p.getFilm();
					model.addAttribute("nomFilm", film.getTitre());
					model.addAttribute("projection", projection.getContent());
					model.addAttribute("pages", new int[projection.getTotalPages()]);
					model.addAttribute("datep", dateformat.format(p.getDateProjection()));
				});
			});
		}
		return "redirect:/ListProjection?keyFilm=" + keyFilm;
	}

	// List Film
	@GetMapping(path = "/ListFilm")
	public String listfilm(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "20") int size,
			@RequestParam(name = "keyword", defaultValue = "") String keyword,
			@RequestParam(name = "keyFilm", defaultValue = "") String keyFilm) {
		Page<Film> films = filmRepository.findAll(PageRequest.of(page, size));
		films.forEach(f -> {
			Long id = f.getId();
			model.addAttribute("id", id);
		});
		model.addAttribute("currentpage", page);
		model.addAttribute("keyword", keyword);
		model.addAttribute("films", films.getContent());
		model.addAttribute("pages", new int[films.getTotalPages()]);
		return "ListFilm";
	}

	// Add Film
	@PostMapping(path = "/AddFilm", produces = MediaType.IMAGE_JPEG_VALUE)
	public String addFilm(Model model, @RequestParam(name = "keyajout", defaultValue = "20") String keyajout,
			@RequestParam("file") MultipartFile file) throws IOException {
		double[] duree = new double[] { 1, 1.5, 2.5, 2, 3 };
		Film film = new Film();
		// *****
		String str = System.getProperty("user.home") + "/Desktop/Media/imagej2e/";
		StringBuilder filename = new StringBuilder();
		Path filen = Paths.get(str, file.getOriginalFilename());
		filename.append(file.getOriginalFilename());
		Files.write(filen, file.getBytes());

		film.setTitre(keyajout);
		List<Categorie> categorie = categorieRepository.findAll();
		film.setDuree(duree[new Random().nextInt(duree.length)]);
		film.setPhoto(file.getOriginalFilename());
		film.setCategorie(categorie.get(new Random().nextInt(categorie.size())));
		filmRepository.save(film);
		return "redirect:/ListFilm";
	}

	// chercher Film
	@GetMapping(path = "/SearchFilm")
	public String chercherF(Model model, @RequestParam(name = "keyword", defaultValue = "") String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "20") int size) {

		Page<Film> films = filmRepository.findByTitreContains(keyword, PageRequest.of(page, size));
		films.forEach(f -> {
			Long id = f.getId();
			model.addAttribute("id", id);
		});
		model.addAttribute("currentpage", page);
		model.addAttribute("keyword", keyword);
		model.addAttribute("films", films.getContent());
		model.addAttribute("pages", new int[films.getTotalPages()]);
		return "ListFilm";
	}

	// suppression Film
	@GetMapping(path = "/deleteFilm")
	public String deleteFilm(Long id) {
		filmRepository.deleteById(id);
		return "redirect:/ListFilm";
	}

	// modifier ville
	@GetMapping(path = "/ModifierVille")
	public String Modifier(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "1") int size,
			
			@RequestParam(name = "name", defaultValue = "") String name,
			@RequestParam(name = "latitude", defaultValue = "0") double latitude,
			@RequestParam(name = "longitude", defaultValue = "0") double longitude,
			@RequestParam(name = "altitude", defaultValue = "0") double altitude) {
		Page<Ville> villes = villeRepository.findByNameContains(name, PageRequest.of(page, size));
		villes.forEach(v -> {
			if (altitude != 0)
				v.setAltitude(altitude);
			if (latitude != 0)
				v.setLatitude(latitude);
			if (longitude != 0)
				v.setLongitude(longitude);
			if (!name.equals(""))
				v.setName(name);
			villeRepository.save(v);
		});
		model.addAttribute("ville", villes.getContent());
		model.addAttribute("pages", new int[villes.getTotalPages()]);
		model.addAttribute("currentpage", page);
		
		model.addAttribute("name", name);
		model.addAttribute("longitude", longitude);
		model.addAttribute("latitude", latitude);
		model.addAttribute("altitude", altitude);
		return "FormVille";
	}
	// ajouter Cinema
		@GetMapping(path = "/403")
		public String A403() {
			return "403";
		}

}
