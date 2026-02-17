package portfolio.owners;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.samples.petclinic.model.Owner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PortfolioOwnerValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldValidateCorrectOwner() {
        Owner owner = new Owner();
        owner.setFirstName("Jan");
        owner.setLastName("Kowalski");
        owner.setAddress("Prosta 1");
        owner.setCity("Warszawa");
        owner.setTelephone("1234567890");

        Set<ConstraintViolation<Owner>> violations = validator.validate(owner);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailWhenFirstNameIsEmpty() {
        Owner owner = new Owner();
        owner.setFirstName("");
        owner.setLastName("Kowalski");
        owner.setAddress("Prosta 1");
        owner.setCity("Warszawa");
        owner.setTelephone("1234567890");

        Set<ConstraintViolation<Owner>> violations = validator.validate(owner);

        assertThat(violations).isNotEmpty();

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @ParameterizedTest(name = "Telephone validation should fail for: {0}")
    @ValueSource(strings = {
        "123",
        "123456789012",
        "telefon",
        "",
        "123-456-789"
    })
    void shouldFailForInvalidTelephone(String invalidPhone) {
        Owner owner = new Owner();
        owner.setFirstName("Jan");
        owner.setLastName("Kowalski");
        owner.setAddress("Prosta 1");
        owner.setCity("Warszawa");
        owner.setTelephone(invalidPhone);

        Set<ConstraintViolation<Owner>> violations = validator.validate(owner);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("telephone"));
    }
}
