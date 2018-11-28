package com.bilal.dzone.medical_glucose.Student;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bilal.dzone.medical_glucose.JsonParsing.Check_internet_connection;
import com.bilal.dzone.medical_glucose.JsonParsing.JsonParser;
import com.bilal.dzone.medical_glucose.Login.Login;
import com.bilal.dzone.medical_glucose.R;
import com.bilal.dzone.medical_glucose.Registeration.Register;
import com.bilal.dzone.medical_glucose.Student.Edit_pass.Edit_Pass;
import com.bilal.dzone.medical_glucose.URL.Url;
import com.github.chrisbanes.photoview.PhotoView;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;


public class Student_Activity_old extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int REQUEST_READ_PERMISSION = 786;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    ProgressDialog progressDialog ;
    String ImageName = "image_name" ;
    String ImagePath = "image_path" ;
    String ServerUploadPath = Url.image_ulr2 ;
//    String ServerUploadPath ="http://cusit.orgfree.com/api/Image_Updating.php" ;
    Bitmap myBitmap;
    Uri picUri;
    boolean check = true;

    CircleImageView dp;
    private TextView stickyView;
    private ListView listView;
    private View stickyViewSpacer;
    int i;

    String[] title
            ,desc
            ,date
            ,image
            ,post_id;

    RelativeLayout linearLayout;
    FloatingActionButton btn, edit, camera, cross;
    SharedPreferences sharedPreferences;
    String pref_id, image_, dept, name, pref_dept, pref_pass;
    boolean server_check=false;
    JSONObject jp_obj;
    JSONArray jar_array;
    String server_response="0"
            ,server_response_text, image_clicked;
    TextView tv_id, tv_dept, tv_name;

    private ResideMenu resideMenu;
    private Student_Activity_old mContext;
    private ResideMenuItem edit_profile, edit_pass, logout;
    String TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_);

        mContext = this;

        sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        pref_id = sharedPreferences.getString("id", "");
        pref_dept = sharedPreferences.getString("dept", "");
        pref_pass = sharedPreferences.getString("pass", "");


        /////////////////////////////////////////

        permissions.add(CAMERA);
        permissionsToRequest = findUnAskedPermissions(permissions);

        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);

            /////////////////////////////////////////


            dp = (CircleImageView) findViewById(R.id.user_profile_photo);
            tv_dept = (TextView) findViewById(R.id.department);
            tv_name = (TextView) findViewById(R.id.name);
            tv_id = (TextView) findViewById(R.id.s_id);
//        btn = (FloatingActionButton) findViewById(R.id.btn);
//        edit = (FloatingActionButton) findViewById(R.id.edit);
            camera = (FloatingActionButton) findViewById(R.id.camera);
//        cross = (FloatingActionButton) findViewById(R.id.cross);


        /* Initialise list view, hero image, and sticky view */
            listView = (ListView) findViewById(R.id.listView);
            stickyView = (TextView) findViewById(R.id.stickyView);
            linearLayout = (RelativeLayout) findViewById(R.id.ll);



        /* Inflate list header layout */
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listHeader = inflater.inflate(R.layout.list_header, null);
            stickyViewSpacer = listHeader.findViewById(R.id.stickyViewPlaceholder);


        /* Add list view header */
            listView.addHeaderView(listHeader);

        /* Handle list View scroll events */
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    //////////////////////////////// animate button on scroll
                    int btn_initPosY = camera.getScrollY();
                    if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                        camera.animate().cancel();
                        camera.animate().translationYBy(300);
                    } else {
                        camera.animate().cancel();
                        camera.animate().translationY(btn_initPosY);
                    }

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                /* Check if the first item is already reached to top.*/
                    if (listView.getFirstVisiblePosition() == 0) {
                        View firstChild = listView.getChildAt(0);
                        int topY = 0;
                        if (firstChild != null) {
                            topY = firstChild.getTop();
                        }

                        int heroTopY = stickyViewSpacer.getTop();
                        stickyView.setY(Math.max(0, heroTopY + topY));

                    /* Set the image to scroll half of the amount that of ListView */
                        linearLayout.setY(topY * 0.5f);
                    }
                }
            });


//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                btn.setVisibility(View.GONE);
//                edit.setVisibility(View.VISIBLE);
//                cross.setVisibility(View.VISIBLE);
//                camera.setVisibility(View.VISIBLE);
//
//            }
//        });
//
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(Student_Activity_old.this, "sd", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        cross.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                btn.setVisibility(View.VISIBLE);
//                edit.setVisibility(View.GONE);
//                cross.setVisibility(View.GONE);
//                camera.setVisibility(View.GONE);
//            }
//        });

            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Check Permissions Now
                        ActivityCompat.requestPermissions(mContext,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                786);
                    }
                    else {

//                        Toast.makeText(mContext, "Granted", Toast.LENGTH_SHORT).show();
                        startActivityForResult(getPickImageChooserIntent(), 200);

                    }


                }
            });


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listview_click(id, position);
                }
            });


            if (new Check_internet_connection(getApplicationContext()).isNetworkAvailable()) {

                new Get_Profile().execute();
                new GetAll_Posts().execute();

            } else {

                Toast.makeText(getApplicationContext(),
                        "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
            }

            setUpMenu();


        }

    }


    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.bggg);
        resideMenu.attachToActivity(this);
//        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        edit_pass = new ResideMenuItem(this, R.drawable.key1, "Change Pass");
        logout = new ResideMenuItem(this, R.drawable.logout_big, "Logout");

        resideMenu.addMenuItem(edit_pass, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(logout, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
         resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

        edit_pass.setOnClickListener(this);
        logout.setOnClickListener(this);

    }




    //listView.setOnItemClickListener handled here///////////////////////////
    public void listview_click(long id,int position){

        if(id == 1) {

            image_clicked = image[position];

            Full_Image_Dialog();


        }
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



    public class Get_Profile extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(Student_Activity_old.this);
            progressDialog.setTitle("Loading! Be Patient!");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();

                obj.put("operation", "load_profile");
                obj.put("id", pref_id);

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

                int j = 1;

                if (server_response.equals("1")) {
                    for (i = 0; j < jar_array.length(); i++) {

                        c = jar_array.getJSONObject(j);

                        if (c.length() > 0) {

                            dept  = c.getString("dept");
                            name  = c.getString("name");
                            image_  = c.getString("image_path");

                        }

                        j++;


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


                    tv_dept.setText(dept);
                    tv_id.setText(pref_id);
                    tv_name.setText(name);


                    Picasso.with( Student_Activity_old.this )
                            .load( Url.loading_imageto_imageview + image_ )
                            .error( R.drawable.dummy_user).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .placeholder( R.drawable.progress_animation )
                            .into( dp );

                } else {
                    Toast.makeText(Student_Activity_old.this, server_response_text, Toast.LENGTH_SHORT).show();

                }


            } else {

                Toast.makeText(Student_Activity_old.this, "Error while loading data", Toast.LENGTH_SHORT).show();
            }

        }
    }



    public class GetAll_Posts extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(Student_Activity_old.this);
            progressDialog.setTitle("Loading! Be Patient!");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj     = new JSONObject();

                obj.put("operation","student_posts");
                obj.put("dept", pref_dept);

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

                    final All_Posts_Adapter_ adapter    = new All_Posts_Adapter_(Student_Activity_old.this,
                            desc, title, date, image);

                    listView.setAdapter(adapter);

                }else {

                    Toast.makeText(Student_Activity_old.this, "Data list is empty", Toast.LENGTH_SHORT).show();
                }

            }else {

                Toast.makeText(Student_Activity_old.this, "Error while loading data", Toast.LENGTH_SHORT).show();
            }

        }
    }



    public void Full_Image_Dialog(){

        //CUSTOM DIALOG///////////////////////////////
        final Dialog dialog = new Dialog(Student_Activity_old.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_image);

        Button btn_cross = (Button) dialog.findViewById(R.id.cross);
        Button save    = (Button) dialog.findViewById(R.id.submit);
        final PhotoView imageView   = (PhotoView) dialog.findViewById(R.id.full_img);

        Picasso.with(getApplicationContext()).load( Url.loading_imageto_imageview + image_clicked).into
                (imageView);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if(new Check_internet_connection(getApplicationContext()).isNetworkAvailable()){

                        //checking permission to write storage
                        boolean hasPermission = (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                        if (!hasPermission) {
                            ActivityCompat.requestPermissions(Student_Activity_old.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_WRITE_STORAGE);
                        }
                        else {


                            CapturePhotoUtils photoUtils = new CapturePhotoUtils();
                            imageView.setDrawingCacheEnabled(true);
                            Bitmap b = imageView.getDrawingCache();
                            photoUtils.insertImage(Student_Activity_old.this.getContentResolver(),
                                    b, "1image", "this is downloaded image sample");
                            Toast.makeText(Student_Activity_old.this, "Saved to Gallery ", Toast.LENGTH_SHORT).show();
                        }


                    }
                    else {

                        Toast.makeText(getApplicationContext(),
                                "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                    dialog.dismiss();
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





    //saving image to gallery
    public class CapturePhotoUtils {

        /**
         * A copy of the Android internals  insertImage method, this method populates the
         * meta data with DATE_ADDED and DATE_TAKEN. This fixes a common problem where media
         * that is inserted manually gets saved at the end of the gallery (because date is not populated).
         * @see MediaStore.Images.Media#insertImage(ContentResolver, Bitmap, String, String)
         */
        public final String insertImage(ContentResolver cr,
                                        Bitmap source,
                                        String title,
                                        String description) {

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, title);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
            values.put(MediaStore.Images.Media.DESCRIPTION, description);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            // Add the date meta data to ensure the image is added at the front of the gallery
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

            Uri url = null;
            String stringUrl = null;    /* value to be returned */

            try {
                url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                if (source != null) {
                    OutputStream imageOut = cr.openOutputStream(url);
                    try {
                        source.compress(Bitmap.CompressFormat.JPEG, 80, imageOut);
                    } finally {
                        imageOut.close();
                    }

                    long id = ContentUris.parseId(url);
                    // Wait until MINI_KIND thumbnail is generated.
                    Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                    // This is for backward compatibility.
                    storeThumbnail(cr, miniThumb, id, 50F, 50F, MediaStore.Images.Thumbnails.MICRO_KIND);
                } else {
                    cr.delete(url, null, null);
                    url = null;
                }
            } catch (Exception e) {
                if (url != null) {
                    cr.delete(url, null, null);
                    url = null;
                }
            }

            if (url != null) {
                stringUrl = url.toString();
            }

            return stringUrl;
        }

        /**
         * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
         * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
         * meta data. The StoreThumbnail method is private so it must be duplicated here.
         * @see MediaStore.Images.Media (StoreThumbnail private method)
         */
        private final Bitmap storeThumbnail(
                ContentResolver cr,
                Bitmap source,
                long id,
                float width,
                float height,
                int kind) {

            // create the matrix to scale it
            Matrix matrix = new Matrix();

            float scaleX = width / source.getWidth();
            float scaleY = height / source.getHeight();

            matrix.setScale(scaleX, scaleY);

            Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                    source.getWidth(),
                    source.getHeight(), matrix,
                    true
            );

            ContentValues values = new ContentValues(4);
            values.put(MediaStore.Images.Thumbnails.KIND,kind);
            values.put(MediaStore.Images.Thumbnails.IMAGE_ID,(int)id);
            values.put(MediaStore.Images.Thumbnails.HEIGHT,thumb.getHeight());
            values.put(MediaStore.Images.Thumbnails.WIDTH,thumb.getWidth());

            Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

            try {
                OutputStream thumbOut = cr.openOutputStream(url);
                thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
                thumbOut.close();
                return thumb;
            } catch (FileNotFoundException ex) {
                return null;
            } catch (IOException ex) {
                return null;
            }
        }
    }





    ////////////////////////////////////////////////////////////////////
    //image ulpoading
    public void ImageUploadToServerFunction(){

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(Student_Activity_old.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(Student_Activity_old.this,string1,Toast.LENGTH_LONG).show();

                // Setting image as transparent after done uploading.
//                imageView.setImageResource(android.R.color.transparent);


            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImageName, "user_image");

                HashMapParams.put("id", pref_id);

                HashMapParams.put(ImagePath, ConvertImage);

                String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }



    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }



    public void dialog(){


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Upload Image?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        ImageUploadToServerFunction();

                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    //set orientation of rotated image
    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }


    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {


            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                    myBitmap = rotateImageIfRequired(myBitmap, picUri);
//                    myBitmap = getResizedBitmap(myBitmap, 500);

                    dp.setImageBitmap(myBitmap);
//                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    dialog();


                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {


                bitmap = (Bitmap) data.getExtras().get("data");

                myBitmap = bitmap;

                if (dp != null) {
                    dp.setImageBitmap(myBitmap);
                }

                dp.setImageBitmap(myBitmap);

            }

        }

    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }



    /**
     * Get the URI of the selected image from {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }



        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        picUri = savedInstanceState.getParcelable("pic_uri");
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (hasPermission(perms)) {

                    } else {

                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                                //Log.d("API123", "permisionrejected " + permissionsRejected.size());

                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }





//////////////////////////////////////////////////////////////////



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
                        Intent intent = new Intent(Student_Activity_old.this, Login.class);
                        startActivity(intent);
                        Student_Activity_old.this.finish();
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
