package space.ahaha.project;
/**
 * Created by lf729 on 2016/12/12.
 */
public class User {
    private Boolean status;
    private String phoneNumber;
    private String username;
    private String avatar;
    private String address;
    public String sessionID;

    User(String phone,String avatar){
        this.phoneNumber=phone;
        this.avatar=avatar;
    }
}
