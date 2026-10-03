package com.howtodoinjava.web.contacts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.hibernate.exception.ConstraintViolationException;

@DataJpaTest
@Import({ContactDao.class, ContactService.class})
class ContactDaoTest {

  @Autowired
  ContactDao contactDao;

  @Autowired
  ContactService contactService;

  @Autowired
  EntityManager em;

  @Test
  void findAllReturnsContactsOrderedByName() {
    assertThat(contactDao.findAll()).extracting(Contact::getName).containsExactly("Alex", "Lokesh");
  }

  @Test
  void savePersistsANewContactAndAssignsTheId() {
    Contact priya = contactDao.save(new Contact("Priya", "priya@example.com", "555-0103", "Pune"));
    assertThat(priya.getId()).isNotNull();
    em.flush();
    em.clear();
    assertThat(contactDao.findById(priya.getId())).get()
        .extracting(Contact::getCity).isEqualTo("Pune");
  }

  @Test
  void saveMergesADetachedContact() {
    Contact lokesh = contactDao.findAll().get(1);
    em.detach(lokesh);
    lokesh.setCity("Mumbai");
    Contact merged = contactDao.save(lokesh);
    assertThat(merged).isNotSameAs(lokesh);
    em.flush();
    em.clear();
    assertThat(contactDao.findById(lokesh.getId()).orElseThrow().getCity()).isEqualTo("Mumbai");
  }

  @Test
  void findByIdReturnsEmptyForUnknownId() {
    assertThat(contactDao.findById(999L)).isEmpty();
  }

  @Test
  void deleteRemovesTheRow() {
    Contact alex = contactDao.findAll().get(0);
    contactDao.delete(alex);
    em.flush();
    assertThat(contactDao.findAll()).extracting(Contact::getName).containsExactly("Lokesh");
  }

  @Test
  void existsByEmailIgnoresTheContactBeingEdited() {
    Long lokeshId = contactDao.findAll().get(1).getId();
    assertThat(contactDao.existsByEmail("lokesh@example.com", null)).isTrue();
    assertThat(contactDao.existsByEmail("lokesh@example.com", lokeshId)).isFalse();
    assertThat(contactDao.existsByEmail("nobody@example.com", null)).isFalse();
  }

  @Test
  void uniqueConstraintRejectsDuplicateEmailWithoutTheCheck() {
    // the @DataJpaTest slice has no PersistenceExceptionTranslationPostProcessor,
    // so the Hibernate exception is not translated here (see ContactControllerTest)
    assertThatThrownBy(() -> contactDao.save(new Contact("Copy", "alex@example.com", null, null)))
        .isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  void serviceUpdateNeedsNoSaveCall() {
    Long id = contactDao.findAll().get(1).getId();
    contactService.update(id, new Contact("Lokesh", "lokesh@example.com", "555-0199", "Delhi"));
    em.flush();
    em.clear();
    assertThat(contactDao.findById(id).orElseThrow().getPhone()).isEqualTo("555-0199");
  }

  @Autowired
  PlatformTransactionManager txManager;

  @Test
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  void readOnlyTransactionDoesNotWriteChanges() {
    TransactionTemplate readOnly = new TransactionTemplate(txManager);
    readOnly.setReadOnly(true);
    readOnly.executeWithoutResult(status -> contactDao.findAll().get(1).setCity("Paris"));

    TransactionTemplate write = new TransactionTemplate(txManager);
    String city = write.execute(status -> contactDao.findAll().get(1).getCity());
    assertThat(city).isEqualTo("Delhi");
  }

  @Test
  void serviceThrowsForUnknownId() {
    assertThatThrownBy(() -> contactService.findById(999L))
        .isInstanceOf(ContactNotFoundException.class)
        .hasMessage("Contact 999 not found");
  }
}
