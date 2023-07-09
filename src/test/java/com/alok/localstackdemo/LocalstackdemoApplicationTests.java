package com.alok.localstackdemo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SuppressWarnings({"rawtypes", "OctalInteger"})
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class LocalstackdemoApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Container
	private static LocalStackContainer localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
			// to create secrets on startup
			.withCopyFileToContainer(MountableFile.forClasspathResource("script.sh",0775),
					"/etc/localstack/init/ready.d/")
			.withServices(LocalStackContainer.Service.SECRETSMANAGER);

	static {
		localStackContainer.start();
	}

	@Container
	private static MySQLContainer database = new MySQLContainer(DockerImageName.parse("mysql/mysql-server:5.7").asCompatibleSubstituteFor("mysql"))
			.withDatabaseName("database")
			.withUsername("user1")
			.withPassword("password");

	static {
		database.start();
	}

	@BeforeAll
	static void beforeAll() {
		System.setProperty("spring.cloud.aws.secretsmanager.endpoint", localStackContainer.getEndpointOverride(LocalStackContainer.Service.SECRETSMANAGER).toString());
		System.setProperty("spring.cloud.aws.secretsmanager.region", localStackContainer.getRegion());
		System.setProperty("spring.cloud.aws.credentials.access-key", "none");
		System.setProperty("spring.cloud.aws.credentials.secret-key", "none");
	}

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", () -> database.getJdbcUrl());
	}

	@Test
	void testApplicationLoadsAndServesRequests() throws Exception {
		//Given
		Model model = new Model();
		model.setId(1);
		model.setName("name");

		//when
		mockMvc.perform(MockMvcRequestBuilders.post("/")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(model)))
				//then
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("name"));

		//when
		mockMvc.perform(MockMvcRequestBuilders.get("/"))
				//then
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.[0].id").value(1))
				.andExpect(jsonPath("$.[0].name").value("name"));
	}

}
