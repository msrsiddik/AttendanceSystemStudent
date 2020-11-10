package msr.attend.student.model;

public class StudentModel {
    private String id;
    private String name;
    private String department;
    private String studentId;
    private String batch;

    public StudentModel() {
    }

    public StudentModel(String id, String name, String department, String studentId, String batch) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.studentId = studentId;
        this.batch = batch;
    }

    public StudentModel(String name, String department, String studentId, String batch) {
        this.name = name;
        this.department = department;
        this.studentId = studentId;
        this.batch = batch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }
}
