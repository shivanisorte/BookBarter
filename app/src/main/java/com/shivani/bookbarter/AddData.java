package com.shivani.bookbarter;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddData extends AppCompatActivity
{
    EditText bookName,authorName,email,ISBNNo,bookGenre;
    Button submit,back;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        bookName=(EditText)findViewById(R.id.add_name);
        email=(EditText)findViewById(R.id.add_email);
        authorName=(EditText)findViewById(R.id.add_course);
        ISBNNo=(EditText)findViewById(R.id.add_purl);
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
    }

    private void processinsert()
    {
        Map<String,Object> map=new HashMap<>();
        map.put("name",bookName.getText().toString());
        map.put("course",authorName.getText().toString());
        map.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        map.put("purl",ISBNNo.getText().toString());
        //String emailid = email.getText().toString();
        FirebaseDatabase.getInstance().getReference().child("students").push()
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