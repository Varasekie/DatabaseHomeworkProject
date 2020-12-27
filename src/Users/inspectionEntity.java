package Users;

public class inspectionEntity {
    String userName;
    String EmployerName;
    String location;
    String time;
    String userTele;
    String employerTele;

    public String getEmployerTele() {
        return employerTele;
    }

    public void setEmployerTele(String employerTele) {
        this.employerTele = employerTele;
    }

    public String getUserTele() {
        return userTele;
    }

    public void setUserTele(String userTele) {
        this.userTele = userTele;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmployerName() {
        return EmployerName;
    }

    public void setEmployerName(String employerName) {
        EmployerName = employerName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
