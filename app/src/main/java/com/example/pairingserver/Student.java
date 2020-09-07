package com.example.pairingserver;

public class Student {
    String user_name;
    String name;
    String location;
    String email;
    String phone;
    String gender;
    int age;
    String study_year;
    int GPA;
    String preferred_gender;
    String preferred_meetings;
    String preferred_work_plan;
    String preferred_hours;
    boolean location_flag, GPA_flag;
    String faculty;
    String course;
    String work_type;

    public Student(String user_name, String name, String location, String email, String phone, String gender, int age, String study_year, int GPA, String preferred_gender, String preferred_meetings, String preferred_work_plan, String preferred_hours, boolean location_flag, boolean GPA_flag, String faculty, String course, String work_type) {
        this.user_name = user_name;
        this.name = name;
        this.location = location;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.age = age;
        this.study_year = study_year;
        this.GPA = GPA;
        this.preferred_gender = preferred_gender;
        this.preferred_meetings = preferred_meetings;
        this.preferred_work_plan = preferred_work_plan;
        this.preferred_hours = preferred_hours;
        this.location_flag = location_flag;
        this.GPA_flag = GPA_flag;
        this.faculty = faculty;
        this.course = course;
        this.work_type = work_type;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(final String user_name) {
        this.user_name = user_name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStudy_year() {
        return study_year;
    }

    public void setStudy_year(String study_year) {
        this.study_year = study_year;
    }

    public int getGPA() {
        return GPA;
    }

    public void setGPA(int GPA) {
        this.GPA = GPA;
    }

    public String getPreferred_gender() {
        return preferred_gender;
    }

    public void setPreferred_gender(String preferred_gender) {
        this.preferred_gender = preferred_gender;
    }

    public String getPreferred_meetings() {
        return preferred_meetings;
    }

    public void setPreferred_meetings(String preferred_meetings) {
        this.preferred_meetings = preferred_meetings;
    }

    public String getPreferred_work_plan() {
        return preferred_work_plan;
    }

    public void setPreferred_work_plan(String preferred_work_plan) {
        this.preferred_work_plan = preferred_work_plan;
    }

    public String getPreferred_hours() {
        return preferred_hours;
    }

    public void setPreferred_hours(String preferred_hours) {
        this.preferred_hours = preferred_hours;
    }

    public boolean isLocation_flag() {
        return location_flag;
    }

    public void setLocation_flag(boolean location_flag) {
        this.location_flag = location_flag;
    }

    public boolean isGPA_flag() {
        return GPA_flag;
    }

    public void setGPA_flag(boolean GPA_flag) {
        this.GPA_flag = GPA_flag;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getWork_type() {
        return work_type;
    }

    public void setWork_type(String work_type) {
        this.work_type = work_type;
    }

}
