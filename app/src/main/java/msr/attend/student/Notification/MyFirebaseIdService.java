package msr.attend.student.Notification;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

import msr.attend.student.FirebaseDatabaseHelper;
import msr.attend.student.model.UserPreference;

import static msr.attend.student.MainActivity.USERID;

public class MyFirebaseIdService extends FirebaseMessagingService {
    private UserPreference preference;
    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);

        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        preference = new UserPreference(this);
        if(!preference.getStudentIdPref().equals("")){
            new FirebaseDatabaseHelper().setNotificationToken(refreshToken,preference.getBatchPref(),preference.getStudentIdPref());
        }

    }
}
