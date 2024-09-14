package com.howtodoinjava.demo.data;

import com.howtodoinjava.demo.data.entity.PersonEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class PersonRepositoryTest {

  @Autowired
  private PersonRepository personRepository;

  private PersonEntity personEntity;

  @BeforeEach
  void setUp() {
    PersonEntity person = new PersonEntity(null, "John", "Doe", "john.doe@example.com");
    personEntity = personRepository.save(person);
  }

  @Test
  void testFindById() {
    Optional<PersonEntity> foundPerson = personRepository.findById(personEntity.getId());
    assertThat(foundPerson).isPresent();
    assertThat(foundPerson.get().getFirstName()).isEqualTo("John");
  }

  @Test
  void testSave() {
    PersonEntity newPerson = new PersonEntity(null, "Jane", "Doe", "jane.doe@example.com");
    personRepository.save(newPerson);
    assertThat(personRepository.findAll()).hasSize(2);
  }
}
