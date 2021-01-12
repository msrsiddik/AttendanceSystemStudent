package msr.attend.student.Messenger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import msr.attend.student.Messenger.adapter.UserAdapter;
import msr.attend.student.Messenger.model.Chatlist;
import msr.attend.student.Messenger.model.FireDatebase;
import msr.attend.student.Messenger.model.User;
import msr.attend.student.model.StudentModel;
import msr.attend.student.model.UserPreference;
import msr.attend.student.R;

public class ChatsUser extends Fragment {
    private UserPreference userPref;
    private RecyclerView recycler_view;
    private UserAdapter userAdapter;
    private List<Chatlist> usersList = new ArrayList<>();
    private List<User> mUsers;

    DatabaseReference reference;

    public ChatsUser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recycler_view = view.findViewById(R.id.recycler_view);
        recycler_view = view.findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));

        userPref = new UserPreference(getContext());

        reference = FireDatebase.getMessengerRef().child("Chatlist").child(userPref.getStudentIdPref());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void chatList() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Teachers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    StudentModel user = snapshot.getValue(StudentModel.class);
                    for (Chatlist chatlist : usersList){
                        if (user.getId().equals(chatlist.getId())){
                            mUsers.add(new User(user.getId(),user.getName(),"offline"));
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(), mUsers, true);
                recycler_view.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}