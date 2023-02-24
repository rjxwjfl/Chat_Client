package Model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ChatRoomModel {
    private String title;
    private UserModel host;
    private List<UserModel> entry;
}
