package com.example.firedroid2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class yarab extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    GoogleSignInApi mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    Button fuckit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yarab);
        fuckit = findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("880731044741-uckfss5giodna805o8hf5qttgfp16ea9.apps.googleusercontent.com")
                //880731044741-nndmgb1o2l7d2do8ukqpnmhvtnt3gejk.apps.googleusercontent.com
                                 //880731044741-nndmgb1o2l7d2do8ukqpnmhvtnt3gejk.apps.googleusercontent.com
                                 //880731044741-uckfss5giodna805o8hf5qttgfp16ea9.apps.googleusercontent.com
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        fuckit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {

    }

    private void signIn() {
        System.out.println("Inside Signin");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent( mGoogleApiClient);
        System.out.println(signInIntent);

        startActivityForResult(signInIntent, RC_SIGN_IN);
        System.out.println("LOOL");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        //Toast.makeText(yarab.this, "WTF +currentUser.getDisplayName()", Toast.LENGTH_LONG).show();

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            //Toast.makeText(yarab.this, "success : " + user.getDisplayName(), Toast.LENGTH_LONG).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                            //Toast.makeText(yarab.this, "Faild in onComplete", Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });

    }

    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
            Toast.makeText(yarab.this, user.getUid(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(yarab.this, "Faild in onComplete", Toast.LENGTH_LONG).show();
        }
    }
}
