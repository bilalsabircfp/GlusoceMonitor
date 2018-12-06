package com.bilal.dzone.medical_glucose.Student;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bilal.dzone.medical_glucose.Login.Login;
import com.bilal.dzone.medical_glucose.R;
import com.bilal.dzone.medical_glucose.Student.Edit_pass.Edit_Pass;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

public class SelectCategory extends AppCompatActivity implements View.OnClickListener {

    ImageView meal, news;
    Intent intent;
    ResideMenu resideMenu;
    SelectCategory mContext;
    ResideMenuItem edit_pass, logout;
    SharedPreferences sharedPreferences;
    String pref_id, pref_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        mContext = this;
        setUpMenu();
        sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        pref_id = sharedPreferences.getString("id", "");
        pref_pass = sharedPreferences.getString("pass", "");


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




    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(SelectCategory.this);
        resideMenu.setBackground(R.color.timestamp);
        resideMenu.attachToActivity(SelectCategory.this);
//        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        edit_pass = new ResideMenuItem(this, R.drawable.key1, "Change Pass");
        logout = new ResideMenuItem(this, R.drawable.logout_big ,  "Logout");

        resideMenu.addMenuItem(edit_pass, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(logout, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

        edit_pass.setOnClickListener(this);
        logout.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {

        if (view == edit_pass){

            resideMenu.closeMenu();
            Intent intent = new Intent(mContext, Edit_Pass.class);
            intent.putExtra("pass", pref_pass);
            intent.putExtra("id", pref_id);
            startActivity(intent);
        }
        else if (view == logout){

            logout();
        }
        else
            resideMenu.closeMenu();
    }



    private void logout(){

        //Creating an alert dialog to confirm logout

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Logout!");
        alertDialogBuilder.setIcon(R.drawable.failure);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Putting blank value to number
                        editor.putString("id", "");
                        editor.putString("cat", "");

                        //Saving the sharedpreferences
                        editor.clear();
                        editor.commit();
                        finish();

                        //Starting login activity
                        Intent intent = new Intent(mContext, Login.class);
                        startActivity(intent);
                        mContext.finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_) {

            resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);

            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
