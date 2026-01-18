package com.contactManager.backend.service.Impl;

import com.contactManager.backend.dto.ContactDto;
import com.contactManager.backend.entity.Contact;
import com.contactManager.backend.mapper.ModelMapper;
import com.contactManager.backend.repository.ContactRepository;
import com.contactManager.backend.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//creates its object auto (bean), controller can inject and use it
@RequiredArgsConstructor // lombok generates constructor auto for *final fields, no need to write the constructor manually
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    //db connector
    // provides method like (findAll(), findbyId(id), save(entity), deletebyId(id))
    // all are coming from the JpaRespository

    @Override
    public List<ContactDto> getAllContacts() {
        return contactRepository.findAll().stream().map(ModelMapper::toDto).toList();
    }
    // db fetch .findAll() -[select * from contact;]
    // output List<Contact> (entities)
    // convert entites into list Dto [.stream().map(ModelMapper::toDto())
    // each row of the *Contact -> converted into *ContactDto
    // Output : List<ContactDto>
    // Service returo Dto list to controller
    // controller returns its as JSON reponse to frontend

    @Override
    public ContactDto getContactById(Long id){
        Contact contact = contactRepository.findById(id).orElseThrow(()-> new RuntimeException("Contact Not Found"));
        return ModelMapper.toDto(contact);
    }
    // input : id , ex : GET/contact/5
    // id = 5, passed to the service
    // find the db : .findById(5), [select * from contact where id =5;]
    // return Optional<Contact> : [because maybe id exists or maybe not]
    // if not found .ElseThrow(...)
    // if exists : Convert entity(contact) -> Dto(ContactDto)
    // ModelMapper.toDto(contact)
    // return ContactDto

    @Override
    public ContactDto createContact(ContactDto dto){
        Contact contact = ModelMapper.toEntity(dto);
        return ModelMapper.toDto(contactRepository.save(contact));
    }
    // input : ContactDto dto
    // this comes from the api body : POST/contacts
    // json :  { "name" : "Aman" , "email" : "aman@gmail.com", "phone": "999"}
    // spring auto convert json->Dto
    // convert dto -> entity [Contact contact = ModelMapper.toEntity(dto);
    // because repository works with the entity not with the dto
    // save in the db, contactRepository.save(contact);
    // sql : [insert into contact(name,emai, phone), values("aman"...);
    // db generates ID auto (because @generated value)
    // so now entity becomes : Contact(id=10, name="Aman"...)
    // now save contact updated :
    // convert saved Entity -> Dto
    // ModelMapper.toDto(savedEntity) , frontend gets back Dto including id
    // return type ContactDto :
    // {
    //  "id": 10,
    //  "name": "Aman",
    //  "email": "aman@gmail.com",
    //  "phone": "99999"
    //}

    @Override
    public ContactDto updateContact(Long id, ContactDto contactDto) {
        Contact existing = contactRepository.findById(id).orElseThrow(()-> new RuntimeException("Contact not found" + id));
        existing.setName(contactDto.getName());
        existing.setEmail(contactDto.getEmail());
        existing.setPhone(contactDto.getPhone());
        Contact updated = contactRepository.save(existing);
        return ModelMapper.toDto(updated);
    }
    // input : LONG id, ContactDto (from request body)
    // PUT/contacts/10
    // json : {
    //  "name": "Aman Raj",
    //  "email": "amanraj@gmail.com",
    //  "phone": "88888"
    //}
    // check record exits : contactRepository.findById(id); [select * from contact where id = 10;]
    // if not found then error
    // object exists has updated values
    // save again : contactRepository.save(existing)
    //UPDATE contact
    //SET name="Aman Raj",
    //    email="amanraj@gmail.com",
    //    phone="88888"
    //WHERE id=10;

    // now convert entity -> dto; [modelMapper.toDto(updated)
    // return type ContactDto



    @Override
    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
}
