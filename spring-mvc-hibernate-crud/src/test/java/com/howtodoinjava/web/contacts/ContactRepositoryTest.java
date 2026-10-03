package com.howtodoinjava.web.contacts;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class ContactRepositoryTest {

  @Autowired
  ContactRepository contactRepository;

  @Test
  void derivedQueriesWork() {
    assertThat(contactRepository.findAllByOrderByNameAsc())
        .extracting(Contact::getName).containsExactly("Alex", "Lokesh");
    assertThat(contactRepository.existsByEmail("alex@example.com")).isTrue();
  }

  @Test
  void saveAndDeleteById() {
    Contact priya = contactRepository.save(new Contact("Priya", "priya@example.com", "555-0103", "Pune"));
    assertThat(priya.getId()).isNotNull();
    assertThat(contactRepository.count()).isEqualTo(3);
    contactRepository.deleteById(priya.getId());
    assertThat(contactRepository.count()).isEqualTo(2);
  }
}
