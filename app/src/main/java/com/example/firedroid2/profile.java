package com.example.firedroid2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class profile extends AppCompatActivity {
    TextView nameView, emailView;
    Button logout;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    ImageView picView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        logout = findViewById(R.id.signOutButton);
        nameView = findViewById(R.id.userName);
        emailView = findViewById(R.id.userEmail);
        picView = findViewById(R.id.userPic);


        String name = getIntent().getStringExtra("userName");
        String email = getIntent().getStringExtra("userEmail");
        String picURL = getIntent().getStringExtra("userPicURL");


        nameView.setText(name);
        emailView.setText(email);
        //picView.setImageBitmap(doInBackground(picURL));
        Picasso.get().load(picURL).into(picView);

        mAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("880731044741-uckfss5giodna805o8hf5qttgfp16ea9.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUser.getPhotoUrl();
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signOut();

            }
        });


        //System.err.println(picURL);
       // System.err.println(email);
        System.err.println(picURL);
        System.out.println(currentUser.getPhoneNumber());

    }

    private void signOut() {
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(profile.this, MainActivity.class);
                        startActivity(i);
                    }
                }
        );
    }

}

