package msr.attend.student;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import msr.attend.student.model.ClassModel;
import msr.attend.student.model.NoticeModel;
import msr.attend.student.model.UserPreference;

public class UserNotification extends Fragment {
    private UserPreference preference;
    private ListView noticeList;
    private FirebaseDatabaseHelper firebaseDatabaseHelper;

    public UserNotification() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        preference = new UserPreference(getContext());
        noticeList = view.findViewById(R.id.noticeList);

        firebaseDatabaseHelper = new FirebaseDatabaseHelper();
        firebaseDatabaseHelper.getNotice(preference.getBatchPref(), noticeModels -> {
            if (getActivity() != null && noticeModels.size() > 0){
                noticeList.setAdapter(new NoticeAdapter(getContext(), noticeModels));
            }
        });
    }

    class NoticeAdapter extends ArrayAdapter<NoticeModel> {
        Context context = null;
        List<NoticeModel> list = null;

        public NoticeAdapter(@NonNull Context context, @NonNull List<NoticeModel> objects) {
            super(context, R.layout.notice_row, objects);
            this.context = context;
            this.list = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.notice_row, parent, false);

            TextView validDate, batch, noticeTitle, noticeBody;
            validDate = view.findViewById(R.id.validDate);
            batch = view.findViewById(R.id.batch);
            noticeTitle = view.findViewById(R.id.noticeTitle);
            noticeBody = view.findViewById(R.id.noticeBody);

            NoticeModel notice = list.get(position);
            validDate.setText(notice.getNoticeValidTime());
            batch.setText(notice.getBatch());
            noticeTitle.setText(notice.getNoticeTitle());
            noticeBody.setText(notice.getNoticeBody());

            return view;
        }
    }

}