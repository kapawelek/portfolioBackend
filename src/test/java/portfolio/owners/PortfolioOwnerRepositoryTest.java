package portfolio.owners;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = PetClinicApplication.class)
@TestPropertySource(properties = {
    "spring.sql.init.mode=never",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class PortfolioOwnerRepositoryTest {

    @Autowired
    private OwnerRepository ownerRepository;

    @Test
    void shouldSaveAndRetrieveOwner() {
        Owner owner = new Owner();
        owner.setFirstName("Anna");
        owner.setLastName("Nowak");
        owner.setAddress("Kwiatowa 15");
        owner.setCity("Warszawa");
        owner.setTelephone("1234567890");

        ownerRepository.save(owner);

        Collection<Owner> foundOwners = ownerRepository.findByLastName("Nowak");

        assertThat(foundOwners).isNotEmpty();
        Owner foundOwner = foundOwners.iterator().next();

        assertThat(foundOwner.getId()).isNotNull();
        assertThat(foundOwner.getFirstName()).isEqualTo("Anna");
        assertThat(foundOwner.getCity()).isEqualTo("Warszawa");
    }

    @Test
    void shouldUpdateOwner() {
        Owner owner = new Owner();
        owner.setFirstName("Marek");
        owner.setLastName("Edytowany");
        owner.setAddress("Stara Ulica 1");
        owner.setCity("Warszawa");
        owner.setTelephone("1234567890");
        ownerRepository.save(owner);

        Owner savedOwner = ownerRepository.findByLastName("Edytowany").stream().findFirst().orElseThrow();
        savedOwner.setAddress("Nowa Ulica 99");
        ownerRepository.save(savedOwner);

        Owner updatedOwner = ownerRepository.findByLastName("Edytowany").stream().findFirst().orElseThrow();
        assertThat(updatedOwner.getAddress()).isEqualTo("Nowa Ulica 99");
    }
}
