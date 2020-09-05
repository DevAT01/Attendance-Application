package com.example.ma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class SheetActivity extends AppCompatActivity {
    FirebaseStorage firebaseStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);

        Button button=findViewById(R.id.download);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadFile();
            }
        });


    }

    private void downloadFile() {

        StorageReference storageRef = firebaseStorage.getInstance().getReference();
        StorageReference  islandRef = storageRef.child("attendence.xlsx");

       // File rootPath = new File(Environment.getExternalStorageDirectory(), "attendance.xls");
       // if(!rootPath.exists()) {
       //     rootPath.mkdirs();


        //final File localFile = new File(rootPath,"attendance.xls");

        islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                downloadFile(SheetActivity.this, "attendence", ".xlsx","/Internal Storage/Download", url);
                Toast.makeText(SheetActivity.this, "Downloading started", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SheetActivity.this, PhotoActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ","File error, " +exception.toString());
                Toast.makeText(SheetActivity.this, "Picture format not valid, please upload again!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SheetActivity.this, PhotoActivity.class);
                startActivity(intent);
            }
        });
    }

    public void downloadFile(Context context, String fileName,String fileExtension,String destinationDirectory, String url){


        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName+fileExtension);
        downloadManager.enqueue(request);
    }




}


