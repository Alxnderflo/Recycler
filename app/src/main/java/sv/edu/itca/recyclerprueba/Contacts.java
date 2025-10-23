package sv.edu.itca.recyclerprueba;

public class Contacts {
    private String name, phoneNo;

    public Contacts() {}

    public Contacts(String name, String phoneNo) {
        this.name = name;
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}