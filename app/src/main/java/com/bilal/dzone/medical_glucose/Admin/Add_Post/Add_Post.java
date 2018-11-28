package com.bilal.dzone.medical_glucose.Admin.Add_Post;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bilal.dzone.medical_glucose.Admin.Edit_Post.Edit_Post;
import com.bilal.dzone.medical_glucose.JsonParsing.Check_internet_connection;
import com.bilal.dzone.medical_glucose.JsonParsing.JsonParser;
import com.bilal.dzone.medical_glucose.R;
import com.bilal.dzone.medical_glucose.URL.Url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.Manifest.permission.CAMERA;


public class Add_Post extends AppCompatActivity {

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    ProgressDialog progressDialog ;
    String ImageName = "image_name" ;
    String ImagePath = "image_path" ;
    String ServerUploadPath = Url.image_ulr ;
    //    String ServerUploadPath ="http://cusit.orgfree.com/api/Image_Updating.php" ;
    Bitmap myBitmap;
    Uri picUri;
    boolean check = true;

    EditText et_title, et_description;
    TextView tv_dop;
    ImageView post_image;
    Button select_image, publish_post;
    Spinner department;
    String title, description, dop_,userChoosenTask;
    JSONObject jp_obj;
    JSONArray jar_array;
    boolean server_check=false;
    ArrayAdapter<CharSequence> adapter; String catagory;
    DatePicker datePicker;int SELECT_FILE=1,REQUEST_CAMERA= 0;
    String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_post);


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        formattedDate = df.format(c);


        tv_dop = (TextView) findViewById(R.id.dob);
        et_title = (EditText) findViewById(R.id.title);
        et_description = (EditText) findViewById(R.id.desc);
        post_image = (ImageView) findViewById(R.id.post_image);
        select_image = (Button) findViewById(R.id.select_img);
        publish_post = (Button) findViewById(R.id.upload_post);
        department = (Spinner) findViewById(R.id.spinner);


        //////////////////////////////////////////
        permissions.add(CAMERA);
        permissionsToRequest = findUnAskedPermissions(permissions);

        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        /////////////////////////////////////////



        tv_dop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //CUSTOM DIALOG///////////////////////////////
                final Dialog dialog = new Dialog(Add_Post.this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.setContentView(R.layout.dialog_dob);

                datePicker       = (DatePicker) dialog.findViewById(R.id.datePicker1);
                Button dob       = (Button) dialog.findViewById(R.id.btn);
                Button btn_cross = (Button) dialog.findViewById(R.id.cross);

                btn_cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });


                //add deadline date for post
                dob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        tv_dop.setText(getDOB());
                        dop_ = tv_dop.getText().toString().trim();
                        dialog.dismiss();

                    }
                });


                dialog.show();

            }

        });



        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(Add_Post.this,
                R.array.depts, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department.setAdapter(adapter);

        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {

                if(id == 0){
                    catagory = "Meals";
                }
                if(id == 1){
                    catagory = "News";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



        publish_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = et_title.getText().toString();
                description = et_description.getText().toString();

                if(et_title.getText().toString().length() == 0){

                    et_title.setError("Enter Title");
                }

                else if(et_description.getText().toString().length() == 0){

                    et_description.setError("Enter Description");
                }
                else {


                    if (new Check_internet_connection(getApplicationContext()).isNetworkAvailable()) {

                        if (myBitmap != null){

                            new Publish_Post().execute();
                            ImageUploadToServerFunction();
                        }
                        else {
                            new Publish_Post().execute();
                        }


                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);


                    } else {

                        Toast.makeText(getApplicationContext(),
                                "Check your Internet Connection & Try again", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });



        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();
//                startActivityForResult(getPickImageChooserIntent(), 200);
            }
        });



    }



    String server_response="0"
            ,server_response_text;
    //ASYNTASK Publish Post////////////////////////////////////
    public class Publish_Post extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

//            progressDialog = new ProgressDialog(Add_Post.this);
//            progressDialog.setTitle("Loading! Be Patient!");
//            progressDialog.setCancelable(false);
//            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj     = new JSONObject();

                obj.put("operation","notes");

                obj.put("dept", catagory);
                obj.put("title", title);
                obj.put("descr", description);
                obj.put("date", formattedDate);


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
                server_check = false;
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {

//            progressDialog.dismiss();

            if(server_check){

                if(server_response.equals("1")){

                    Toast.makeText(Add_Post.this, "Uploading Post",
                            Toast.LENGTH_SHORT).show();



                }else {
                    Toast.makeText(Add_Post.this, server_response_text, Toast.LENGTH_SHORT).show();

                }

            }else {

                Toast.makeText(Add_Post.this, "Uploaded", Toast.LENGTH_SHORT).show();
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

                progressDialog = ProgressDialog.show(Add_Post.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
//                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(Add_Post.this,"Uploaded",Toast.LENGTH_LONG).show();

                // Setting image as transparent after done uploading.
//                imageView.setImageResource(android.R.color.transparent);


            }

            @Override
            protected String doInBackground(Void... params) {

                Add_Post.ImageProcessClass imageProcessClass = new Add_Post.ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImageName, "user_image");

                HashMapParams.put("id", "00");

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


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Add_Post.this);
        alertDialogBuilder.setMessage("Upload Image?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        post_image.setImageResource(R.drawable.no_image);
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
        PackageManager packageManager = Add_Post.this.getPackageManager();

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
        File getImage = Add_Post.this.getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }


//        Bitmap bitmap;
//        if (resultCode == Activity.RESULT_OK) {
//
//
//            if (getPickImageResultUri(data) != null) {
//                picUri = getPickImageResultUri(data);
//
//                try {
//                    myBitmap = MediaStore.Images.Media.getBitmap(Add_Post.this.getContentResolver(), picUri);
//                    myBitmap = rotateImageIfRequired(myBitmap, picUri);
////                    myBitmap = getResizedBitmap(myBitmap, 500);
//
//                    post_image.setImageBitmap(myBitmap);
////                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//
//                    dialog();
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            } else {
//
//
//                bitmap = (Bitmap) data.getExtras().get("data");
//
//                myBitmap = bitmap;
//
//                if (post_image != null) {
//                    post_image.setImageBitmap(myBitmap);
//                }
//
//                post_image.setImageBitmap(myBitmap);
//
//            }
//
//        }

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
     * Get the URI of the selected image from {@link #//getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera and gallery image.
     *
     * @param //data the returned data of the activity result
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
    public void onSaveInstanceState(Bundle outState) {
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
                return (Add_Post.this.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Add_Post.this)
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


    //dialog
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Add_Post.this);
        builder.setTitle("Add Photo!");
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    cameraIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask ="Choose from Gallery";
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    //gallery
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);

    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        if (data != null) {
            try {
                myBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                dialog();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        post_image.setImageBitmap(myBitmap);
    }


    //camera
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    //capture image process
    private void onCaptureImageResult(Intent data) {

        myBitmap = (Bitmap) data.getExtras().get("data");
        post_image.setImageBitmap(myBitmap);

        dialog();
    }



    //set dob to textview
    public String getDOB(){

        StringBuilder builder  = new StringBuilder();
        builder.append(datePicker.getDayOfMonth()+ "\t");


        int month=datePicker.getMonth();
        String[] MONTHS = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        String mon=MONTHS[month];


        builder.append(mon + "\t");//month is 0 based
        builder.append(datePicker.getYear());


        return builder.toString();
    }






    @Override
    public void onBackPressed() {

        Add_Post.this.finish();

        super.onBackPressed();
    }
}
