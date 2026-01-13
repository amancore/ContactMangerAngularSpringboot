package com.contactManager.backend.mapper;

import com.contactManager.backend.dto.ContactDto;
import com.contactManager.backend.entity.Contact;

public class ModelMapper {
    public static ContactDto toDto(Contact contact){
        if(contact==null) return null;
        return new ContactDto(
                contact.getId(),
                contact.getName(),
                contact.getEmail(),
                contact.getPhone()
        );
    }
    public static Contact toEntity(ContactDto dto){
        if(dto==null) return null;
        return new Contact(
                null,
                dto.getName(),
                dto.getEmail(),
                dto.getPhone()
        );
    }
}
