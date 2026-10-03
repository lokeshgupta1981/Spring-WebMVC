package com.howtodoinjava.web.contacts;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ContactService {

  private final ContactDao contactDao;

  public ContactService(ContactDao contactDao) {
    this.contactDao = contactDao;
  }

  public List<Contact> findAll() {
    return contactDao.findAll();
  }

  public Contact findById(Long id) {
    return contactDao.findById(id).orElseThrow(() -> new ContactNotFoundException(id));
  }

  @Transactional
  public Contact create(Contact contact) {
    if (contactDao.existsByEmail(contact.getEmail(), null)) {
      throw new DuplicateEmailException(contact.getEmail());
    }
    return contactDao.save(contact);
  }

  @Transactional
  public Contact update(Long id, Contact form) {
    Contact contact = findById(id);
    if (contactDao.existsByEmail(form.getEmail(), id)) {
      throw new DuplicateEmailException(form.getEmail());
    }
    contact.setName(form.getName());
    contact.setEmail(form.getEmail());
    contact.setPhone(form.getPhone());
    contact.setCity(form.getCity());
    return contact;   // managed entity: Hibernate runs the UPDATE at commit
  }

  @Transactional
  public void delete(Long id) {
    contactDao.delete(findById(id));
  }
}
