package com.hfad.tapestrypoetry4;

import android.companion.CompanionDeviceManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authlistener;
    private static final int RC_SIGN_IN = 1;
    private Button mButton;

    private CardView cv_read;
    private CardView cv_write;
    private CardView cv_fav;



    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cv_read=findViewById(R.id.main_read);
        cv_read.setOnClickListener(this);
        cv_write=findViewById(R.id.main_write);
        cv_write.setOnClickListener(this);
        cv_fav=findViewById(R.id.main_favourites);
        cv_fav.setOnClickListener(this);


        auth=FirebaseAuth.getInstance();
        mButton=findViewById(R.id.btn_signout);
        mButton.setOnClickListener(this);
        authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                    Toast.makeText(MainActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
                }else
                {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder().setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build()
                                    ))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            if(resultCode==RESULT_OK)
            {
                Toast.makeText(this,"Signed in!",Toast.LENGTH_SHORT).show();
            }
            else if(resultCode==RESULT_CANCELED)
            {
                Toast.makeText(this,"Login Cancelled",Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        auth.removeAuthStateListener(authlistener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(authlistener);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.main_read:
            {   Toast.makeText(this, "Swipe left for edit , right for reading !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,act_readandedit.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);

             break;}
            case R.id.main_write:
            { Intent intent = new Intent(this,act_write1.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
                break;}
            case R.id.main_favourites:
            {Toast.makeText(this, "fav was clicked , in development", Toast.LENGTH_SHORT).show();
                break;}
            case R.id.btn_signout:
            {   auth.getInstance().signOut();
                Toast.makeText(this, "read was clicked ", Toast.LENGTH_SHORT).show();
                break;}
        }

    }
}
