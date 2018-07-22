package hsim.checkpoint.model.user;

import hsim.checkpoint.model.BaseModel;
import hsim.checkpoint.model.user.type.Gender;
import hsim.checkpoint.model.user.type.Membership;
import lombok.Data;

@Data
public class UserModel extends BaseModel {

    private String name;
    private String loginId;
    private String password;

    private Membership membership;
    private String nickName;
    private Gender gender;
    private String email;
    private String contactNumber;
    private String address;
}
