package msr.attend.student.model;

public class StudentModel {
    private String id;
    private String name;
    private String department;
    private String roll;
    private String studentId;
    private String batch;
    private String studentPhone;
    private String guardianPhone;

    public StudentModel() {
    }

    public StudentModel(String id, String name, String department, String roll, String studentId, String batch, String studentPhone, String guardianPhone) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.roll = roll;
        this.studentId = studentId;
        this.batch = batch;
        this.studentPhone = studentPhone;
        this.guardianPhone = guardianPhone;
    }

    public StudentModel(String name, String department, String roll, String studentId, String batch, String studentPhone, String guardianPhone) {
        this.name = name;
        this.department = department;
        this.roll = roll;
        this.studentId = studentId;
        this.batch = batch;
        this.studentPhone = studentPhone;
        this.guardianPhone = guardianPhone;
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

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
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

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    public String getGuardianPhone() {
        return guardianPhone;
    }

    public void setGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone;
    }
}
