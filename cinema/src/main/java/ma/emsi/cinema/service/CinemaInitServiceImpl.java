package ma.emsi.cinema.service;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import ma.emsi.cinema.dao.CategorieRepository;
import ma.emsi.cinema.dao.CinemaRepository;
import ma.emsi.cinema.dao.FilmRepository;
import ma.emsi.cinema.dao.PlaceRepository;
import ma.emsi.cinema.dao.ProjectionRepository;
import ma.emsi.cinema.dao.SalleRepository;
import ma.emsi.cinema.dao.SeanceRepository;
import ma.emsi.cinema.dao.TicketRepository;
import ma.emsi.cinema.dao.VilleRepository;
import ma.emsi.cinema.entities.Categorie;
import ma.emsi.cinema.entities.Cinema;
import ma.emsi.cinema.entities.Film;
import ma.emsi.cinema.entities.Place;
import ma.emsi.cinema.entities.Projection;
import ma.emsi.cinema.entities.Salle;
import ma.emsi.cinema.entities.Seance;
import ma.emsi.cinema.entities.Ticket;
import ma.emsi.cinema.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService{
	
	@Autowired
	private VilleRepository villeRepository;
	@Autowired
	private CinemaRepository cinemaRepository;
	@Autowired
	private SalleRepository salleRepository;
	@Autowired
	private PlaceRepository placeRepository;
	@Autowired
	private CategorieRepository categorieRepository;
	@Autowired
	private ProjectionRepository projectionRepository;
	@Autowired
	private SeanceRepository seanceRepository;
	@Autowired
	private TicketRepository ticketRepository;
	@Autowired
	private FilmRepository filmRepository;
	//-------------------------------------------------------------------------------
	@Override
	public void initVilles() {
		Stream.of("Casablanca","Marrakech","Rabat","Tanger").forEach(nameVille->{
			Ville  ville=new Ville();
			ville.setName(nameVille);
			villeRepository.save(ville);
		});
		
	}

	@Override
	public void initCinemas() {
		villeRepository.findAll().forEach(v->{
			Stream.of("MegaRama","IMax","FOUNOUN","CHAHRAZAD","DAOULIZ").forEach(nameCinema->{
				Cinema cinema = new Cinema();
				cinema.setName(nameCinema);
				cinema.setNombreSalles(3+(int)(Math.random()*7));
				cinema.setVille(v);
				cinemaRepository.save(cinema);
			});
		});
		
	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema->{
			for (int i = 0; i<cinema.getNombreSalles(); i++) {
				Salle salle = new Salle();
				salle.setName("Salle "+(i+1));
				salle.setCinema(cinema);
				salle.setNombrePlaces(15+(int)(Math.random()*20));
				salleRepository.save(salle);
			}
		});
		
	}

	@Override
	public void initPlaces() {
		salleRepository.findAll().forEach(salle->{
			for(int i = 0; i<salle.getNombrePlaces(); i++) {
				Place place = new Place();
				place.setNumero(i+1);
				place.setSalle(salle);
				placeRepository.save(place);
			}
		});
		
	}

	@Override
	public void initSeances() {
		DateFormat dateForma = new SimpleDateFormat("HH:mm");
		Stream.of("12:00", "15:00", "17:00", "19:00", "21:00").forEach(s->{
			Seance seance = new Seance();
			try {
				seance.setHeureDebut(dateForma.parse(s));
				seanceRepository.save(seance);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
	}

	@Override
	public void initCategories() {
		Stream.of("Histoire", "Actions", "Fiction", "Drama").forEach(cat->{
			Categorie categorie = new Categorie();
			categorie.setName(cat);
			categorieRepository.save(categorie);
		});
		
	}

	@Override
	public void initFilms() {
		double[] durees = new double[] {1, 1.5, 2, 2.5, 3};
		List<Categorie> categories = categorieRepository.findAll();
		Stream.of("GOT", "Vikings", "Triangle", "Happy deathday").forEach(titreFilm->{
			Film film = new Film();
			film.setTitre(titreFilm);
			film.setDuree(durees[new Random().nextInt(durees.length)]);
			film.setPhoto(titreFilm.replaceAll(" ", "")+".jpg");
			film.setCategorie(categories.get(new Random().nextInt(categories.size())));
			filmRepository.save(film);
		});
		
	}

	@Override
	public void initProjections() {
		double[] prices = new double[] {30, 50, 60, 70, 90, 100};
		villeRepository.findAll().forEach(ville->{
			ville.getCinemas().forEach(cinema->{
				cinema.getSalles().forEach(salle->{
					filmRepository.findAll().forEach(film->{
						seanceRepository.findAll().forEach(seance->{
							Projection projection = new Projection();
							projection.setDateProjection(new Date());
							projection.setFilm(film);
							projection.setPrix(prices[new Random().nextInt(prices.length)]);
							projection.setSalle(salle);
							projection.setSeance(seance);
							projectionRepository.save(projection);
						});
					});
				});
			});
		});
	}

	@Override
	public void initTickets() {
		projectionRepository.findAll().forEach(p->{
			p.getSalle().getPlaces().forEach(place->{
				Ticket ticket = new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				ticket.setProjection(p);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
			});
		});
	}

}
