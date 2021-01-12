package msr.attend.student.Messenger.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireDatebase {
    public static DatabaseReference getMessengerRef(){
        return FirebaseDatabase.getInstance().getReference("Messenger");
    }
}
