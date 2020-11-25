package msr.attend.student;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import msr.attend.student.model.StudentModel;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase database;
    private DatabaseReference studentRef;
    private DatabaseReference currentStatus;

    public FirebaseDatabaseHelper() {
        database = FirebaseDatabase.getInstance();
        studentRef = database.getReference("Students");
        currentStatus = database.getReference("AttendInfoInUniversity");
    }

    public interface StudentData{
        void verifyStudent(StudentModel studentModel);
        void loginFailed();
    }

    public void loginStudentByCardScan(String id, final StudentData data){
        studentRef.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    StudentModel model = ds.getValue(StudentModel.class);
                    if (!model.getId().equals("")){
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

    public interface CurrentStatus{
        void currentStatusListener(String s);
    }

    public void getStatusInOut(String id, CurrentStatus status){
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String date = format.format(new Date(Calendar.getInstance().getTime().getTime()));

        currentStatus.child("CurrentStatus").child(date).orderByChild(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot d : snapshot.getChildren()){
                            if (d.getKey().equals(id)){
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
