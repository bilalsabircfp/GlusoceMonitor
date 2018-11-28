package com.bilal.dzone.medical_glucose.Splash;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bilal.dzone.medical_glucose.Login.Login;
import com.bilal.dzone.medical_glucose.Admin.Admin_Activity;
import com.bilal.dzone.medical_glucose.R;
import com.bilal.dzone.medical_glucose.Student.SelectCategory;
import com.bilal.dzone.medical_glucose.Student.Student_Activity;
import com.bilal.dzone.medical_glucose.Student.Student_Activity_old;

import org.w3c.dom.Text;

public class Splash extends AppCompatActivity {

    Intent myintent;
    ImageView imageView;
    SharedPreferences sharedPreferences;
    ProgressBar pb;
    int pgstatus;
    TextView tv;
    Handler hd = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView = (ImageView) findViewById(R.id.donut_progress);
        tv = (TextView) findViewById(R.id.textView) ;
        pb = (ProgressBar) findViewById(R.id.progressBar) ;


        pgstatus = 0;
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while(pgstatus < 100){
                    pgstatus += 1;
                    hd.post(new Runnable(){
                        public void run(){
                            pb.setProgress(pgstatus);
                            tv.setText("Loading :" + pgstatus + "/" + pb.getMax());
                        }
                    });
                    try{
                        Thread.sleep(30);

                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        PropertyValuesHolder donutAlphaProperty = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        PropertyValuesHolder donutProgressProperty = PropertyValuesHolder.ofInt("donut_progress", 0, 100);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(imageView, donutAlphaProperty, donutProgressProperty);
        animator.setDuration(3500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();


    }


    @Override
    protected void onResume() {
        super.onResume();

        //In onresume fetching value from sharedpreference
        sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);

        if (sharedPreferences.getString("id", "").equals("")) {


            myintent = new Intent(this, Login.class);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(myintent);
                    finish();
                }
            }, 4000);


        } else if (sharedPreferences.getString("cat", "").equals("admin")) {

//            Toast.makeText(this, "Data Found", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Admin_Activity.class);
            startActivity(intent);
            this.finish();

        } else {

            Intent intent = new Intent(this, SelectCategory.class);
            startActivity(intent);
            this.finish();
        }
    }
}