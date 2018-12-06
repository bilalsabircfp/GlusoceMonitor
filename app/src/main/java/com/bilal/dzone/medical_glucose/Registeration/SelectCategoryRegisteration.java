package com.bilal.dzone.medical_glucose.Registeration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bilal.dzone.medical_glucose.R;
import com.bilal.dzone.medical_glucose.Student.Student_Activity;

public class SelectCategoryRegisteration extends AppCompatActivity {

    ImageView doctor, patient;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_registeration);

        doctor = (ImageView) findViewById(R.id.meal);
        patient = (ImageView) findViewById(R.id.news);


        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(SelectCategoryRegisteration.this, DoctorRegister.class);
                startActivity(intent);
            }
        });


        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(SelectCategoryRegisteration.this, Register.class);
                startActivity(intent);
            }
        });

    }
}
