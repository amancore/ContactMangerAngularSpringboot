package com.contactManager.backend.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContactDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
}
