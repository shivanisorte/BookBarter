package com.shivani.bookbarter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.*;
import static com.firebase.ui.auth.viewmodel.RequestCodes.GOOGLE_PROVIDER;

/**
 * Created by l on 11/3/17.
 */

public class BorrowLendActivity extends AppCompatActivity {


    public static String finalUid = "mee";
    public static String finalemail = "mee";
    public static String finalname = "mee";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    public static final int RC_SIGN_IN=1;

    public static final int PRO_REQ=2;

    private profile mprofileObject;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_lend);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        mprofileObject = new profile();


        Button logout = findViewById(R.id.log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReference.child(finalUid).setValue(mprofileObject);
                getInstance().signOut(BorrowLendActivity.this);
            }
        });




        ImageView pro = findViewById(R.id.profile);

        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseReference.child(finalUid).setValue(mprofileObject);
                Intent numbersIntent = new Intent(BorrowLendActivity.this, AccountActivity.class);
                numbersIntent.putExtra("Profile",mprofileObject);
                startActivity(numbersIntent);
                //setResult(Activity.RESULT_OK,numbersIntent);
                //startActivityForResult(numbersIntent,PRO_REQ);
            }
        });


        Button buy = findViewById(R.id.buy_books);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseReference.child(finalUid).setValue(mprofileObject);
                Intent numbersIntent = new Intent(BorrowLendActivity.this, ArtistActivity.class);
                //startActivity(numbersIntent);
                startActivityForResult(numbersIntent, 5);
            }
        });


        Button sell = findViewById(R.id.sell_books);

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseReference.child(finalUid).setValue(mprofileObject);
                Intent numbersIntent = new Intent(BorrowLendActivity.this, FormForLending.class);
                startActivity(numbersIntent);
            }
        });







    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                List<IdpConfig> providers= Arrays.asList(
               new AuthUI.IdpConfig.GoogleBuilder().build());


                if (user != null) {
                    finalUid= user.getUid();
                    finalemail = user.getEmail();
                    finalname = user.getDisplayName();
                    mprofileObject.setEmail(user.getEmail());
                    mprofileObject.setName(user.getDisplayName());
                    mprofileObject.setUid(user.getUid());
                    mprofileObject.setPhoneno("01*********");
                    mDatabaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            profile obj = dataSnapshot.getValue(profile.class);
                            if(obj!=null)
                            {
                                mprofileObject=obj;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });


                } else {
                    startActivityForResult(
                            getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .setTheme(R.style.Theme_BookBarter)
                                    .build(),
                            RC_SIGN_IN);
                }



            }
        };



    }



    private void onSignedInInitialize(String Uid, String name, String email) {

        mDatabaseReference.child(Uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profile obj = dataSnapshot.getValue(profile.class);
                if(obj!=null)
                {
                    mprofileObject=obj;
                }
                else
                {

                    mprofileObject.setPhoneno("01*********");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void onSignedOutInitialize() {

        if(mChildEventListener!=null)
        {
            mDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener=null;
        }
    }




    @Override
    protected void onPause() {
        super.onPause();
        if(mFirebaseAuthListener!=null)
        {
            mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN)
        {
            if(resultCode== RESULT_OK)
            {
                Toast.makeText(BorrowLendActivity.this,"signed in",Toast.LENGTH_LONG).show();
            }
            else if(resultCode== RESULT_CANCELED)
            {
                finish();
            }
        }
        if (requestCode == 5) {
            if(resultCode == Activity.RESULT_OK){
                //Toast.makeText(BuySell.this,"backpressed successful",Toast.LENGTH_LONG).show();
                if(data.getIntExtra("flag",0)==5){
                    Toast.makeText(BorrowLendActivity.this,"backpressed successful",Toast.LENGTH_LONG).show();

                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}