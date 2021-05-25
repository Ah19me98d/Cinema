package ma.emsi.cinema.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import ma.emsi.cinema.dao.FilmRepository;
import ma.emsi.cinema.dao.TicketRepository;
import ma.emsi.cinema.entities.Film;
import ma.emsi.cinema.entities.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

@RestController
public class CinemaRestController {
	@Autowired
	private FilmRepository filmRepository ;
	@Autowired
	private TicketRepository ticketRepositorie;
	@GetMapping(path = "/imageFilm/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] image(@PathVariable(name="id")Long id) throws IOException {
		Film f = filmRepository.findById(id).get();
		String photoName=f.getPhoto();
		File file = new File(System.getProperty("user.home")+"/cinema/images/"+photoName);
		Path path = Paths.get(file.toURI());
		return Files.readAllBytes(path);
	}
	@PostMapping("/payerTickets")
	@Transactional
	public List<Ticket> payerTickets(@RequestBody TicketForm ticketForm){
		List<Ticket> listTickets = new ArrayList<Ticket>();
		ticketForm.getTickets().forEach(idTicket->{
			//System.out.println(idTicket);
			Ticket ticket = ticketRepositorie.findById(idTicket).get();
			ticket.setNomClient(ticketForm.getNomClient());
			ticket.setReserve(true);
			ticket.setCodePayement(ticketForm.getCodePayement());
			ticketRepositorie.save(ticket);
			listTickets.add(ticket);
		});
		return listTickets;
	}
}
@Data
class TicketForm{
	private String nomClient;
	private int codePayement;
	private List<Long> tickets = new ArrayList<>();
}