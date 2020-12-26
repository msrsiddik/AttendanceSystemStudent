package msr.attend.student.model;

public class ClassModel {
    private String classId;
    private String teacherId;
    private String depart;
    private String batch;
    private String semester;
    private String subCode;
    private String day;
    private String time;

    public ClassModel() {
    }

    public ClassModel(String teacherId, String depart, String batch, String semester, String subCode, String day, String time) {
        this.teacherId = teacherId;
        this.depart = depart;
        this.batch = batch;
        this.semester = semester;
        this.subCode = subCode;
        this.day = day;
        this.time = time;
    }

    public ClassModel(String classId, String teacherId, String depart, String batch, String semester, String subCode, String day, String time) {
        this.classId = classId;
        this.teacherId = teacherId;
        this.depart = depart;
        this.batch = batch;
        this.semester = semester;
        this.subCode = subCode;
        this.day = day;
        this.time = time;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
