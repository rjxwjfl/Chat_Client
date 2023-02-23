package src.Model.DataTransferObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Dto<T> {
    private int transferCode;
    private T body;
}

/*

* CODE
    1 - CONNECT
    2 - DISCONNECT
    3 - JOINING CHATROOM
    4 - LEFT CHATROOM
    5 - SEND MESSAGE
    6 - RECEIVE MESSAGE

*/