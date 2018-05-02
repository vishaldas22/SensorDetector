package excel.com.sensordetector.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import excel.com.sensordetector.R;
import excel.com.sensordetector.constant.AppConstant;
import excel.com.sensordetector.model.SensorModel;

public class IntruderDetectionService extends Service {
    private MediaPlayer mediaPlayer;
    @Override
    public void onCreate() {
        mediaPlayer  = new MediaPlayer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        readData();
        return START_STICKY;
    }

    private void readData() {
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              //  Toast.makeText(getApplicationContext(), "child added!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //Toast.makeText(getApplicationContext(),"value changed",Toast.LENGTH_SHORT).show();

                double distance = 0;
                String motion;


                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    SensorModel sensorModel = dataSnapshot.getValue(SensorModel.class);
                    distance = Double.parseDouble(sensorModel.getDistance());
                    motion = sensorModel.getMotion().trim();

                    if (distance < 10)
                    {
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                                .setContentTitle(AppConstant.ALERT)
                                .setContentText(AppConstant.INTRUDER_DETECTED)
                                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                        R.drawable.intru))
                                .setSmallIcon(R.drawable.intru)
                                .setSound(Uri.parse("android.resource://"
                                + getPackageName() + "/" + R.raw.intruder_alert))

                                .build();
                        notificationManager.notify(1, notification);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "No change", Toast.LENGTH_SHORT).show();
                    }

                    //SensorModel  users = dataSnapshot.getValue(SensorModel.class);
                    //Toast.makeText(getApplicationContext(), users.getEmail() + "\n" + users.getUsername(), Toast.LENGTH_SHORT).show();
                  //  String message = (String) messageSnapshot.child("message").getValue();
                }
                // long childCount = dataSnapshot.child(userId).getChildrenCount();

//                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//                Notification notification = new NotificationCompat.Builder(getApplicationContext())
//                        .setContentTitle(AppConstant.ALERT)
//                        .setContentText(AppConstant.INTRUDER_DETECTED)
//                        .setSmallIcon(android.R.drawable.stat_sys_warning)
////                        .setSound(Uri.parse("android.resource://"
////                                + getPackageName() + "/" + R.raw.intruder_alert))
//
//                        .build();
//                notificationManager.notify(1, notification);
//                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.intruder_alert);
//                mediaPlayer.setLooping(true);
//                mediaPlayer.start();
//
//               new CountDownTimer(60000,1000)
//                {
//                    public void onTick(long millisUntilFinished)
//                    {
//                    }
//
//                    public void onFinish()
//                    {
//                        stopAlert();
//                    }
//                }.start();


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void stopAlert() {
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }
}