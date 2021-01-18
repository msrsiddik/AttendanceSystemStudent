package msr.attend.student;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import msr.attend.student.Notification.APIService;
import msr.attend.student.Notification.Client;
import msr.attend.student.Notification.Data;
import msr.attend.student.Notification.MyResponse;
import msr.attend.student.Notification.NotificationSender;
import msr.attend.student.model.ClassAttendModel;
import msr.attend.student.model.ClassModel;
import msr.attend.student.model.NoticeModel;
import msr.attend.student.model.StudentModel;
import msr.attend.student.model.TeacherModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase database;
    private DatabaseReference studentRef;
    private DatabaseReference currentStatus;
    private DatabaseReference notification;
    private DatabaseReference classInfoRef;
    private DatabaseReference teacherProfile;
    private DatabaseReference classAttendInfo;

    private APIService apiService;

    public FirebaseDatabaseHelper() {
        database = FirebaseDatabase.getInstance();
        studentRef = database.getReference("Students");
        currentStatus = database.getReference("AttendInfoInUniversity");
        notification = database.getReference().child("Notification");
        classInfoRef = database.getReference().child("ClassInformation");
        teacherProfile = database.getReference("Teachers");
        classAttendInfo = database.getReference().child("ClassAttendInfo");

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

    }

    public interface AttendDataShort{
        void classAttendListener(List<ClassAttendModel> attendList);
    }

    public void getAllAttendanceInfoByBatchAndSubjectCode(String batch, String subjectCode, final AttendDataShort dataShort) {
        classAttendInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ClassAttendModel> list = new ArrayList<>();
                for (DataSnapshot d : snapshot.getChildren()) {
                    //date path
                    for (DataSnapshot a : d.getChildren()) {
                        //subject code path
                        for (DataSnapshot b : a.getChildren()) {
                            ClassAttendModel attendModel = b.getValue(ClassAttendModel.class);
                            if (attendModel.getBatch().equals(batch) && attendModel.getSubjectCode().equals(subjectCode)) {
                                list.add(attendModel);
                            }
                        }
                    }
                }
                dataShort.classAttendListener(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface TeacherDataShot {
        void teacherProfileListener(TeacherModel teacherModel);
    }

    public void getTeacherProfile(String teacherId, final TeacherDataShot dataShot) {
        Query query = teacherProfile.orderByChild("id").equalTo(teacherId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TeacherModel model = dataSnapshot.getValue(TeacherModel.class);
                    if (model.getId().equals(teacherId)) {
                        dataShot.teacherProfileListener(model);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface NoticeDataShot {
        void noticeDataListener(List<NoticeModel> noticeModels);
    }

    public void getNotice(String batch, final NoticeDataShot dataShot) {
        notification.child("Notice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<NoticeModel> list = new ArrayList<>();
                for (DataSnapshot teacherId : snapshot.getChildren()) {
                    for (DataSnapshot notice : teacherId.getChildren()) {
                        NoticeModel model = notice.getValue(NoticeModel.class);
                        if (model.getBatch().equals(batch)) {
                            list.add(model);
                        }
                    }
                }
                Collections.reverse(list);
                dataShot.noticeDataListener(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface ClassModelDataShot {
        void classModelListener(List<ClassModel> models);
    }

    public void classModelByBatch(String batch, final ClassModelDataShot classModelShot) {
        classInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ClassModel> classModels = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ClassModel model = ds.getValue(ClassModel.class);
                    if (model.getBatch().equals(batch)) {
                        classModels.add(model);
                    }
                }
                classModelShot.classModelListener(classModels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface StudentData {
        void verifyStudent(StudentModel studentModel);

        void loginFailed();
    }

//    public interface NoticeDataShot{
//        void noticeListener(List<NoticeModel> noticeModelList);
//    }

//    public void getNotice(String batch, final NoticeDataShot dataShot, Context context){
//
//        notification.child("-MNCOxP1Eh-bpqGpA0IR").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"n")
//                        .setContentTitle("title")
//                        .setSmallIcon(R.drawable.ic_launcher_background)
//                        .setAutoCancel(true)
//                        .setContentText("body");
//
//                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
//                managerCompat.notify(999,builder.build());
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        notification.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<NoticeModel> noticeModels = new ArrayList<>();
//                for (DataSnapshot d : snapshot.getChildren()){
//                    for (DataSnapshot tId : d.getChildren()){
//                        NoticeModel model = tId.getValue(NoticeModel.class);
//                        if (model.getBatch().equals(batch)) {
//                            noticeModels.add(model);
//                            Log.e("Notice",model.getNoticeBody());
//                        }
//                    }
//                }
//
//                dataShot.noticeListener(noticeModels);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    public void sendNotification(String teacherId, String senderName, Context context){
        notification.child("TeachersToken").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot token : snapshot.getChildren()) {
                    if (token.getKey().equals(teacherId)){
                        Data data = new Data(senderName,"New Message");
                        NotificationSender sender = new NotificationSender(data,token.getValue(String.class));
                        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if (response.code() == 200){
                                    if (response.body().success != 1){
                                        Toast.makeText(context, "Does not use the Teacher app", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setNotificationToken(String token, String batch, String studentId) {
        notification.child("Tokens").child(batch).child(studentId).setValue(token);
    }

    public void loginStudentByCardScan(String id, final StudentData data) {
        studentRef.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    StudentModel model = ds.getValue(StudentModel.class);
                    if (!model.getId().equals("")) {
                        data.verifyStudent(model);
                    } else {
                        data.loginFailed();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface CurrentStatus {
        void currentStatusListener(String s);
    }

    public void getStatusInOut(String id, CurrentStatus status) {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String date = format.format(new Date(Calendar.getInstance().getTime().getTime()));

        currentStatus.child("CurrentStatus").child(date).orderByChild(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot d : snapshot.getChildren()) {
                            if (d.getKey().equals(id)) {
                                status.currentStatusListener(d.getValue(String.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}
