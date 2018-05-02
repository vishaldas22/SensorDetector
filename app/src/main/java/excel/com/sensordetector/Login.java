package excel.com.sensordetector;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

import excel.com.sensordetector.activity.HomeActivity;

public class Login  extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Login";
    private static final int RC_SIGN_IN = 1;

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLogin;
    private TextView mLinkSignUp;
    private ImageView image;
    private ProgressDialog progressDialog;
//    private Button googleSignIn;
//    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // [START declare_auth]
    private FirebaseAuth mAuth;

    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        image = (ImageView) findViewById(R.id.image);
        mEmailField = findViewById(R.id.input_email);
        mPasswordField = findViewById(R.id.input_password);

        // Buttons
        mLogin = (Button)findViewById(R.id.btn_login);
        mLinkSignUp = (TextView) findViewById(R.id.link_signup);
        //googleSignIn = (Button) findViewById(R.id.googleSignIn);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                startActivity(new Intent(Login.this, HomeActivity.class));
            }
        };

//        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        //mGoogleSignInClient = googleSignIn.getClient(this,gso);
//
//        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
//                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//                        Toast.makeText(Login.this, "You got an error", Toast.LENGTH_SHORT).show();
//                    }
//                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//
//        googleSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkConnection(v);
//                progressDialog.setMessage("Please Wait...");
//                progressDialog.show();
//                signIn();
//            }
//        });

        mLogin.setOnClickListener(this);
        mLinkSignUp.setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        // [END initialize_auth]

        progressDialog = new ProgressDialog(this);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    Intent i = new Intent(Login.this, HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    (Login.this).finish();
                    startActivity(i);
                }
            }
        };



    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void checkConnection(View view){
        if(!isOnline()){
            Snackbar.make(view, "You are not connected to Internet", Snackbar.LENGTH_LONG).setAction("Action", null).show();}
            //Toast.makeText(Login.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
//        }else{
//            Snackbar.make(view, "You are not connected to Internet", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            //Toast.makeText(Login.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
//        }
    }

//    public void onBackPressed()
//    {
//        progressDialog.dismiss();
//    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);
    }

//    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
//                // ...
//            }
//        }
//    }

//    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            //updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(Login.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
//                            //updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }

    private boolean validatePassword(){
        String password = mPasswordField.getText().toString().trim();
        if (password.isEmpty()){
            mPasswordField.setError("Field cannot be empty");
            return false;
        }
        else if (password.length() < 6){
            mPasswordField.setError("Password should be between 6 to 15 characters");
            return  false;
        }
        else{
            mPasswordField.setError(null);
            return true;
        }
    }

    private boolean validateEmail(){
        String email = mEmailField.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.isEmpty()){
            mEmailField.setError("Field cannot be empty");
            return false;
        }
        else if (!email.matches(emailPattern))
        {
            mEmailField.setError("Not a valid email");
            return false;
        }
        else{
            mEmailField.setError(null);
            return true;
        }
    }

    public void userLogin(View view)
    {
        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();
        final FirebaseUser user = mAuth.getCurrentUser();
        //checking if email and passwords are empty
        if(!validateEmail() | !validatePassword()){
            return;
        }

        //if fields are not empty dialog appears
        progressDialog.setMessage("Authenticating Please Wait...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (!task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "User does not exist" + "\n Incorrect Email Id or password", Toast.LENGTH_SHORT).show();

                }else
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Successfully Logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Login.this, HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    checkIfEmailVerified();
                }
            }
        });
    }

    private void checkIfEmailVerified()
    {
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        boolean emailVerified = users.isEmailVerified();
        if (!emailVerified)
        {
            Toast.makeText(getApplicationContext(), "Verify your email address", Toast.LENGTH_LONG).show();
            mAuth.signOut();
            finish();
        }
        else {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        int i = v.getId();
         if (i == R.id.btn_login) {
             checkConnection(v);
             userLogin(v);
        }
        else if (i==R.id.link_signup)
        {
            Intent intent = new Intent(Login.this, Register.class);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, image, "robot");
            startActivity(intent, options.toBundle());
        }
    }
}
