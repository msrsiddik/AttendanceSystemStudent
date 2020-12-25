package msr.attend.student.model;

public class NoticeModel {
    private String noticeId;
    private String teacherId;
    private String batch;
    private String noticeTitle;
    private String noticeBody;
    private String noticeValidTime;

    public NoticeModel() {
    }

    public NoticeModel(String teacherId, String batch, String noticeTitle, String noticeBody, String noticeValidTime) {
        this.teacherId = teacherId;
        this.batch = batch;
        this.noticeTitle = noticeTitle;
        this.noticeBody = noticeBody;
        this.noticeValidTime = noticeValidTime;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeBody() {
        return noticeBody;
    }

    public void setNoticeBody(String noticeBody) {
        this.noticeBody = noticeBody;
    }

    public String getNoticeValidTime() {
        return noticeValidTime;
    }

    public void setNoticeValidTime(String noticeValidTime) {
        this.noticeValidTime = noticeValidTime;
    }
}
