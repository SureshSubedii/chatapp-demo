package com.example.chatapp.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDataDto {
    private int to;
    private String message;
    private int from;


}

