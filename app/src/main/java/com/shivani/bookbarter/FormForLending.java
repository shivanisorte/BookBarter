package com.shivani.bookbarter;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



public class FormForLending extends AppCompatActivity {

    private EditText mbookName;
    private EditText mbookAuthor;
    private EditText mbookprice;
    private String mbookTag;
    private Track mbookObject;


    private static final int RC_PHOTO_PICKER =  2;



    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    private FirebaseStorage mFirebaseStorage;

    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_for_lending);

        mFirebaseDatabase = FirebaseDatabase.getInstance();


        mDatabaseReference = mFirebaseDatabase.getReference().child("Books");

        mFirebaseStorage = FirebaseStorage.getInstance();

        Spinner spinner = (Spinner) findViewById(R.id.form_for_lending_book_type_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mbookTag = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button continueBuying = findViewById(R.id.form_for_selling_add_button);
        mbookName = findViewById(R.id.form_for_selling_book_name);
        mbookAuthor = findViewById(R.id.form_for_selling_author);
        mbookprice = findViewById(R.id.form_for_selling_price);
        //mbookTag = findViewById(R.id.form_for_selling_book_type);

        mbookObject = new Track();



        continueBuying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mbookName.getText().toString().equals("") || mbookAuthor.getText().toString().equals("") || mbookprice.getText().toString().equals(""))
                {
                    mbookName.setHintTextColor(Color.parseColor("#E57373"));
                    mbookAuthor.setHintTextColor(Color.parseColor("#E57373"));
                    mbookprice.setHintTextColor(Color.parseColor("#E57373"));
                    Toast.makeText(FormForLending.this,"Red Fields are Mandetory",Toast.LENGTH_LONG).show();
                }
                else
                {
                    mbookObject.setTrackName(mbookName.getText().toString());

                    mbookObject.setPrice(mbookprice.getText().toString());
                    mbookObject.setBooktype(mbookTag);
                    mbookObject.setEmail(BorrowLendActivity.finalemail);
                    mbookObject.setCount(0);



                    mDatabaseReference.child(BorrowLendActivity.finalUid).push().setValue(mbookObject);
                    //mDatabaseReference.child(mbookTag.getText().toString()).push().setValue(mbookObject);


                    Intent familyIntent = new Intent(FormForLending.this, TrackList.class);
                    startActivity(familyIntent);

                }

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

}