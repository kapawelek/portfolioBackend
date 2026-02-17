package portfolio.owners;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.service.ClinicServiceImpl;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PortfolioOwnerServiceTest {

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private ClinicServiceImpl clinicService;

    @Test
    void shouldFindOwnerById() {
        Owner owner = new Owner();
        owner.setId(1);
        owner.setLastName("Kowalski");

        given(ownerRepository.findById(1)).willReturn(owner);

        Owner foundOwner = clinicService.findOwnerById(1);

        assertThat(foundOwner).isNotNull();
        assertThat(foundOwner.getLastName()).isEqualTo("Kowalski");
        verify(ownerRepository, times(1)).findById(1);
    }

    @Test
    void shouldSaveOwner() {
        Owner owner = new Owner();
        owner.setLastName("Nowak");

        clinicService.saveOwner(owner);

        verify(ownerRepository, times(1)).save(owner);
    }

    @Test
    void shouldFindOwnersByLastName() {
        Owner owner1 = new Owner();
        owner1.setLastName("Smith");
        Owner owner2 = new Owner();
        owner2.setLastName("Smith");

        List<Owner> owners = List.of(owner1, owner2);

        given(ownerRepository.findByLastName("Smith")).willReturn(owners);

        Collection<Owner> foundOwners = clinicService.findOwnerByLastName("Smith");

        assertThat(foundOwners).hasSize(2);
        verify(ownerRepository).findByLastName("Smith");
    }
}
