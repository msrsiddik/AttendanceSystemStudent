package msr.attend.student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import msr.attend.student.Messenger.MessageActivity;
import msr.attend.student.Messenger.MessengerActivity;
import msr.attend.student.model.NoticeModel;
import msr.attend.student.model.StudentModel;
import msr.attend.student.model.UserPreference;

public class MainActivity extends AppCompatActivity implements FragmentInterface, BottomNavigationView.OnNavigationItemSelectedListener {
    private UserPreference preference;
    private FragmentManager fragmentManager;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        preference = new UserPreference(this);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        if (preference.getLoginStatus()){
            loadFragment(new VirtualCrad());
        } else {
            loadFragment(new Login());
            navigation.setVisibility(View.INVISIBLE);
//            preLoginApp();
        }

        onNewIntent(getIntent());

    }

    private void preLoginApp(){
        preference.setStudentIdPref("-MNCKtReQEod6aCbe-P-");
        preference.setBatchPref("43");
        preference.setLoginStatus(true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if(extras != null) {
            if (extras.containsKey("notice")) {
                loadFragment(new UserNotification());
            } else if (extras.containsKey("message")){
                Intent i = new Intent(this, MessengerActivity.class);
                this.startActivity(i);
            }
        }
    }

    @Override
    public void openScanner() {
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        integrator.setPrompt("DIU Student Id QR Scanner");
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null){
            new FirebaseDatabaseHelper().loginStudentByCardScan(result.getContents(), new FirebaseDatabaseHelper.StudentData() {
                @Override
                public void verifyStudent(StudentModel studentModel) {
                    preference.setStudentIdPref(studentModel.getId());
                    preference.setBatchPref(studentModel.getBatch());
                    preference.setUserName(studentModel.getName());
                    fragmentManager.beginTransaction().replace(R.id.FragmentContainer, new VirtualCrad()).commit();
                    navigation.setVisibility(View.VISIBLE);
                    preference.setLoginStatus(true);
                }

                @Override
                public void loginFailed() {
                    preference.setLoginStatus(false);
                    Toast.makeText(MainActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.card:
                fragment = new VirtualCrad();
                break;

            case R.id.profile:
                fragment = new Profile();
                break;

            case R.id.notification:
                fragment = new UserNotification();
                break;

            case R.id.messenger:
                Intent intent = new Intent(this, MessengerActivity.class);
                startActivity(intent);
                break;
        }
        return loadFragment(fragment);
    }
}