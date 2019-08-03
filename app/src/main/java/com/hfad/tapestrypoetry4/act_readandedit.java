package com.hfad.tapestrypoetry4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.tapestrypoetry4.Adapter.ReadPoemAdapter;
import com.hfad.tapestrypoetry4.Data.PoemData;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE;
import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
import static androidx.recyclerview.widget.ItemTouchHelper.RIGHT;

public class act_readandedit extends AppCompatActivity implements ReadPoemAdapter.OnItemClickListener,SearchView.OnQueryTextListener {

    private ReadPoemAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference1;
    private List<PoemData> mReadPoems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_readandedit);

        mDatabase = FirebaseDatabase.getInstance();
        mReference1 = mDatabase.getReference().child("Poems");
        mRecyclerView = findViewById(R.id.RVReadEdit);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mReadPoems.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    PoemData mPoem = ds.getValue(PoemData.class);
                    mReadPoems.add(mPoem);
                }

                mAdapter = new ReadPoemAdapter(mReadPoems);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(act_readandedit.this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.Callback() {
            Boolean swipeBack = false;

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, LEFT | RIGHT);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                if (swipeBack) {
                    swipeBack = false;
                    return 0;
                }
                return super.convertToAbsoluteDirection(flags, layoutDirection);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ACTION_STATE_SWIPE) {
                    setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            private void setTouchListener(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                recyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;

                        if (swipeBack) {
                            if (dX > 300) {
                                Log.d("SWIPE"," positive  "+String.valueOf(dX));
                                int pos = viewHolder.getAdapterPosition();
                                Log.d("onclick", "onclick was triggered");
                                String key = mReadPoems.get(pos).getKey();
                                Bundle mBundle = new Bundle();
                                mBundle.putString("KEY", key);
                                Intent intent = new Intent(getApplicationContext(), EditPoem.class);
                                intent.putExtras(mBundle);
                                startActivity(intent);
                            }
                            if (dX < -300){
                                Log.d("SWIPE"," positive  "+String.valueOf(dX));
                            int pos = viewHolder.getAdapterPosition();
                            Log.d("onclick", "onclick was triggered");
                            String key = mReadPoems.get(pos).getKey();
                            Bundle mBundle = new Bundle();
                            mBundle.putString("KEY", key);
                            Intent intent = new Intent(getApplicationContext(), ReadPoem.class);
                            intent.putExtras(mBundle);
                            startActivity(intent);}
                        }
                        return false;
                    }
                });
            }

        }).attachToRecyclerView(mRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchview = (SearchView) menuItem.getActionView();
        searchview.setOnQueryTextListener(this);
        return true;
    }


    @Override
    public void onItemClick(int position) {
        Log.d("onclick", "onclick was triggered");
        String key = mReadPoems.get(position).getKey();
        Bundle mBundle = new Bundle();
        mBundle.putString("KEY", key);
        Intent intent = new Intent(this, EditPoem.class);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userinput=newText.toLowerCase().trim();
        List<PoemData> newList = new ArrayList<>();

        for(PoemData fd: mReadPoems)
        {
            if(fd.getTitle().toLowerCase().contains(userinput)|| fd.getPoemLines().toLowerCase().contains(userinput))
                newList.add(fd);
        }

        mAdapter.updateList(newList);
        return true;
    }
}

