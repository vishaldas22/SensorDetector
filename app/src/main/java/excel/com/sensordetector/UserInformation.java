package excel.com.sensordetector;

public class UserInformation {

    public String name;
    public String address;
    public String email;
    public String mobile;
    public String password;

    public UserInformation(){

    }

    public UserInformation(String name, String address, String email, String mobile, String password) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
    }
}
