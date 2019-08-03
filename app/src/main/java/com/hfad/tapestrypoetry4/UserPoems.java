package com.hfad.tapestrypoetry4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.tapestrypoetry4.Adapter.ReadPoemAdapter;
import com.hfad.tapestrypoetry4.Adapter.UserPoemAdapter;
import com.hfad.tapestrypoetry4.Data.PoemData;

import java.util.ArrayList;
import java.util.List;

public class UserPoems extends AppCompatActivity {


    private UserPoemAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference1;
    private DatabaseReference mReference2;
    private List<PoemData> mReadPoems = new ArrayList<>();
    private List<String> KEYS = new ArrayList<>();
    private String dbName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_poems);

        mDatabase = FirebaseDatabase.getInstance();
        mReference1 = mDatabase.getReference();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String nickname = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        dbName = decodeUserEmail(email)+nickname;
        mReference2 = mDatabase.getReference().child("Users").child(dbName);
        mRecyclerView = findViewById(R.id.RVUserPoems);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                KEYS.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getValue(String.class);
                    KEYS.add(key);
                }
               // mAdapter.setOnItemClickListener(UserPoems.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        for(int i=0;i<KEYS.size();i++)
        {




        }

    }

    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }
}
