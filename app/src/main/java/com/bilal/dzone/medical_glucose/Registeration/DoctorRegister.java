package com.bilal.dzone.medical_glucose.Registeration;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bilal.dzone.medical_glucose.JsonParsing.Check_internet_connection;
import com.bilal.dzone.medical_glucose.JsonParsing.JsonParser;
import com.bilal.dzone.medical_glucose.Login.Login;
import com.bilal.dzone.medical_glucose.R;
import com.bilal.dzone.medical_glucose.URL.Url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

public class DoctorRegister extends AppCompatActivity {

    boolean server_check=false;

    RadioGroup radioGroup;
    RadioButton male
            ,female;

    String s_name, hospitalName_, specility_
            ,s_number
            ,s_id
            ,s_pass
            ,s_con_pass
            ,s_email
            ,s_dept
            ,gender
            ,code1_
            ,code2_
            ,code3_
            ,code4_;

    JSONObject jp_obj;

    JSONArray jar_array;

    Intent intent;

    EditText number, hospitalName, specility
            ,name
            ,password
            ,con_pass
            ,id
            ,email
            ,code1
            ,code2
            ,code3
            ,code4;
    Spinner dept;
    int[] code = new int[4];
    Random rand = new Random();
    String sms_code, refreshedToken;
    ArrayAdapter<CharSequence> adapter; String catagory;
    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_register);


        name             = (EditText) findViewById(R.id.name);
        number           = (EditText) findViewById(R.id.number);
        password         = (EditText) findViewById(R.id.pass);
        radioGroup       = (RadioGroup) findViewById(R.id.r_group);
        male             = (RadioButton) findViewById(R.id.male);
        female           = (RadioButton) findViewById(R.id.female);
        con_pass         = (EditText) findViewById(R.id.con_pass);
        dept             = (Spinner) findViewById(R.id.spinner1);
        id               = (EditText) findViewById(R.id.id);
        email            = (EditText) findViewById(R.id.email);
        hospitalName     = (EditText) findViewById(R.id.hospital);
        specility        = (EditText) findViewById(R.id.speciality);

        btn_register     = (Button) findViewById(R.id.btn_register);



        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(DoctorRegister.this,
                R.array.depts, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dept.setAdapter(adapter);

        dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {

                if(id == 0){
                    catagory = "BS(CS)";
                }else if(id == 1){
                    catagory = "BBA";
                }else if(id == 2){
                    catagory = "MS(CS)";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });




        Btn_ClickListner();

    }


    public void Btn_ClickListner(){

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                s_name         = name.getText().toString().trim();
                s_number       = number.getText().toString().trim();
                s_pass         = password.getText().toString().trim();
                s_con_pass     = con_pass.getText().toString().trim();
                s_dept         = catagory;
                s_email        = email.getText().toString().trim();
                hospitalName_  = hospitalName.getText().toString().trim();
                specility_     = specility.getText().toString().trim();

                if(male.isChecked()){
                    gender = "Male";
                }
                else
                    gender = "Female";



                if ((name.getText().toString().length() == 0)) {
                    name.setError("Enter Name");
                }
                else if (radioGroup.getCheckedRadioButtonId() == -1) {
                    male.setError("Select Gender");
                }
                else if ((email.getText().toString().length() == 0)) {
                    email.setError("Enter Email");
                }
                else if (!s_email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                    email.setError("Invalid Email Address");
                }
                else if ((hospitalName.getText().toString().length() == 0)) {
                    hospitalName.setError("Enter Hospital Name");
                }
                else if ((specility.getText().toString().length() == 0)) {
                    specility.setError("Enter Speciality");
                }
                else if ((number.getText().toString().length() == 0)) {
                    number.setError("Enter Number");
                }
                else if ((password.getText().toString().length() == 0)) {
                    password.setError("Enter Password");
                }
                else if ((con_pass.getText().toString().length() == 0)) {
                    con_pass.setError("Enter Password Again");
                }
                else if(!s_pass.equals(s_con_pass)){
                    Toast.makeText(DoctorRegister.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                }

                else {



                     if(new Check_internet_connection(getApplicationContext()).isNetworkAvailable()){

                         new RegisterUser().execute();

                     }
                     else {

                         Toast.makeText(getApplicationContext(),
                                 "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();

                     }

                    //Creating an alert dialog to ask send sms
//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Register.this);
//
//                    alertDialogBuilder.setTitle("Send SMS!");
//                    alertDialogBuilder.setCancelable(false);
//                    alertDialogBuilder.setIcon(R.drawable.logo);
//                    alertDialogBuilder.setMessage("Send Verification Code to " + "\t" + s_number);
//                    alertDialogBuilder.setPositiveButton("Yes",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface arg0, int arg1) {
//
//                                    if (ActivityCompat.checkSelfPermission(Register.this, Manifest.permission.SEND_SMS)
//                                            != PackageManager.PERMISSION_GRANTED) {
//                                        // Check Permissions Now
//                                        ActivityCompat.requestPermissions(Register.this,
//                                                new String[]{Manifest.permission.SEND_SMS},
//                                                1);
//                                    }
//                                    else {
//
//                                        send_sms();
//                                        Sms_Verification_Dialog();
//
//                                    }
//                                }
//                            });
//
//                    alertDialogBuilder.setNegativeButton("No",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface arg0, int arg1) {
//
//                                    Toast.makeText(Register.this, "Kindly complete the process to complete Registeration!", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//
//                    //Showing the alert dialog
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();



                }
            }
        });

    }


    String server_response="0"
            ,server_response_text;
    //ASYNTASK REGISTER USER////////////////////////////////////
    public class RegisterUser extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(DoctorRegister.this);
            progressDialog.setTitle("Loading! Be Patient!");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj     = new JSONObject();

                obj.put("operation","register");

                obj.put("tag", "admin");
                obj.put("number", s_number);
                obj.put("password", s_pass);
                obj.put("name", s_name);
                obj.put("gender", gender);
                obj.put("id", s_email);
                obj.put("dept", "kaka");
                obj.put("email", s_email);
                obj.put("token", refreshedToken);


                String str_req = JsonParser.multipartFormRequestForFindFriends(Url.ulr , "UTF-8", obj,null);

                jp_obj    = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;

                c = jar_array.getJSONObject(0);

                if(c.length()>0){

                    server_response    = c.getString("response");

                    if(server_response.equals("0")){
                        server_response_text    = c.getString("response-text");

                    }
                }

                server_check=true;

            } catch (Exception e) {
                e.printStackTrace();

                //server response/////////////////////////
                server_check =false;
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            progressDialog.dismiss();

            if(server_check){

                if(server_response.equals("1")){

                    Toast.makeText(DoctorRegister.this, "User Added Succesfully",
                            Toast.LENGTH_SHORT).show();

                    DoctorRegister.this.finish();
                    intent = new Intent(DoctorRegister.this, Login.class);
                    startActivity(intent);

                }else {
                    Toast.makeText(DoctorRegister.this, server_response_text, Toast.LENGTH_SHORT).show();

                }

            }else {

                Toast.makeText(DoctorRegister.this, "Error while loading data", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public void send_sms(){

        try {

            Generate_Code();

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(s_number, null, sms_code, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    public void Generate_Code(){

        //generate random number for number verification
        for (int j=0; j < code.length; j++)
        {
            int code_ = rand.nextInt(9) + 1;
            code[j] = code_;
        }

        sms_code = Arrays.toString(code).replaceAll("\\[|\\]|,|\\s", "");
//        Toast.makeText(this, sms_code, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, code[0] + "" + code[1] + "" + code[2] + "" + code[3], Toast.LENGTH_SHORT).show();

    }


    public void Sms_Verification_Dialog(){

        //CUSTOM DIALOG///////////////////////////////
        final Dialog dialog = new Dialog(DoctorRegister.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_sms_verification);

        Button btn_cross = (Button) dialog.findViewById(R.id.cross);
        Button submit    = (Button) dialog.findViewById(R.id.submit);
        code1   = (EditText) dialog.findViewById(R.id.code1);
        code2   = (EditText) dialog.findViewById(R.id.code2);
        code3   = (EditText) dialog.findViewById(R.id.code3);
        code4   = (EditText) dialog.findViewById(R.id.code4);


        //change focus from one field to another
        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //
                if(editable.length()>0){
                    code1.clearFocus();
                    code2.requestFocus();
                    code2.setCursorVisible(true);
                }
            }
        });

        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //
                if(editable.length() > 0) {
                    code2.clearFocus();
                    code3.requestFocus();
                    code3.setCursorVisible(true);
                }
            }
        });

        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0) {
                    code3.clearFocus();
                    code4.requestFocus();
                    code4.setCursorVisible(true);
                }
            }
        });
        //////////////////////////////////////


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                code1_ = code1.getText().toString();
                code2_ = code2.getText().toString();
                code3_ = code3.getText().toString();
                code4_ = code4.getText().toString();

                String result = code1_ + code2_ + code3_ + code4_;

//                if(result.equals(sms_code)){
//
//                    Toast.makeText(Register.this, "Number Verified", Toast.LENGTH_SHORT).show();


                    if(new Check_internet_connection(getApplicationContext()).isNetworkAvailable()){

                        new RegisterUser().execute();

                    }
                    else {

                        Toast.makeText(getApplicationContext(),
                                "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
//                    }

//                    dialog.dismiss();
                }
//                else {
//
//                    Toast.makeText(Register.this, "Wrong Code Try Again", Toast.LENGTH_SHORT).show();
//                }

                Toast.makeText(DoctorRegister.this, result, Toast.LENGTH_SHORT).show();
            }
        });

        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}