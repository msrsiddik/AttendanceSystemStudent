        package msr.attend.student;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import msr.attend.student.model.StudentModel;
import msr.attend.student.model.UserPreference;
import msr.attend.student.model.Utils;

public class VirtualCrad extends Fragment {
    private UserPreference preference;
    private ImageView qrCodeView;
    private TextView studentName, studentBatch, studentDepartName, studentCurrentStatus;
    public VirtualCrad() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_virtual_crad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        qrCodeView = view.findViewById(R.id.qrCode);
        studentName = view.findViewById(R.id.studentName);
        studentBatch = view.findViewById(R.id.studentBatch);
        studentDepartName = view.findViewById(R.id.studentDepartName);
        studentCurrentStatus = view.findViewById(R.id.studentCurrentStatus);
        preference = new UserPreference(getContext());

        String id = preference.getStudentIdPref();
        if (!id.equals("")){
            new FirebaseDatabaseHelper().loginStudentByCardScan(id, new FirebaseDatabaseHelper.StudentData() {
                @Override
                public void verifyStudent(StudentModel studentModel) {
                    setUpProfile(studentModel);
                }

                @Override
                public void loginFailed() {

                }
            });
        }

        Thread thread = new Thread(() -> {
            while (true) {
                new FirebaseDatabaseHelper().getStatusInOut(id, s -> studentCurrentStatus.setText(s));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    private void setUpProfile(StudentModel studentModel) {
        MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
        try{
            BitMatrix bitMatrix=multiFormatWriter.encode(studentModel.getId(), BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
            final Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
            qrCodeView.setImageBitmap(bitmap);
            studentName.setText(studentModel.getName());
            studentBatch.setText(studentModel.getBatch());
            studentDepartName.setText(studentModel.getDepartment());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}