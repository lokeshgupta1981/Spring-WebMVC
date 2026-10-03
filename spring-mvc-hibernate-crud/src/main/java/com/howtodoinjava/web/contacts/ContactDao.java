package com.howtodoinjava.web.contacts;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

/**
 * Plain JPA/Hibernate DAO: every call goes through the EntityManager.
 * Transactions are started by the service layer.
 */
@Repository
public class ContactDao {

  @PersistenceContext
  private EntityManager em;

  public List<Contact> findAll() {
    return em.createQuery("select c from Contact c order by c.name", Contact.class)
        .getResultList();
  }

  public Optional<Contact> findById(Long id) {
    return Optional.ofNullable(em.find(Contact.class, id));
  }

  public Contact save(Contact contact) {
    if (contact.getId() == null) {
      em.persist(contact);
      return contact;
    }
    return em.merge(contact);
  }

  public void delete(Contact contact) {
    em.remove(contact);
  }

  public boolean existsByEmail(String email, Long excludeId) {
    return em.createQuery(
            "select count(c) from Contact c where c.email = :email and (:id is null or c.id <> :id)",
            Long.class)
        .setParameter("email", email)
        .setParameter("id", excludeId)
        .getSingleResult() > 0;
  }
}
