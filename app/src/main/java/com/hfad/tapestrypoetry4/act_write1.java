package com.hfad.tapestrypoetry4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.tapestrypoetry4.Data.PoemData;
import com.hfad.tapestrypoetry4.Data.UserData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class act_write1 extends AppCompatActivity {

    Button mButton;
    EditText et_title;
    EditText et_poemLines;
    String title = null;                                      // check if string data type will later throw an npe
    String poemLines = null;
    private FirebaseDatabase mfirebasedatabase;
    private DatabaseReference mdatabasereference;
    private DatabaseReference mdatabasereference3;
    private DatabaseReference mdatabasereference4;
    private FirebaseUser User;
    private List<String> Users = new ArrayList<>();
    private String Username;
    private String Time;
    private Calendar calendar;
    private List<String> authors = new ArrayList<>();
    private FusedLocationProviderClient mclient;
    private String Nickname;
    private String dbName;
    private List<String> DBkeys=new ArrayList<>();

    double Mlatitude;
    double Mlongitude;

    private String klatitude;
    private String klongitude;

    private List<String> mLatitudes = new ArrayList<>();
    private List<String> mLongitudes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_write1);

        et_title = findViewById(R.id.newpoemtitle);
        et_poemLines = findViewById(R.id.NewPoemLines);
        mButton = findViewById(R.id.btncreate);

        RequestPermission();

        mclient = LocationServices.getFusedLocationProviderClient(this);

        mfirebasedatabase = FirebaseDatabase.getInstance();
        mdatabasereference = mfirebasedatabase.getReference();
        mdatabasereference3 = mfirebasedatabase.getReference();
        mdatabasereference4 = mfirebasedatabase.getReference();
        User = FirebaseAuth.getInstance().getCurrentUser();
        Username = User.getEmail();
        Nickname = User.getDisplayName();
        dbName = encodeUserEmail(Username);
        dbName = dbName+Nickname;



        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                mclient.getLastLocation().addOnSuccessListener(act_write1.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location==null)
                            Toast.makeText(getApplicationContext(),"Turn on location to add poem",Toast.LENGTH_SHORT).show();
                        if(location!=null)
                        {
                            Mlatitude=location.getLatitude();
                            Mlongitude=location.getLongitude();
                            klatitude=String.valueOf(Mlatitude);
                            klongitude=String.valueOf(Mlongitude);
                            String samplelocation = String.valueOf(Mlatitude)+" , "+String.valueOf(Mlongitude);
                            title= et_title.getText().toString();
                            poemLines=et_poemLines.getText().toString();
                            authors.add(Username);
                            calendar=Calendar.getInstance();
                            String Month = calendar.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.getDefault());
                            Time= calendar.get(Calendar.DAY_OF_MONTH) +"th "+Month+", " + calendar.get(Calendar.YEAR) + " " ;
                            mdatabasereference3= mdatabasereference.child("Poems").push();
                            String key = mdatabasereference3.getKey();
                            mLatitudes.add(klatitude);
                            mLongitudes.add(klongitude);
                            PoemData poemdata = new PoemData(title,Time,poemLines,authors,key,mLatitudes,mLongitudes);
                            mdatabasereference3.setValue(poemdata);
                            DBkeys.add(key);
                            PoemData mUserdata = new PoemData(title,Time,poemLines,authors,key,mLatitudes,mLongitudes);
                            mdatabasereference4 = mdatabasereference.child("Users").child(dbName);
                            mdatabasereference4.setValue(mUserdata);
                            // write code for the users data structure to store which all poems are stored
                            Toast.makeText(getApplicationContext(),"An amazing poem has been created ",Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }

        });
    }

    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }


    private void RequestPermission(){
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1);
        }

}
