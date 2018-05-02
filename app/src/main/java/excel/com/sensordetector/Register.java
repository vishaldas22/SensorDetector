package excel.com.sensordetector;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegister;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;
    private EditText mAddress;
    private EditText mPhone;
    private EditText mConfirmPassword;
    private TextView mLinkLogin;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
//    private DatabaseReference databaseReference;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        //databaseReference = FirebaseDatabase.getInstance().getReference();

        btnRegister = (Button) findViewById(R.id.btn_signup);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mName = (EditText) findViewById(R.id.input_name);
        mAddress = (EditText) findViewById(R.id.input_address);
        mPhone = (EditText) findViewById(R.id.input_mobile);
        mConfirmPassword = (EditText) findViewById(R.id.input_reEnterPassword);
        mLinkLogin = (TextView) findViewById(R.id.link_login);

        progressDialog = new ProgressDialog(this);

        btnRegister.setOnClickListener(this);

//        if (mAuth.getCurrentUser() != null){
//            finish();
//            Toast.makeText(getApplicationContext(), "Successfully Logged in", Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(Register.this, Login.class);
//            startActivity(i);
//        }
    }

//    private void saveUserInformation(){
//        String name = mName.getText().toString().trim();
//        String address = mAddress.getText().toString().trim();
//        String email = mEmail.getText().toString().trim();
//        String mobile = mPhone.getText().toString().trim();
//        String password= mPassword.getText().toString().trim();
//
//        UserInformation userInformation = new UserInformation(name,address,email,mobile,password);
//        user = mAuth.getCurrentUser();
//        databaseReference.child(user.getUid()).setValue(userInformation);
//    }

    private boolean validateName()
    {
        String name = mName.getText().toString().trim();
         if (name.isEmpty())
         {
             mName.setError("Name cannot be left empty");
             return false;
         }
         else if (name.length() > 15){
             mName.setError("Name cannot be more then 15 characters");
             return false;
         }
         else {
             mName.setError(null);
             return true;
         }

    }
    private boolean validatePhone()
    {
        String phone = mPhone.getText().toString().trim();
        if (phone.isEmpty())
        {
            mPhone.setError("Field cannot be empty");
            return false;
        }
        else if (phone.length() > 10)
        {
            mPhone.setError("Phone number cannot be more then 10 characters");
            return false;
        }
        else
        {
            mPhone.setError(null);
            return true;
        }
    }

    private boolean validateAddress()
    {
        String address = mAddress.getText().toString().trim();
        if (address.isEmpty())
        {
            mAddress.setError("Field cannot be left empty");
            return false;
        }
        else {
            mAddress.setError(null);
            return true;
        }
    }


    private boolean validatePassword(){
        String password = mPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString().trim();
        if (password.isEmpty()| confirmPassword.isEmpty()){
            mPassword.setError("Field cannot be empty");
            mConfirmPassword.setError("Field cannot be empty");
            return false;
        }
        else if (password.length() > 10 | password.length() < 6 | confirmPassword.length() > 10 | confirmPassword.length() < 6){
            mPassword.setError("Password should be between 6 to 10 characters");
            mConfirmPassword.setError("Password should be between 6 to 10 characters");
            return  false;
        }
        else if (!password.matches(confirmPassword)){
            mPassword.setError("Passwords do not match");
            mConfirmPassword.setError("Passwords do not match");
            return false;
        }
        else{
            mPassword.setError(null);
            return true;
        }
    }

    private boolean validateEmail(){
        String email = mEmail.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.isEmpty()){
            mEmail.setError("Field cannot be empty");
            return false;
        }
        else if (!email.matches(emailPattern))
        {
            mEmail.setError("Not a valid email");
            return false;
        }
        else{
            mEmail.setError(null);
            return true;
        }
    }

    public void registerUser()
    {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String address = mAddress.getText().toString().trim();


        //checking if email and passwords are empty
        if(!validateEmail() | !validatePassword() | !validateName() | !validateAddress() | !validatePhone()){
            //Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    progressDialog.dismiss();
                    sendEmailVerification();
                    user = mAuth.getCurrentUser();

//                    finish();
//                    Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(Register.this, VerifyEmail.class);
//                    startActivity(i);
                }else
                {
                    Toast.makeText(getApplicationContext(), "Registration Unsuccessful" + "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null)
        {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Register.this, "Check your email for verification", Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
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
            Snackbar.make(view, "You are not connected to Internet", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            //Toast.makeText(Login.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
        }
//        else{
//            Snackbar.make(view, "You are not connected to Internet", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            //Toast.makeText(Login.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
//        }
    }


    @Override
    public void onClick(View v) {
        if(v == btnRegister)
        {
            checkConnection(v);
            registerUser();
            //saveUserInformation();
        }
        if (v == mLinkLogin){
            Intent i = new Intent(Register.this, Login.class);
            startActivity(i);
        }
    }
}
