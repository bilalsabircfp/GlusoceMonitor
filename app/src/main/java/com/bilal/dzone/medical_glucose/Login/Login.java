package com.bilal.dzone.medical_glucose.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bilal.dzone.medical_glucose.JsonParsing.Check_internet_connection;
import com.bilal.dzone.medical_glucose.JsonParsing.JsonParser;
import com.bilal.dzone.medical_glucose.Admin.Admin_Activity;
import com.bilal.dzone.medical_glucose.R;
import com.bilal.dzone.medical_glucose.Registeration.Register;
import com.bilal.dzone.medical_glucose.Registeration.SelectCategoryRegisteration;
import com.bilal.dzone.medical_glucose.Student.SelectCategory;
import com.bilal.dzone.medical_glucose.Student.Student_Activity;
import com.bilal.dzone.medical_glucose.Student.Student_Activity_old;
import com.bilal.dzone.medical_glucose.URL.Url;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    TextView create_acc;
    Intent intent;
    SharedPreferences sharedPreferences;
    boolean server_check = false;
    JSONObject jp_obj;
    JSONArray jar_array;
    EditText et_id, et_password;
    String id, password, tag;
    Button btn_login;
    RadioButton admin, student;
    RadioGroup radioGroup;
    String user_id, dept, pass, refreshedToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("tag", "Refreshed token: " + refreshedToken);
//        Toast.makeText(this, refreshedToken, Toast.LENGTH_SHORT).show();

        radioGroup = (RadioGroup) findViewById(R.id.r_group);
        create_acc = (TextView) findViewById(R.id.create_account);
        admin = (RadioButton) findViewById(R.id.teacher);
        student = (RadioButton) findViewById(R.id.student);
        et_id = (EditText) findViewById(R.id.id);
        et_password = (EditText) findViewById(R.id.pass);
        btn_login = (Button) findViewById(R.id.btn);

        create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(Login.this,SelectCategoryRegisteration.class);
                startActivity(intent);
            }
        });


        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag = "admin";
            }
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag = "student";
            }
        });

        Button_Clicks();

    }

    //ONClick Listners////////////////////////////////////////
    public void Button_Clicks() {


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                id    = et_id.getText().toString().trim();
                password = et_password.getText().toString().trim();


                if ((et_id.getText().toString().length() == 0))
                    et_id.setError("Enter ID");

                else if ((et_password.getText().toString().length() == 0))
                    et_password.setError("Enter Password");

                else if (radioGroup.getCheckedRadioButtonId() == -1)
                    student.setError("Select Catagory");

                else if (tag.equals(""))
                    Toast.makeText(Login.this, "Sign In as Teacher or Student", Toast.LENGTH_SHORT).show();

                else {

                    if(new Check_internet_connection(getApplicationContext()).isNetworkAvailable()){

                        new LOginUser().execute();

                    }
                    else {

                        Toast.makeText(getApplicationContext(),
                                "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }


                }

            }
        });


    }


    String server_response = "0", server_response_text;

    //ASYNTASK JSON//////////////////////////////////////////
    public class LOginUser extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setTitle("Loading! Be Patient!");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "login");

                obj.put("id", id);
                obj.put("password", password);
                obj.put("tag", tag);


                String str_req = JsonParser.multipartFormRequestForFindFriends(Url.ulr, "UTF-8", obj, null);

                jp_obj = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;

                c = jar_array.getJSONObject(0);

                if (c.length() > 0) {

                    server_response = c.getString("response");

                    if (server_response.equals("0")) {
                        server_response_text = c.getString("response-text");

                    }
                }


                if (server_response.equals("1")) {

                    c = jar_array.getJSONObject(1);

                    if (c.length() > 0 ) {

                        user_id  = c.getString("id");
                        dept  = c.getString("dept");
                        pass  = c.getString("pass");

                    }

                }


                server_check = true;

            } catch (Exception e) {
                e.printStackTrace();

                //server response/////////////////////////
                server_check = false;
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            progressDialog.dismiss();


            if (server_check = true) {

                if (server_response.equals("1")) {


                    //Creating a shared preference
                    sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);

                    //Creating editor to store values to shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    //Adding values to editor
                    editor.putString("cat", tag);
                    editor.putString("id", user_id);
                    editor.putString("dept", dept);
                    editor.putString("pass", pass);

                    //Saving values to editor
                    editor.commit();


                    if (tag.equals("admin")) {

                        intent = new Intent(Login.this, Admin_Activity.class);
                        startActivity(intent);
                        finish();

                    } else if (tag.equals("student")) {

                        //update token
                        new Update_Token().execute();
                        intent = new Intent(Login.this, SelectCategory.class);
                        startActivity(intent);
                        finish();
                    }
                    else {

                        Toast.makeText(Login.this, "-_-", Toast.LENGTH_SHORT).show();
                    }



                } else {
                    Toast.makeText(Login.this, server_response_text, Toast.LENGTH_SHORT).show();

                }


            } else {

                Toast.makeText(Login.this, "Error while loading data", Toast.LENGTH_SHORT).show();
            }

        }
    }



    //Update App token//////////////////////////////////////////
    public class Update_Token extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setTitle("Loading! Be Patient!");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "app_token");

                obj.put("id", id);
                obj.put("token", refreshedToken);

                String str_req = JsonParser.multipartFormRequestForFindFriends(Url.ulr, "UTF-8", obj, null);

                jp_obj = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;

                c = jar_array.getJSONObject(0);

                if (c.length() > 0) {

                    server_response = c.getString("response");

                    if (server_response.equals("0")) {
                        server_response_text = c.getString("response-text");

                    }
                }


                if (server_response.equals("1")) {

                }


                server_check = true;

            } catch (Exception e) {
                e.printStackTrace();

                //server response/////////////////////////
                server_check = false;
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            progressDialog.dismiss();


            if (server_check = true) {

                if (server_response.equals("1")) {



                } else {
                    Toast.makeText(Login.this, server_response_text, Toast.LENGTH_SHORT).show();

                }


            } else {

                Toast.makeText(Login.this, "Error while loading data", Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        //In onresume fetching value from sharedpreference
        sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);

        if (sharedPreferences.getString("id", "").equals("")) {

//            Toast.makeText(this, "No Data Here", Toast.LENGTH_SHORT).show();

        } else if (sharedPreferences.getString("cat", "").equals("admin")) {

//            Toast.makeText(this, "Data Found", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this, Admin_Activity.class);
            startActivity(intent);
            Login.this.finish();

        } else {

            Intent intent = new Intent(Login.this, Student_Activity.class);
            startActivity(intent);
            Login.this.finish();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
