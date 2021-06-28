package com.shivani.bookbarter;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AddData extends AppCompatActivity
{

    EditText bookName,authorName,email,ISBNNo,bookGenre;
    Button submit,back;

    String ownername, pincode;

    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);


        bookName=(EditText)findViewById(R.id.add_name);
        email=(EditText)findViewById(R.id.add_email);
        authorName=(EditText)findViewById(R.id.add_authorname);
        ISBNNo=(EditText)findViewById(R.id.add_isbn);
        back=(Button)findViewById(R.id.add_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UploadLibActivity.class));
                finish();
            }
        });

        submit=(Button)findViewById(R.id.add_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processinsert();
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference("Users");


        databaseReference = firebaseDatabase.getInstance().getReference("Users");


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                databaseReference.child("Users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());

                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            ownername= (String) dataSnapshot.child(firebaseUser.getUid()).child("name").getValue();
//                            Toast.makeText(AddData.this, "The owner is"+ownername, Toast.LENGTH_SHORT).show();
                            pincode= (String) dataSnapshot.child(firebaseUser.getUid()).child("pincode").getValue();


                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(postListener);



    }

    private void processinsert()
    {
        Map<String,Object> map=new HashMap<>();
        map.put("name",bookName.getText().toString());
        map.put("author",authorName.getText().toString());
        map.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        map.put("isbnno",ISBNNo.getText().toString());
        map.put("ownername",ownername);
        map.put("ownerpincode",pincode);
        //String emailid = email.getText().toString();
        FirebaseDatabase.getInstance().getReference().child("Books").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        bookName.setText("");
                        authorName.setText("");
                        email.setText("");
                        ISBNNo.setText("");
                        Toast.makeText(getApplicationContext(),"Inserted Successfully",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(getApplicationContext(),"Could not insert",Toast.LENGTH_LONG).show();
                    }
                });

    }
}