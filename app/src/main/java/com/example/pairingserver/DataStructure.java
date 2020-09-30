package com.example.pairingserver;

public class DataStructure {
    Student[] students;
    int[][] scores;
    int[][] pairs;
    String faculty;
    String course;
    String workType;
    int count=0;
    public DataStructure() {
    }

    public void AddStudent(Student st) {
        if (students != null)
            if (count < students.length) {
                st.setNo(count);
                students[count] = st;
                count++;
            }
    }
    public DataStructure(Student[] students, String faculty, String course, String workType) {
        this.students = students;
        this.faculty = faculty;
        this.course = course;
        this.workType = workType;
        if (students != null)
            this.scores = new int[students.length][students.length];
    }

    public DataStructure(Student[] students) {
        this.students = students;
    }

    public Student[] getStudents() {
        return this.students;
    }

    public void setStudents(final Student[] students) {
        this.students = students;
    }

    public String getFaculty() {
        return this.faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getCourse() {
        return this.course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getWorkType() {
        return this.workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public void setScores(int[][] scores) {
        this.scores = scores;
    }
    public int[][] getScores() {
        return this.scores;
    }
}
