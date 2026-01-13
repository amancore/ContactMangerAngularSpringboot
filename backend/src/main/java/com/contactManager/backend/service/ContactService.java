package com.contactManager.backend.service;

import com.contactManager.backend.dto.ContactDto;

import java.util.List;

public interface ContactService {
    List<ContactDto> getAllContacts();
    ContactDto getContactById(Long id);
    ContactDto createContact(ContactDto contactDto);
    ContactDto updateContact(Long id, ContactDto contactDto);
    void deleteContact(Long id);
}
