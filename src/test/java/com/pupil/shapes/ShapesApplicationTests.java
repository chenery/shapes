package com.pupil.shapes;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import com.pupil.shapes.model.dto.CreateShapeCommand;
import com.pupil.shapes.model.dto.ShapeResponse;
import com.pupil.shapes.service.ShapeRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ShapesApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ShapeRepository shapeRepository;

	private static MariaDB4jSpringService DB;

	@BeforeAll
	// todo move this to the regular app context
	public static void init() throws ManagedProcessException {
		DB = new MariaDB4jSpringService();
		DB.setDefaultPort(3307);
		DB.start();
		DB.getDB().createDB("shapesdb");
	}

	@BeforeEach
	public void before() {
		shapeRepository.findAll().forEach(shapeRepository::delete);
	}

	@AfterAll
	public static void cleanup() {
//		if (DB != null) DB.stop();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void createShapeAndFind() {
		// GIVEN a new shape command
		String squareWkt = "POLYGON ((10 10, 10 40, 40 40, 40 10, 10 10))";
		CreateShapeCommand createShapeCommand = new CreateShapeCommand("square-1", "square", squareWkt);

		// WHEN created
		ResponseEntity<ShapeResponse> response = restTemplate.postForEntity(
				"http://localhost:" + port + "/shapes", createShapeCommand , ShapeResponse.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(201);
		ShapeResponse createResponse = response.getBody();

		// THEN response looks OK
		assertThat(createResponse).isNotNull();
		assertThat(createResponse.getId()).isNotNull();
		assertThat(createResponse.getWktGeometry()).isEqualTo(squareWkt);

		// AND new shape is found via GET endpoint
		ResponseEntity<ShapeResponse[]> getResp = restTemplate.getForEntity("http://localhost:" + port + "/shapes", ShapeResponse[].class);
		assertThat(getResp.getStatusCodeValue()).isEqualTo(200);
		ShapeResponse[] persistedShapes = getResp.getBody();
		assertThat(persistedShapes).contains(createResponse).hasSize(1);
	}

	@Test
	void createShapeFailedDueToNonUniqueName() {
		// GIVEN an existing shape
		String squareWkt = "POLYGON ((10 10, 10 40, 40 40, 40 10, 10 10))";
		CreateShapeCommand createShapeCommand = new CreateShapeCommand("square-1", "square", squareWkt);

		ResponseEntity<ShapeResponse> response = restTemplate.postForEntity(
				"http://localhost:" + port + "/shapes", createShapeCommand, ShapeResponse.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(201);
		ShapeResponse createResponse = response.getBody();

		assertThat(createResponse).isNotNull();
		assertThat(createResponse.getId()).isNotNull();
		assertThat(createResponse.getWktGeometry()).isEqualTo(squareWkt);

		// WHEN shape with same name is created
		CreateShapeCommand createShapeCommand2 = new CreateShapeCommand("square-1", "square", squareWkt);
		ResponseEntity<Map> response2 = restTemplate.postForEntity(
				"http://localhost:" + port + "/shapes", createShapeCommand2, Map.class);

		// THEN the create operation fails with a 422 response
		assertThat(response2.getStatusCodeValue()).isEqualTo(422);
		Map createResponse2 = response2.getBody();
		assertThat(createResponse2).isNotNull();
		assertThat((String) createResponse2.get("message")).isEqualTo("Non Unique Name");
	}

	@Test
	void createShapeFailedDueToOverlap() {
		// GIVEN an existing shape
		String squareWkt = "POLYGON ((10 10, 10 40, 40 40, 40 10, 10 10))";
		CreateShapeCommand createShapeCommand = new CreateShapeCommand("square-1", "square", squareWkt);

		ResponseEntity<ShapeResponse> response = restTemplate.postForEntity(
				"http://localhost:" + port + "/shapes", createShapeCommand , ShapeResponse.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(201);
		ShapeResponse createResponse = response.getBody();

		assertThat(createResponse).isNotNull();
		assertThat(createResponse.getId()).isNotNull();
		assertThat(createResponse.getWktGeometry()).isEqualTo(squareWkt);

		// WHEN an overlapping shape is created
		String squareWkt2 = "POLYGON ((20 10, 20 40, 50 40, 50 10, 20 10))";
		CreateShapeCommand createShapeCommand2 = new CreateShapeCommand("square-2", "square", squareWkt2);
		ResponseEntity<Map> response2 = restTemplate.postForEntity(
				"http://localhost:" + port + "/shapes", createShapeCommand2 , Map.class);

		// THEN the create operation fails with a 422 response
		assertThat(response2.getStatusCodeValue()).isEqualTo(422);
		Map createResponse2 = response2.getBody();
		assertThat(createResponse2).isNotNull();
		assertThat((String) createResponse2.get("message")).isEqualTo("Overlapping Shape");
	}

	// todo test same shape, different name twice

}
