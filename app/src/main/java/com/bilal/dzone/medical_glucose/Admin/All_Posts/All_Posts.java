package com.bilal.dzone.medical_glucose.Admin.All_Posts;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bilal.dzone.medical_glucose.Admin.Edit_Post.Edit_Post;
import com.bilal.dzone.medical_glucose.JsonParsing.Check_internet_connection;
import com.bilal.dzone.medical_glucose.JsonParsing.JsonParser;
import com.bilal.dzone.medical_glucose.R;
import com.bilal.dzone.medical_glucose.URL.Url;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;


public class All_Posts extends Fragment {

    private static final String TAG = "token";
    ListView listView;
    boolean server_check=false;
    String server_response="0", del_post, server_response_text;
    String[] title
            ,desc
            ,date
            ,image
            ,post_id;
    JSONObject jp_obj;
    JSONArray jar_array;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter; String catagory;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v       = inflater.inflate(R.layout.all_posts, container, false  );


        listView     = (ListView) v.findViewById(R.id.listView_);
        spinner = (Spinner) v.findViewById(R.id.filter);


        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.filter_depts, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {

//                if(id == 0){
//
//                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){
//
//                        new GetAll().execute();
//                    }
//                    else {
//                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
//                    }
//
//                }else
                    if(id == 0){

                    catagory = "Meal";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 1){

                    catagory = "News";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 3){

                    catagory = "B.COM";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 4){

                    catagory = "BS-Commerce";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 5){

                    catagory = "BS-CS";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 6){

                    catagory = "BS-SE";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 7){

                    catagory = "BS-IT";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 8){

                    catagory = "B.ED";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 9){

                    catagory = "BS-Mathematics";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 10){

                    catagory = "BS-Civil Eng";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 11){

                    catagory = "BS-MLT";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 12){

                    catagory = "BS-Electrical Eng";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 13){

                    catagory = "M.Com";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 14){

                    catagory = "MBA";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 15){

                    catagory = "MPM";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 16){

                    catagory = "MCS";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 17){

                    catagory = "MS-CS";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 18){

                    catagory = "MS-SE";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 19){

                    catagory = "M.ED";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 20){

                    catagory = "MS-Mathematics";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 21){

                    catagory = "MA-English";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }
                else if(id == 22){

                    catagory = "MS-Electrical Engineering";
                    if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                        new Filter_post().execute();
                    }
                    else {
                        Toast.makeText(getActivity(), "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listview_click(id,position);
            }
        });


        if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

            new GetAll().execute();

        }
        else {

            Toast.makeText(getActivity(),
                    "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
        }



        return v;
    }




    //listView.setOnItemClickListener handled here///////////////////////////
    public void listview_click(long id,int position){

        if(id == 1){

            if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

//                Toast.makeText(getActivity(), "btn clicked" + " " + image[position], Toast.LENGTH_SHORT).show();
                del_post = post_id[position];
                Dialog_Delete_Service();

            }
            else {

                Toast.makeText(getActivity(),
                        "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
            }

        }

        else if(id == 2){

            if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                String note_id = post_id[position];
                Intent intent = new Intent(getActivity(),Edit_Post.class);
                intent.putExtra("post_id", note_id);
                startActivity(intent);

            }
            else {

                Toast.makeText(getActivity(),
                        "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
            }

        }


    }


    //ASYNTASK Getting Data From Server/////////////////////////////////////
    public class GetAll extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Loading! Be Patient!");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj     = new JSONObject();

                obj.put("operation","all_posts");

                String str_req = JsonParser.multipartFormRequestForFindFriends(Url.ulr, "UTF-8", obj,null);

                jp_obj = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;

                image          = new String[(jar_array.length()-1)];
                title       = new String[(jar_array.length()-1)];
                desc     = new String[(jar_array.length()-1)];
                date  = new String[(jar_array.length()-1)];
                post_id  = new String[(jar_array.length()-1)];


                c = jar_array.getJSONObject(0);

                if(c.length()>0){

                    server_response    = c.getString("response");

                }

                int j =1;

                if(server_response.equals("1")){
                    for(int i=0;j<jar_array.length();i++) {

                        c = jar_array.getJSONObject(j);

                        if (c.length() > 0) {

                            image[i]         = c.getString("image_path");
                            title[i]      = c.getString("title");
                            desc[i]    = c.getString("descr");
                            date[i] = c.getString("date");
                            post_id[i] = c.getString("id");

                        }

                        j++;
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

                if(title.length>0){

                    final All_Posts_Adapter adapter    = new All_Posts_Adapter(getActivity(),
                            desc, title, date, image);

                    listView.setAdapter(adapter);

                }else {

                    Toast.makeText(getActivity(), "Data list is empty", Toast.LENGTH_SHORT).show();
                }

            }else {

                Toast.makeText(getActivity(), "Error while loading data", Toast.LENGTH_SHORT).show();
            }

        }
    }



    //ASYNTASK Getting Data From Server/////////////////////////////////////
    public class Filter_post extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Loading! Be Patient!");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj     = new JSONObject();

                obj.put("operation","student_posts");
                obj.put("dept",catagory);

                String str_req = JsonParser.multipartFormRequestForFindFriends(Url.ulr, "UTF-8", obj,null);

                jp_obj = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;

                image          = new String[(jar_array.length()-1)];
                title       = new String[(jar_array.length()-1)];
                desc     = new String[(jar_array.length()-1)];
                date  = new String[(jar_array.length()-1)];
                post_id  = new String[(jar_array.length()-1)];


                c = jar_array.getJSONObject(0);

                if(c.length()>0){

                    server_response    = c.getString("response");

                }

                int j =1;

                if(server_response.equals("1")){
                    for(int i=0;j<jar_array.length();i++) {

                        c = jar_array.getJSONObject(j);

                        if (c.length() > 0) {

                            image[i]         = c.getString("image_path");
                            title[i]      = c.getString("title");
                            desc[i]    = c.getString("descr");
                            date[i] = c.getString("date");
                            post_id[i] = c.getString("id");

                        }

                        j++;
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

                if(title.length>0){

                    final All_Posts_Adapter adapter    = new All_Posts_Adapter(getActivity(),
                            desc, title, date, image);

                    listView.setAdapter(adapter);

                }else {

                    Toast.makeText(getActivity(), "Data list is empty", Toast.LENGTH_SHORT).show();
                }

            }else {

                Toast.makeText(getActivity(), "Error while loading data", Toast.LENGTH_SHORT).show();
            }

        }
    }




    //DIALOG TO DELETE A SERVICE FROM LIST///////////////////////////
    private void Dialog_Delete_Service(){

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle("Delete Post!");
        alertDialogBuilder.setIcon(R.drawable.failure);
        alertDialogBuilder.setMessage("Are you sure you want to Delete this Post?");


        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        if(new Check_internet_connection(getActivity()).isNetworkAvailable()){

                            new Delete_Post().execute();
                        }
                        else {

                            Toast.makeText(getActivity(),
                                    "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                        }

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


    //ASYNCTASK DELETE SERVICE///////////////////////////////////////////////////
    public class Delete_Post extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Loading! Be Patient!");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "post_delete");

                obj.put("id", del_post);

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


                server_check = true;


            } catch (Exception e) {
                e.printStackTrace();

            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            progressDialog.dismiss();

            if (server_check) {

                if (server_response.equals("1")) {

                    Toast.makeText(getActivity(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    new GetAll().execute();

                } else {
                    Toast.makeText(getActivity(), server_response_text, Toast.LENGTH_SHORT).show();

                }


            } else {

                Toast.makeText(getActivity(), "Error while loading data", Toast.LENGTH_SHORT).show();
            }

        }
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("All Posts");
    }
}
