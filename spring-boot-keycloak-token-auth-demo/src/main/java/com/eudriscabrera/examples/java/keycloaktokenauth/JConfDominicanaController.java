package com.eudriscabrera.examples.java.keycloaktokenauth;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ecabrerar
 * @since Apr 29, 2020
 */
@RestController
public class JConfDominicanaController {

	@GetMapping(value = "/jconfdominicana/sessions", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Session> desconferencias(Principal principal) {
		return new SessionResource().getSessions();

	}

	@GetMapping(value = "/jconfdominicana/speakers", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> desconferencistas(Principal principal) {
		return new SessionResource().getSessions().stream().map(Session::getSpeaker).collect(Collectors.toList());
	}

	@GetMapping(value = "/jconfdominicana/sessions/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Session> getSpeaker(@PathVariable("sessionId") String sessionId, Principal principal) {
		return new SessionResource().getSessions().stream()
				.filter(session -> session.getId() == Integer.parseInt(sessionId)).map(session -> session)
				.peek(session -> System.out.println(session)).collect(Collectors.toList());
	}

	static class SessionResource {

		private List<Session> getSessions() {

			List<Session> sessions = new ArrayList<>();
			sessions.add(new Session(101, "The Future of Java and You", "Heather VanCura", "8:30 AM"));
			sessions.add(new Session(102, "¿Java EE? ¿Jakarta EE? ¿MicroProfile? ¿O tal vez, todos ellos?",
					"Sebastian Daschner", "9:30 AM"));
			sessions.add(new Session(103,
					"Bebiendo del Stream: Como usar las plataformas de mensajería para escalamiento y rendimiento",
					"Mark Heckler", "10:30 AM"));
			sessions.add(new Session(104, "The Bootiful Application", "Josh Long", "2:30 PM"));
			sessions.add(new Session(105, "RSocket - new Reactive cross-network Protocol", "Oleh Dokuka", "2:30 PM"));
			sessions.add(
					new Session(106, "Unleashing Kotlin's power: A talk for Java developers", "Eury Perez", "2:30 PM"));
			sessions.add(new Session(107, "Plataformas, herramientas y librerías políglotas en plataformas Java",
					"Eliezer Herrera", "2:30 PM"));
			sessions.add(new Session(108,
					"Build, Test, Ship, Repeat: Effective testing and delivery using Continuous Integration",
					"Carlos Paulino", "03:30 PM"));
			sessions.add(new Session(109, "Continuous code quality in java projects", "Igor Suhorukov", "03:30 PM"));
			sessions.add(new Session(110, "Desarrollar Lenguajes DSL (Domain Specific Language) con Antlr",
					"Clivens Petit", "03:30 PM"));
			sessions.add(new Session(111, "Serverless con Openfaas y Java", " Carlos Camacho", "03:30 PM"));
			sessions.add(new Session(112, "Creando un webservice rapidamente sin multiples endpoints",
					"Omar Berroterán", "04:30 PM"));
			sessions.add(new Session(113, "The Reactive Revolution", "Josh Long", "04:30 PM"));
			sessions.add(
					new Session(114, "Dockerizing Java applications with Oracle Cloud", "Hilmer Chona", "04:30 PM"));
			sessions.add(new Session(115, "Pruebas de Integración para Servicios REST usando REST Assured",
					"Eudris Cabrera", "04:30 PM"));
			sessions.add(new Session(116, "Java is back to the browser thanks to WebAssembly!", "Miguel Useche",
					"05:30 PM"));
			sessions.add(new Session(117, "Cloud Native, Service-Meshed Java Enterprise With Istio",
					"Sebastian Daschner", "05:30 PM"));
			sessions.add(new Session(118, "Emulate Amazon Web Services infrastructure in single JMV process to reduce",
					"Igor Suhorukov", "05:30 PM"));
			sessions.add(new Session(119, "Paralelismo en java para todos", "Aluis Marte", "05:30 PM"));

			return sessions;
		}
	}
}
