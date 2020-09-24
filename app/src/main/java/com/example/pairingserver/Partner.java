package com.example.pairingserver;


public class Partner {
    int id;
    String name;
    String username;
    String email;
    String phone;
    int agreed1;
    String faculty;
    String course;
    String workType;
    String Pair_UN;
    String pairName;
    String pairEmail;
    String pairPhone;
    int agreed2;

    public Partner(int id, String username, String name, String email, String phone, int agreed1, String faculty, String course, String workType, String Pair_UN , String pairName, String pairEmail, String phone_number, int agreed2) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.agreed1 = agreed1;
        this.faculty = faculty;
        this.course = course;
        this.workType = workType;
        this.Pair_UN = Pair_UN;
        this.pairName = pairName;
        this.pairEmail = pairEmail;
        this.pairPhone = phone_number;
        this.agreed2 = agreed2;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getPair_UN() {
        return Pair_UN;
    }

    public void setPair_UN(String pair_UN) {
        Pair_UN = pair_UN;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAgreed1() {
        return agreed1;
    }

    public void setAgreed1(int agreed1) {
        this.agreed1 = agreed1;
    }

    public String getPairEmail() {
        return pairEmail;
    }

    public void setPairEmail(String pairEmail) {
        this.pairEmail = pairEmail;
    }

    public String getPairPhone() {
        return pairPhone;
    }

    public void setPairPhone(String pairPhone) {
        this.pairPhone = pairPhone;
    }

    public int getAgreed2() {
        return agreed2;
    }

    public void setAgreed2(int agreed2) {
        this.agreed2 = agreed2;
    }

    public String getPairName() {
        return pairName;
    }

    public void setPairName(String pairName) {
        this.pairName = pairName;
    }
}
