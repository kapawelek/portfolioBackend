package portfolio.owners;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.samples.petclinic.mapper.OwnerMapper;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.mapper.VisitMapper;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.rest.controller.OwnerRestController;
import org.springframework.samples.petclinic.rest.dto.OwnerDto;
import org.springframework.samples.petclinic.rest.dto.OwnerFieldsDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OwnerRestController.class)
@ContextConfiguration(classes = PetClinicApplication.class)
class PortfolioOwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClinicService clinicService;

    @MockitoBean
    private OwnerMapper ownerMapper;

    @MockitoBean
    private PetMapper petMapper;

    @MockitoBean
    private VisitMapper visitMapper;

    @Test
    void shouldReturnOwnerList() throws Exception {
        Owner owner = new Owner();
        owner.setId(1);
        owner.setFirstName("Jan");
        owner.setLastName("Kowalski");

        given(clinicService.findAllOwners()).willReturn(List.of(owner));

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setFirstName("Jan");
        ownerDto.setLastName("Kowalski");

        given(ownerMapper.toOwnerDtoCollection(any())).willReturn(List.of(ownerDto));

        mockMvc.perform(get("/api/owners")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].firstName").value("Jan"));
    }

    @Test
    void shouldCreateOwner() throws Exception {
        OwnerDto inputDto = new OwnerDto();
        inputDto.setFirstName("Nowy");
        inputDto.setLastName("Wlasciciel");
        inputDto.setAddress("Ulica 1");
        inputDto.setCity("Miasto");
        inputDto.setTelephone("1234567890");

        Owner owner = new Owner();
        owner.setId(1);

        OwnerDto outputDto = new OwnerDto();
        outputDto.setId(1);
        outputDto.setFirstName("Nowy");
        outputDto.setLastName("Wlasciciel");

        given(this.ownerMapper.toOwner(any(OwnerFieldsDto.class))).willReturn(owner);
        given(this.ownerMapper.toOwnerDto(any(Owner.class))).willReturn(outputDto);

        mockMvc.perform(post("/api/owners")
                .content(objectMapper.writeValueAsString(inputDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstName").value("Nowy"))
            .andExpect(jsonPath("$.lastName").value("Wlasciciel"))
            .andExpect(jsonPath("$.id").value(1));
    }
}
