package com.bilal.dzone.medical_glucose.Student.Edit_pass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bilal.dzone.medical_glucose.JsonParsing.Check_internet_connection;
import com.bilal.dzone.medical_glucose.JsonParsing.JsonParser;
import com.bilal.dzone.medical_glucose.Login.Login;
import com.bilal.dzone.medical_glucose.R;
import com.bilal.dzone.medical_glucose.Registeration.Register;
import com.bilal.dzone.medical_glucose.Student.Student_Activity;
import com.bilal.dzone.medical_glucose.URL.Url;

import org.json.JSONArray;
import org.json.JSONObject;

public class Edit_Pass extends AppCompatActivity {

    String intent_pass, old_pass, new_pass, con_pass, intent_id;
    EditText et_old_pass, et_new_pass, et_con_pass;
    Button submit, update;
    LinearLayout l1, l2;
    boolean server_check=false;
    JSONObject jp_obj;
    JSONArray jar_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__pass);

        intent_pass = getIntent().getStringExtra("pass");
        intent_id = getIntent().getStringExtra("id");

        et_old_pass = (EditText)findViewById(R.id.password);
        et_new_pass = (EditText)findViewById(R.id.new_password);
        et_con_pass = (EditText)findViewById(R.id.con_password);
        submit = (Button)findViewById(R.id.submit_);
        update = (Button)findViewById(R.id.update);
        l1 = (LinearLayout)findViewById(R.id.l1);
        l2 = (LinearLayout)findViewById(R.id.l2);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                old_pass = et_old_pass.getText().toString();

                if(old_pass.equals(intent_pass)){

                    if ((et_old_pass.getText().toString().length() == 0))
                        et_old_pass.setError("Enter Password");

                    l1.setVisibility(View.GONE);
                    l2.setVisibility(View.VISIBLE);
                }
                else {

                    Toast.makeText(Edit_Pass.this, "Wrong Password Try Again", Toast.LENGTH_SHORT).show();
                }

            }
        });



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new_pass = et_new_pass.getText().toString();
                con_pass = et_con_pass.getText().toString();

                if ((et_new_pass.getText().toString().length() == 0)) {
                    et_new_pass.setError("Enter Password");
                }
                else if ((et_con_pass.getText().toString().length() == 0)) {
                    et_con_pass.setError("Enter Password Again");
                }
                else if(!new_pass.equals(con_pass)){
                    Toast.makeText(Edit_Pass.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                }
                else{


                    if(new Check_internet_connection(getApplicationContext()).isNetworkAvailable()){

                        new Update_Pass().execute();

                    }
                    else {

                        Toast.makeText(getApplicationContext(),
                                "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }


                }

            }
        });

    }




    String server_response="0"
            ,server_response_text;
    //ASYNTASK REGISTER USER////////////////////////////////////
    public class Update_Pass extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(Edit_Pass.this);
            progressDialog.setTitle("Loading! Be Patient!");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj     = new JSONObject();

                obj.put("operation","update_pass");

                obj.put("id", intent_id);
                obj.put("pass", new_pass);


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

                    Toast.makeText(Edit_Pass.this, "Password Updated Successfully",
                            Toast.LENGTH_SHORT).show();

                    Edit_Pass.this.finish();
                    Intent intent = new Intent(Edit_Pass.this, Student_Activity.class);
                    startActivity(intent);

                }else {
                    Toast.makeText(Edit_Pass.this, server_response_text, Toast.LENGTH_SHORT).show();

                }

            }else {

                Toast.makeText(Edit_Pass.this, "Error while loading data", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
