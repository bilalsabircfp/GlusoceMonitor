package com.bilal.dzone.medical_glucose.Student;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bilal.dzone.medical_glucose.R;

public class SelectCategory extends AppCompatActivity {

    ImageView meal, news;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        meal = (ImageView) findViewById(R.id.meal);
        news  = (ImageView) findViewById(R.id.news);


        meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(SelectCategory.this, Student_Activity.class);
                intent.putExtra("type", "Meal");
                startActivity(intent);
            }
        });


        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(SelectCategory.this, Student_Activity.class);
                intent.putExtra("type", "News");
                startActivity(intent);
            }
        });

    }
}
