package com.hfad.tapestrypoetry4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class EditPoem extends AppCompatActivity {

    private String key;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference2;   // for poems
    private DatabaseReference mDatabaseReference3;  // for users
    PoemData mPoem;
    private FusedLocationProviderClient mClient;
    private List<String> listAuthors = new ArrayList<>();

    boolean isMoving = false;
    private TextView CP2title;
    private TextView CP2poem;
    private TextView CP2time;
    private EditText newpoem;
    private Button cp2Button;
    private ActionMode mActionmode;
    private SampleInterface1 mInterface;
    private String reqword;
    private List<WORD> mWORDS = new ArrayList<>();

    private double qlatitude;
    private double qlongitude;

    private String slatitude;
    private String slongitude;


    private Button mapButton;
    private List<String> mapLats=new ArrayList<>();
    private List<String> mapLongs=new ArrayList<>();
    List<String> wLatitudes = new ArrayList<>();
    List<String> wLongitudes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_poem);

        Intent intent = getIntent();
        key = intent.getStringExtra("KEY");

        CP2title = findViewById(R.id.edit_title);
        CP2poem = findViewById(R.id.edit_originalpoem);
        CP2time = findViewById(R.id.edit_time);
        cp2Button = findViewById(R.id.btn_edit);
        mapButton = findViewById(R.id.btn_viewmap);
        newpoem = findViewById(R.id.edit_newpoem);

        RequestPermission();

        mInterface = Retrofit.getRetrofitClient().create(SampleInterface1.class);




        cp2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

                if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                mClient.getLastLocation().addOnSuccessListener(EditPoem.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location==null)
                            Toast.makeText(getApplicationContext(),"Turn on location to add poem",Toast.LENGTH_SHORT).show();
                        if (location != null) {
                            qlatitude = location.getLatitude();
                            qlongitude = location.getLongitude();
                            slatitude = String.valueOf(qlatitude);
                            slongitude = String.valueOf(qlongitude);
                            mDatabaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String NewLines = null;
                                    String OldLines = mPoem.getPoemLines();
                                    NewLines = OldLines + newpoem.getText().toString();
                                    mPoem.setPoemLines(NewLines);
                                    Log.d("LATITUDE", "a");
                                    wLatitudes = mPoem.getLatitudes();
                                    wLatitudes.add(slatitude);
                                    wLongitudes = mPoem.getLongitudes();
                                    wLongitudes.add(slongitude);
                                    mPoem.setLatitudes(wLatitudes);
                                    mPoem.setLongitudes(wLongitudes);
                                    List<String> mAuthors = mPoem.getAuthors();
                                    mAuthors.add(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                    mPoem.setAuthors(mAuthors);
                                    CP2poem.setText(NewLines);
                                    mDatabaseReference2.setValue(mPoem);
                                    mapLats = mPoem.getLatitudes();
                                    mapLongs = mPoem.getLongitudes();
                                    Toast.makeText(getApplicationContext(), "Added!", Toast.LENGTH_SHORT).show();
                                    newpoem.setText("");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }



                });


            }
    });


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Poems").child(key);
        mDatabaseReference2 = mFirebaseDatabase.getReference("Poems").child(key);
        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        String Username = User.getEmail();
        String Nickname = User.getDisplayName();
        String dbName = encodeUserEmail(Username);
        dbName = dbName+Nickname;
        mDatabaseReference3 = mFirebaseDatabase.getReference("Users").child(dbName).child(key);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPoem = dataSnapshot.getValue(PoemData.class);
                CP2title.setText(mPoem.getTitle());
                CP2time.setText(mPoem.getTime());
                CP2poem.setText(mPoem.getPoemLines());
                mapLats=mPoem.getLatitudes();
                mapLongs=mPoem.getLongitudes();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        CP2poem.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    isMoving = true;
                    Log.i("isMoving:", "true");
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int q = CP2poem.getOffsetForPosition(event.getX(), event.getY());
                    reqword = getWord(CP2poem.getText().toString(), q);
                    Log.d("IMP", reqword);
                    isMoving = false;
                    Log.i("isMoving:", "false");
                }
                return false;
            }
        });

        CP2poem.setOnLongClickListener(new View.OnLongClickListener() {

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


        newpoem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    isMoving = true;
                    Log.i("isMoving:", "true");
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int q = newpoem.getOffsetForPosition(event.getX(), event.getY());
                    String reqword = getWord(newpoem.getText().toString(), q);
                    Log.d("IMP", reqword);
                    isMoving = false;
                    Log.i("isMoving:", "false");
                }
                return false;
            }

        });

        newpoem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mActionmode != null) {
                    return false;
                }
                mActionmode = startSupportActionMode(mActionModeCallback);
                return true;
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
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

    }

    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
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
                    //Toast.makeText(getApplicationContext(), "Thesaraus", Toast.LENGTH_SHORT).show();
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
                                //Toast.makeText(getApplicationContext(), ans, Toast.LENGTH_LONG).show();

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
