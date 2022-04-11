package com.company.entity;

import com.company.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Users {
    private String phoneNumber;
    private String messageText;
    private Status status;

}
