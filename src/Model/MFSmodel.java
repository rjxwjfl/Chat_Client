package Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MFSmodel {
    private UserModel sender;
    private String contents;
}
