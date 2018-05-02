package excel.com.sensordetector;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyEmail extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Verify Email";

    TextView status;
    // [START declare_auth]
    private FirebaseAuth mAuth;

    FirebaseUser user;
    // [END declare_auth]

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        status = (TextView) findViewById(R.id.status);

        user = mAuth.getCurrentUser();

        Button verify = (Button) findViewById(R.id.verify);

        // Buttons
        verify.setOnClickListener(VerifyEmail.this);

        progressDialog = new ProgressDialog(this);
    }

    private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.verify).setEnabled(false);

        //if fields are not empty dialog appears
        progressDialog.setMessage("Verifying Please Wait...");
        progressDialog.show();

        // Send verification email
        // [START send_email_verification]

        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.verify).setEnabled(true);

                        if (task.isSuccessful()) {

                            Toast.makeText(VerifyEmail.this, "Verification email sent to " + user.getEmail() + "Please verify your email address to login", Toast.LENGTH_SHORT).show();


                                Intent i = new Intent(VerifyEmail.this, Login.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                (VerifyEmail.this).finish();
                                startActivity(i);
                                finish();

                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(VerifyEmail.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]

                    }
                });
//        if (user.isEmailVerified()){
//            progressDialog.dismiss();
//            status.setText("Email is verified!");
//            Intent i = new Intent(VerifyEmail.this,MainActivity.class);
//            startActivity(i);
//        }
//        else
//        {
//            status.setText("Email is not verified" + "\n Click again!!!");
//        }

        // [END send_email_verification]
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.verify) {
            sendEmailVerification();
        }
    }
}
