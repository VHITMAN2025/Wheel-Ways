package com.example.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class uploadActivity extends AppCompatActivity {

    ImageView uploadImage;

    Button savebutton;
    EditText uploadid, uploadcolour, uploadlocation;
    FirebaseFirestore firestore;
    Task<DocumentReference> df;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        firestore = FirebaseFirestore.getInstance();
        uploadImage = findViewById(R.id.uploadImage);
        uploadid = findViewById(R.id.uploadid);
        uploadcolour = findViewById(R.id.uploadcolour);
        uploadlocation = findViewById(R.id.uploadlocation);
        savebutton = findViewById(R.id.savebutton);

       /* ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(uploadActivity.this, "no image selected", Toast.LENGTH_SHORT);
                        }
                    }
                }
        );*/

       /* uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);

            }
        });*/

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploaddata();
            }
        });
    }

  /* public void savedata() {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("cycledata").child(uri.getLastPathSegment());


        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                uploaddata();
            }
        });


    }*/

    private void uploaddata() {
        String id = uploadid.getText().toString();
        String colour = uploadcolour.getText().toString();
        String location = uploadlocation.getText().toString();
        String status = "available";
        String condition = "undamaged";

        DataClass dataClass = new DataClass(id, colour, location,status,condition);
        df = firestore.collection("cycledata").add(dataClass).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(uploadActivity.this, "Uploaded SuccessFully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(uploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}