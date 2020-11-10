package msr.attend.student;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import msr.attend.student.model.StudentModel;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase database;
    private DatabaseReference studentRef;

    public FirebaseDatabaseHelper() {
        database = FirebaseDatabase.getInstance();
        studentRef = database.getReference("Students");
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

}
