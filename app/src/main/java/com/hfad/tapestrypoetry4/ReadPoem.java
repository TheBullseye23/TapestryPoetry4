package com.hfad.tapestrypoetry4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.tapestrypoetry4.Data.PoemData;
import com.hfad.tapestrypoetry4.Data.WORD;
import com.hfad.tapestrypoetry4.REST.Retrofit;
import com.hfad.tapestrypoetry4.REST.SampleInterface1;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ReadPoem extends AppCompatActivity {

    private TextView title;
    private TextView tPoem;
    private TextView mAuthors;
    private Button mButton;
    private String key;
    private TextView time;
    PoemData mPoem;
    boolean isMoving=false;
    private String reqword;
    private ActionMode mActionmode;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference2;
    private List<String> mapLats=new ArrayList<>();
    private List<String> mapLongs=new ArrayList<>();

    private SampleInterface1 mInterface;
    private FusedLocationProviderClient mClient;
    private List<WORD> mWORDS = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_poem);

        Intent intent = getIntent();
        key = intent.getStringExtra("KEY");

        title=findViewById(R.id.title_readpoem);
        tPoem=findViewById(R.id.poem_readpoem);
        mAuthors=findViewById(R.id.authors_readpoem);
        mButton=findViewById(R.id.mapbtn_readpoem);
        time=findViewById(R.id.tv_time);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),mapview_edit.class);
                intent.putStringArrayListExtra("LATS", (ArrayList<String>) mapLats);
                intent.putStringArrayListExtra("LONGS", (ArrayList<String>) mapLongs);
                List<String> names = new ArrayList<>();
                for(int i=0;i<mPoem.getAuthors().size();i++)
                {
                    names.add(mPoem.getAuthors().get(i));
                }
                intent.putStringArrayListExtra("NAMES", (ArrayList<String>) names);
                startActivity(intent);

            }
        });

        RequestPermission();

        mInterface = Retrofit.getRetrofitClient().create(SampleInterface1.class);

        mClient = LocationServices.getFusedLocationProviderClient(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Poems").child(key);
        mDatabaseReference2 = mFirebaseDatabase.getReference("Poems").child(key);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPoem = dataSnapshot.getValue(PoemData.class);
                title.setText(mPoem.getTitle());
                tPoem.setText(mPoem.getPoemLines());
                String dAuthors=" ";
                for(int i=0;i< mPoem.getAuthors().size();i++)
                {
                    dAuthors+=mPoem.getAuthors().get(i);
                    dAuthors+=",";
                }
                mAuthors.setText(dAuthors);
                time.setText(mPoem.getTime());
                mapLats = mPoem.getLatitudes();
                mapLongs = mPoem.getLongitudes();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tPoem.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    isMoving = true;
                    Log.i("isMoving:", "true");
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int q = tPoem.getOffsetForPosition(event.getX(), event.getY());
                    reqword = getWord(tPoem.getText().toString(), q);
                    Log.d("IMP", reqword);
                    isMoving = false;
                    Log.i("isMoving:", "false");
                }
                return false;
            }
        });

        tPoem.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                if (!isMoving) {
                    if (mActionmode != null) {
                        return false;
                    }
                    mActionmode = startSupportActionMode(mActionModeCallback);
                }
                return true;
            }
        });

    }

    private void RequestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1);
    }

    public static String getWord(String textOfTextView, int offsetPosition) {
        int endpositionofword = 0;
        int startpositionofword = 0;
        for (int i = offsetPosition; i < textOfTextView.length(); i++) {
            if (i == textOfTextView.length() - 1) {
                endpositionofword = i;
                break;
            }
            if (textOfTextView.charAt(i) == ' ') {
                endpositionofword = i;
                break;
            }

        }
        for (int i = offsetPosition; i >= 0; i--) {
            if (i == 0) {
                startpositionofword = i;
                break;
            }
            if (textOfTextView.charAt(i) == ' ') {
                startpositionofword = i;
                break;
            }

        }
        return textOfTextView.substring(startpositionofword, endpositionofword);

    }


    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.example_menu, menu);
            mode.setTitle("Get Assistance?");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.option1: {
                    Call<List<WORD>> call = mInterface.getSynonyms("words?ml=" + reqword);

                    call.enqueue(new Callback<List<WORD>>() {
                        @Override
                        public void onResponse(Call<List<WORD>> call, Response<List<WORD>> response) {
                            mWORDS = response.body();
                            Log.d("VIMP", "response" + "  " + mWORDS.size());
                            String ans = "";
                            if (mWORDS.size() == 0)
                                Toast.makeText(getApplicationContext(), " No words found ", Toast.LENGTH_SHORT).show();
                            else {
                                for (int i = 0; i < mWORDS.size(); i++)
                                    ans += mWORDS.get(i).getWord() + " , ";
                               // Toast.makeText(getApplicationContext(), ans, Toast.LENGTH_LONG).show();
                                exampleDialog mDialog = new exampleDialog("Thesaurus",ans);
                                mDialog.show(getSupportFragmentManager(),"example dialog");

                            }
                        }

                        @Override
                        public void onFailure(Call<List<WORD>> call, Throwable t) {             //"words?ml="+reqword

                        }
                    });
                    mode.finish();


                    return true;
                }
                case R.id.option2: {
                    Call<List<WORD>> call = mInterface.getRhyme("words?rel_rhy=" + reqword);

                    call.enqueue(new Callback<List<WORD>>() {
                        @Override
                        public void onResponse(Call<List<WORD>> call, Response<List<WORD>> response) {
                            mWORDS = response.body();
                            Log.d("VIMP", "response" + "  " + mWORDS.size());
                            String ans = "";
                            if (mWORDS.size() == 0)
                                Toast.makeText(getApplicationContext(), " No words found ", Toast.LENGTH_SHORT).show();
                            else {
                                for (int i = 0; i < mWORDS.size(); i++)
                                    ans += mWORDS.get(i).getWord() + " , ";
                              //  Toast.makeText(getApplicationContext(), ans, Toast.LENGTH_LONG).show();
                                exampleDialog mDialog = new exampleDialog("Rhyming words",ans);
                                mDialog.show(getSupportFragmentManager(),"example dialog");

                            }


                        }

                        @Override
                        public void onFailure(Call<List<WORD>> call, Throwable t) {

                        }
                    });


                    mode.finish();
                    return true;
                }


                default:
                    return false;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionmode = null;
        }
    };
}
