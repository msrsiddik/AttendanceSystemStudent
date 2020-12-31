package msr.attend.student;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import msr.attend.student.model.ClassModel;
import msr.attend.student.model.StudentModel;
import msr.attend.student.model.UserPreference;

public class Profile extends Fragment {
    private UserPreference preference;
    private TextView studentName, studentBatch, studentDepartName,
            studentPhoneNumber, guardianMobileNo;
    private ExpandableListView exListView;
    private FirebaseDatabaseHelper firebaseDatabaseHelper;
    private List<String> day;
    private HashMap<String, List<ClassModel>> classModelByDate;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        studentName = view.findViewById(R.id.studentName);
        studentBatch = view.findViewById(R.id.studentBatch);
        studentDepartName = view.findViewById(R.id.studentDepartName);
        studentPhoneNumber = view.findViewById(R.id.studentPhoneNumber);
        guardianMobileNo = view.findViewById(R.id.guardianMobileNo);
        exListView = view.findViewById(R.id.exListView);

        preference = new UserPreference(getContext());
        firebaseDatabaseHelper = new FirebaseDatabaseHelper();

        firebaseDatabaseHelper.loginStudentByCardScan(preference.getStudentIdPref(), new FirebaseDatabaseHelper.StudentData() {
            @Override
            public void verifyStudent(StudentModel studentModel) {
                studentName.setText(studentModel.getName());
                studentBatch.setText(studentModel.getBatch());
                studentDepartName.setText(studentModel.getDepartment());
                studentPhoneNumber.setText(studentModel.getStudentPhone());
                guardianMobileNo.setText(studentModel.getGuardianPhone());
            }

            @Override
            public void loginFailed() {

            }
        });

        firebaseDatabaseHelper.classModelByBatch(preference.getBatchPref(), models -> {
            if (getActivity() != null){

                Comparator<String> dateComparator = (s1, s2) -> {
                    try{
                        SimpleDateFormat format = new SimpleDateFormat("EEE");
                        Date d1 = format.parse(s1);
                        Date d2 = format.parse(s2);
                        if(d1.equals(d2)){
                            return s1.compareTo(s2);
                        }else{
                            Calendar cal1 = Calendar.getInstance();
                            Calendar cal2 = Calendar.getInstance();
                            cal1.setTime(d1);
                            cal2.setTime(d2);
                            return cal1.get(Calendar.DAY_OF_WEEK) - cal2.get(Calendar.DAY_OF_WEEK);
                        }
                    }catch(ParseException pe){
                        throw new RuntimeException(pe);
                    }
                };

                Comparator<ClassModel> timeCompare = (s1, s2) -> {
                    try{
                        SimpleDateFormat format = new SimpleDateFormat("h");
                        Date d1 = format.parse(s1.getTime());
                        Date d2 = format.parse(s2.getTime());
                        if(d1.equals(d2)){
                            return s1.getTime().compareTo(s2.getTime());
                        }else{
                            Calendar cal1 = Calendar.getInstance();
                            Calendar cal2 = Calendar.getInstance();
                            cal1.setTime(d1);
                            cal2.setTime(d2);
                            return cal1.get(Calendar.HOUR_OF_DAY) - cal2.get(Calendar.HOUR_OF_DAY);
                        }
                    }catch(ParseException pe){
                        throw new RuntimeException(pe);
                    }
                };

                List<String> title = new ArrayList<>();
                HashMap<String, List<ClassModel>> map = new HashMap<>();
                for (ClassModel c : models){
                    if (!title.contains(c.getDay())){
                        title.add(c.getDay());
                    }
                }

                Collections.sort(title, dateComparator);

                for (String t : title){
                    List<ClassModel> classModels = new ArrayList<>();
                    for (ClassModel c : models){
                        if (c.getDay().equals(t)){
                            classModels.add(c);
                        }
                    }
                    Collections.sort(classModels, timeCompare);
                    map.put(t,classModels);
                }

                day = title;
                classModelByDate = map;
                exListView.setAdapter(new ExpanListAdapter(getContext(),map,title));

            }
        });

        exListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            firebaseDatabaseHelper.getTeacherProfile(classModelByDate.get(day.get(groupPosition)).get(childPosition).getTeacherId(), teacherModel -> {
                Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.teacher_profile);

                TextView teacherName, teacherPhone, teacherEmail, teacherDepart;
                teacherName = dialog.findViewById(R.id.teacherName);
                teacherPhone = dialog.findViewById(R.id.teacherPhone);
                teacherEmail = dialog.findViewById(R.id.teacherEmail);
                teacherDepart = dialog.findViewById(R.id.teacherDepart);
                if (teacherModel.getGender().equals("Male")){
                    teacherName.setText("👨‍🎓 "+teacherModel.getName());
                } else {
                    teacherName.setText("👩‍🎓 "+teacherModel.getName());
                }
                teacherPhone.setText("📞 "+teacherModel.getPhone());
                teacherPhone.setOnClickListener(v1 -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+teacherModel.getPhone()));
                    startActivity(intent);
                });
                teacherEmail.setText("✉ "+teacherModel.getEmail());
                teacherEmail.setOnClickListener(v1 -> {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"+teacherModel.getEmail()));
                    startActivity(intent);
                });
                teacherDepart.setText("🏬 "+teacherModel.getDepartment());

                dialog.show();
            });
            return false;
        });

    }

    class ExpanListAdapter extends BaseExpandableListAdapter{
        Context context;
        HashMap<String, List<ClassModel>> child;
        List<String> parent;
        char colorPos = 'x';

        public ExpanListAdapter(Context context, HashMap<String, List<ClassModel>> child, List<String> parent) {
            this.context = context;
            this.child = child;
            this.parent = parent;
        }

        @Override
        public int getGroupCount() {
            return parent.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return child.get(parent.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return parent.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return child.get(parent.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return groupPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String title = (String) getGroup(groupPosition);
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.ex_header_row,null);
            }
            TextView txt = convertView.findViewById(R.id.header);
            txt.setTypeface(null, Typeface.BOLD);
            if (title.equals(new SimpleDateFormat("EEEE").format(Calendar.getInstance().getTime()))){
                txt.setBackgroundColor(Color.GREEN);
                colorPos = 'c';
            } else {
                txt.setBackgroundColor(Color.WHITE);
                colorPos = 'x';
            }
            txt.setText(title);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ClassModel child = (ClassModel) getChild(groupPosition,childPosition);
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.ex_child_row,null);
            }
            TextView textView = convertView.findViewById(R.id.child);
            textView.setText("   "+child.getSubCode()+" -> "+child.getTime());
            if (colorPos == 'c'){
                textView.setBackgroundColor(Color.LTGRAY);
            } else {
                textView.setBackgroundColor(Color.WHITE);
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}