package com.moneyme.camera;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn;
    ImageView image;
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int PICK_USER_PROFILE_IMAGE = 1000;
    private Bitmap bitmap;
    private Bitmap btimap_in_imageview;
    private String reduced_bitmap;
    AlertDialog.Builder alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initOnclick();
    }

    public void initViews()
    {
        btn = (Button) findViewById(R.id.btn);
        image = (ImageView) findViewById(R.id.image);
    }

    public void initOnclick()
    {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showFileChooser();
                //startCameraActivity();
                showAlert();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode)
        {
            case PICK_USER_PROFILE_IMAGE :
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                image.setImageBitmap(photo
                );
                break;
            case PICK_IMAGE_REQUEST :
                    Log.e("case","file");
                Uri filePath = data.getData();
                try {
                    //Getting the Bitmap from Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                    //Setting the Bitmap to ImageView
                    image.setImageBitmap(bitmap);
                    btimap_in_imageview = bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                    break;
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void startCameraActivity(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(cameraIntent, PICK_USER_PROFILE_IMAGE);
        }
    }
    public void showAlert()
    {
        final String [] actions = {"Camera","Gallery"};
        int [] icons  = {R.drawable.ic_photo_camera_black_24dp,R.drawable.ic_folder_black_24dp};
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
        for ( int i = 0 ; i <=1 ; i++)
        {
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("name",actions[i]);
            hm.put("icon", String.valueOf(icons[i]));
            aList.add(hm);
        }
        String[] from = { "name","icon" };
        int[] to = {R.id.name,R.id.image};
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.list_row_layout, from, to);
        alertDialog = new AlertDialog.Builder(MainActivity.this);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_tab_layout, null);
        alertDialog.setView(convertView);
        ListView lv = (ListView) convertView.findViewById(R.id.list);
        lv.setAdapter(adapter);
        final AlertDialog alert = alertDialog.create();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (actions[position].equals("Camera"))
                {
                    startCameraActivity();
                }
                else
                {
                    showFileChooser();
                }
                alert.cancel();

            }

        });

        alert.setTitle("Complete Action Using"); // Title
        alert.show();
    }


}
