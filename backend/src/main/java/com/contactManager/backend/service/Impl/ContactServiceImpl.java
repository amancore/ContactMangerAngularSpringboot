package com.contactManager.backend.service.Impl;

import com.contactManager.backend.dto.ContactDto;
import com.contactManager.backend.entity.Contact;
import com.contactManager.backend.mapper.ModelMapper;
import com.contactManager.backend.repository.ContactRepository;
import com.contactManager.backend.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    @Override
    public List<ContactDto> getAllContacts() {
        return contactRepository.findAll().stream().map(ModelMapper::toDto).toList();
    }

    @Override
    public ContactDto getContactById(Long id){
        Contact contact = contactRepository.findById(id).orElseThrow(()-> new RuntimeException("Contact Not Found"));
        return ModelMapper.toDto(contact);
    }
    @Override
    public ContactDto createContact(ContactDto dto){
        Contact contact = ModelMapper.toEntity(dto);
        return ModelMapper.toDto(contactRepository.save(contact));
    }

    @Override
    public ContactDto updateContact(Long id, ContactDto contactDto) {
        Contact existing = contactRepository.findById(id).orElseThrow(()-> new RuntimeException("Contact not found" + id));
        existing.setName(contactDto.getName());
        existing.setEmail(contactDto.getEmail());
        existing.setPhone(contactDto.getPhone());
        Contact updated = contactRepository.save(existing);
        return ModelMapper.toDto(updated);
    }

    @Override
    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
}
