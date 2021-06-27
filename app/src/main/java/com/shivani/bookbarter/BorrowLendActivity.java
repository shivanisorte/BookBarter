package com.shivani.bookbarter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static com.firebase.ui.auth.AuthUI.*;
import static com.firebase.ui.auth.viewmodel.RequestCodes.GOOGLE_PROVIDER;


public class BorrowLendActivity extends AppCompatActivity {


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference, reqReference, lentReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    Button sendRequestButton, declineRequestButton;
    String currentSate = "nil";
    String bookName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_lend);
        final String userId = getIntent().getStringExtra("uid");
        if (userId == null) {
            Log.d(TAG, "onCreate: ");
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        reqReference = FirebaseDatabase.getInstance().getReference().child("Requests");
        lentReference = FirebaseDatabase.getInstance().getReference().child("BooksLent");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();


        sendRequestButton = (Button) findViewById(R.id.req_books);
        declineRequestButton = (Button) findViewById(R.id.cancel_req);

        LoadUser();

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAction(userId);
            }
        });
        CheckBookAvailabiltiy(userId);


    }

    private void CheckBookAvailabiltiy(String userId) {
        lentReference.child(mUser.getUid()).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentSate = "lent";
                    sendRequestButton.setText("Lent");
                    sendRequestButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reqReference.child(userId).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentSate = "borrowed";
                    sendRequestButton.setText("Borrowed");              
                   sendRequestButton.setVisibility(View.GONE); }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reqReference.child(mUser.getUid()).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("status").getValue().toString().equals("pending")) {
                        currentSate = "sent/Pending";
                        sendRequestButton.setText("Cancel Request");
                        declineRequestButton.setVisibility(View.GONE);
                    }
                    if (snapshot.child("status").getValue().toString().equals("declined")) {
                    }
                    currentSate = "declined";
                    sendRequestButton.setText("Cancel Request");
                    declineRequestButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reqReference.child(userId).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        currentSate = "received/pending";
                        sendRequestButton.setText("Accepts Borrow Request");
                        declineRequestButton.setText("Decline Borrow Request");
                        declineRequestButton.setVisibility(View.VISIBLE);
                    }
                }
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(currentSate.equals("nil")){
            currentSate="nil";
            sendRequestButton.setText("Send Request to Borrow Book");
            declineRequestButton.setVisibility(View.GONE);
        }
    }

    private void PerformAction(String userId) {
        if (currentSate.equals("nil")) {
            HashMap hashmap = new HashMap();
            hashmap.put("satus", "pending");
            reqReference.child(mUser.getUid()).child(userId).updateChildren(hashmap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(BorrowLendActivity.this, "Request Sent", Toast.LENGTH_LONG).show();
                         //declineRequestButton.setVisibility(View.GONE);
                         currentSate = "sent/Pending";
                         sendRequestButton.setText("Cancel Request");
                    } else {
                        Toast.makeText(BorrowLendActivity.this, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        if (currentSate.equals("sent/Pending") || currentSate.equals(("declined"))) {
            reqReference.child(mUser.getUid()).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(BorrowLendActivity.this, "Request Cancelled", Toast.LENGTH_SHORT).show();
                        currentSate = "nil";
                        sendRequestButton.setText(("Send Request to Borrow Book"));
                       declineRequestButton.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(BorrowLendActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if (currentSate.equals("received/pending")) {
            reqReference.child(mUser.getUid()).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        HashMap hashmap = new HashMap();
                        hashmap.put("status", "Lent");
                        hashmap.put("name", bookName);
                        lentReference.child(mUser.getUid()).child(userId).updateChildren(hashmap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    lentReference.child(userId).child(mUser.getUid()).updateChildren(hashmap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Toast.makeText(BorrowLendActivity.this, "Book Lent", Toast.LENGTH_SHORT).show();
                                            currentSate = "lent";
                                       
                                        }
                                    });
                                }
                            }
                        });
                    }

                }
            });
        }
        if (currentSate.equals("lent")) {
            //
        }
    }

    private void LoadUser() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    bookName = snapshot.child("name").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}