package excel.com.sensordetector.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import excel.com.sensordetector.Login;
import excel.com.sensordetector.R;
import excel.com.sensordetector.adapter.SensorAdapter;
import excel.com.sensordetector.constant.AppConstant;
import excel.com.sensordetector.model.SensorModel;
import excel.com.sensordetector.service.IntruderDetectionService;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference databaseReference;
    private String userId;
    private ListView lvHistory;
    private LinearLayout llStartDate;
    private LinearLayout llEndDate;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private Button btnShowHistory;
    private int year;
    private int month;
    private int date;
    private Calendar calendar;
    private SensorAdapter sensorAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;

    private static final String FILE_NAME = "example.txt";

    private SensorAdapter adapter;

   // List<SensorModel> sensorModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initControls();
        setListener();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        //userId = "222";
        // writeNewUser("Steve Rogers","rogers@sheild.com");
        Intent intent = new Intent(this, IntruderDetectionService.class);
        startService(intent);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            Intent i = new Intent(HomeActivity.this, Login.class);
            startActivity(i);
        }

    }

    private void clearList(){
        lvHistory.setAdapter(null);
    }


    private void save()
    {
        String distance ;
        String motion;
        String temperature;

        SensorModel sensorModel = new SensorModel();
        distance = sensorModel.getDistance();
        motion = sensorModel.getMotion();
        temperature = sensorModel.getTemperature();

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(distance.getBytes());
            fos.write(motion.getBytes());
            fos.write(temperature.getBytes());
            //mEditText.getText().clear();
            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout)
        {
            logOut();
        }else if (id == R.id.clear){
            clearList();
        }
        else if (id == R.id.save){
            //save();
        }
        return super.onOptionsItemSelected(item);
    }

//    public void save(DataSnapshot dataSnapshot) {
//
//        String distance ;
//        String motion;
//        String temperature;
//
//        for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()){
//            SensorModel sensorModel = dataSnapshot.getValue(SensorModel.class);
//            distance = sensorModel.getDistance();
//            motion = sensorModel.getMotion();
//            temperature = sensorModel.getTemperature();
//
//            FileOutputStream fos = null;
//
//            try {
//                fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
//                fos.write(distance.getBytes());
//                fos.write(motion.getBytes());
//                fos.write(temperature.getBytes());
//                //mEditText.getText().clear();
//                Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (fos != null) {
//                    try {
//                        fos.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }





    //}

    //    private void writeNewUser(String name, String email) {
//        User user = new User(name, email);
//
//        databaseReference.child("temperature").child(userId).setValue(user);
//        //  readData();
//    }

    public void logOut()
    {
        progressDialog.setMessage("Logging Out...");
        progressDialog.show();
        firebaseAuth.signOut();
        Intent i = new Intent(HomeActivity.this, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        (HomeActivity.this).finish();
        startActivity(i);
    }

    private void setListener() {

        llStartDate.setOnClickListener(this);
        llEndDate.setOnClickListener(this);
        btnShowHistory.setOnClickListener(this);
    }

    private void initControls() {
        lvHistory = (ListView) findViewById(R.id.lv_alert_list);
        btnShowHistory = (Button) findViewById(R.id.btn_show_history);
        tvStartDate = (TextView) findViewById(R.id.tv_start_date);
        tvEndDate = (TextView) findViewById(R.id.tv_end_date);
        llStartDate = (LinearLayout) findViewById(R.id.ll_start_date);
        llEndDate = (LinearLayout) findViewById(R.id.ll_end_date);
        calendar = Calendar.getInstance();
    }

//    private void writeNewUser(String name, String email) {
//        // SensorModel user = new SensorModel(name, email);
//
//        //databaseReference.child("temperature").child(userId).setValue(user);
//        //  readData();
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_show_history:
                showAlertHistory();
                break;
            case R.id.ll_start_date:
                showPickerDialog(llStartDate);
                break;
            case R.id.ll_end_date:
                showPickerDialog(llEndDate);
                break;
        }
    }

    private void showAlertHistory() {
        final List<SensorModel> sensorModelList = new ArrayList<>();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    SensorModel sensorModel = childSnapshot.getValue(SensorModel.class);
                    //writeFileOnInternalStorage(HomeActivity.this, "myfile", "" + sensorModelList);
                    sensorModelList.add(sensorModel);
                }
                sensorAdapter = new SensorAdapter(HomeActivity.this, sensorModelList);
                lvHistory.setAdapter(sensorAdapter);



            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.child(AppConstant.SENSOR_DATA).addValueEventListener(postListener);

        //save();
        //writeFileOnInternalStorage(HomeActivity.this, "myfile", "" + sensorModelList);

    }

//    public void writeFileOnInternalStorage(Context context,String FileName,String Body){
//        File file = new File(context.getFilesDir(), "mydir");
//        if (!file.exists()){
//            boolean seen = file.mkdir();
//            if ( !seen){
//                Toast.makeText(HomeActivity.this, "Could not make directory", Toast.LENGTH_SHORT).show();
//            }
//            else
//            {
//                Toast.makeText(HomeActivity.this, "Yaay its created", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        try{
//            File gpxfile = new File(file,FileName);
//            FileWriter writer = new FileWriter(gpxfile);
//            writer.append(Body);
//            writer.flush();
//            writer.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }


    private void showPickerDialog(final View dateView) {
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int years, int monthOfYear, int dayOfMonth) {

                year = years;
                month = monthOfYear + 1;
                date = dayOfMonth;
                StringBuilder sb = new StringBuilder();

                String strMonth = "", strDate = "";
                if (date < 10)
                    strDate = "0" + date;
                else
                    strDate = "" + date;
                if (month < 10)
                    strMonth = "0" + month;
                else
                    strMonth = "" + month;

                sb.append(strDate);
                sb.append("/");
                sb.append(strMonth);
                sb.append("/");
                sb.append(year);
                if (dateView.getId() == R.id.ll_start_date) {
                    tvStartDate.setText(sb.toString());
                }
                if (dateView.getId() == R.id.ll_end_date) {
                    tvEndDate.setText(sb.toString());
                }

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        dpd.show();

    }
}
