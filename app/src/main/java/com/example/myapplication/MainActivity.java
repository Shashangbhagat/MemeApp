package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

public class MainActivity extends AppCompatActivity {
    public static int RESULT_LOAD_IMAGE =1;
    TextView topText;
    TextView bottomText;
    EditText topEdit;
    EditText bottomEdit;
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView)findViewById(R.id.image);
        topText = (TextView)findViewById(R.id.memeTopText);
        bottomText = (TextView)findViewById(R.id.memeBottomText);
        topEdit = (EditText) findViewById(R.id.editTop);
        bottomEdit = (EditText)findViewById(R.id.editBottom);
    }

    public void addImage(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,RESULT_LOAD_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            String [] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn,null,null,null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            iv.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    public void tryImage(View view){

        topText.setText(topEdit.getText().toString());
        bottomText.setText(bottomEdit.getText().toString());
        hideme(view);


    }

    public void hideme(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    public static Bitmap getScreenShot(View view){
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void store(Bitmap bm , String fileName){
        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        File dir = new File(dirPath);
        if(!dir.exists()){
            dir.mkdir();
        }

        File file = new File(dirPath,fileName);
        try{
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.flush();
            fos.close();
            Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void saveImage(View view){
        View content = (View)findViewById(R.id.lay);
        Bitmap bitmap = getScreenShot(content);
        String fileName = "meme"+ System.currentTimeMillis()+".png";
        store(bitmap,fileName);
    }

}
