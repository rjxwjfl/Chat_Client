package Model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class MTSmodel {
    private UserModel sender;
    private String content;
    private boolean isWhisper;
    private List<UserModel> receivers;
}
