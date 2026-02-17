package portfolio.owners;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.samples.petclinic.rest.dto.OwnerDto;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration(classes = PetClinicApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.sql.init.mode=never",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PortfolioOwnerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndRetrieveOwner() throws Exception {
        OwnerDto newOwner = new OwnerDto();
        newOwner.setFirstName("Integracyjny");
        newOwner.setLastName("Testowy");
        newOwner.setAddress("Polna 123");
        newOwner.setCity("Krakow");
        newOwner.setTelephone("9876543210");

        mockMvc.perform(post("/api/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOwner)))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/api/owners")
                .param("lastName", "Testowy")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].firstName").value("Integracyjny"))
            .andExpect(jsonPath("$.[0].telephone").value("9876543210"));
    }
}
