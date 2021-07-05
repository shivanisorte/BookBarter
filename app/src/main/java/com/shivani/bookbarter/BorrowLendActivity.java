package com.shivani.bookbarter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;
import static com.firebase.ui.auth.AuthUI.*;
import static com.firebase.ui.auth.viewmodel.RequestCodes.GOOGLE_PROVIDER;


public class BorrowLendActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference, reqReference, lentReference, userRef, ref;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;
    private Button sendRequestButton, dateButton , calenderButton;
    private String mCurrentSate;
    private String bookName;
    private ProgressDialog mProgressDialog;
    private String bookId, userId;
    private TextView mbookName , dateformat;
    private String displayBookName;
    private String userEmail , userPin;
    private EditText title , description , location;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_borrow_lend);
            bookId = getIntent().getStringExtra("uid");
            userRef = FirebaseDatabase.getInstance().getReference().child("Books").child(bookId);
            calenderButton = (Button) findViewById(R.id.calender);
            title = findViewById(R.id.title);
            description = findViewById(R.id.description);
            location = findViewById(R.id.location);
            dateButton = findViewById(R.id.date_picker);
            dateformat = (TextView) findViewById(R.id.date);


            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        userId = snapshot.child("ownerUserId").getValue().toString();
                        userEmail = snapshot.child("email").getValue().toString();
                        userPin = snapshot.child("ownerpincode").getValue().toString();
                        displayBookName = snapshot.child("name").getValue().toString();
                        title.setText("BOOK BARTER APP REMINDER TO RETURN BORROWED BOOK");
                        location.setText("Return at location :" + userPin);
                        description.setText("Return Book "+displayBookName+" borrowed Via Book Barter App" );

                    }
                    else{
                        Toast.makeText(BorrowLendActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(BorrowLendActivity.this, "error", Toast.LENGTH_SHORT).show();

                }
            });
            if (userId == null) {
                Log.d(TAG, "onCreate: ");
            }

            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Books").child(bookId);//.child(userId);
            reqReference = FirebaseDatabase.getInstance().getReference().child("Requests");
            lentReference = FirebaseDatabase.getInstance().getReference().child("BooksLent");
            mFirebaseAuth = FirebaseAuth.getInstance();
            mUser = mFirebaseAuth.getCurrentUser();
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Loading Book Details");
            mProgressDialog.setMessage("Please wait");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            mCurrentSate = "not_lent";


            sendRequestButton = (Button) findViewById(R.id.req_books);
            //declineRequestButton = (Button) findViewById(R.id.cancel_req);
            calenderButton = (Button) findViewById(R.id.calender);
            title = findViewById(R.id.title);
            description = findViewById(R.id.description);
            location = findViewById(R.id.location);
            title.setText("BOOK BARTER APP REMINDER TO RETURN BORROWED BOOK");
            location.setText("Return at location :" + userPin);
            description.setText("Return Book "+displayBookName+" borrowed Via Book Barter App" );

            mbookName = (TextView) findViewById(R.id.bookName);

            //LoadUser();
            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        displayBookName = snapshot.child("name").getValue().toString();
                        mbookName.setText(displayBookName);
                        //reqReference = FirebaseDatabase.getInstance().getReference().child("Requests");
                        //reqReference = reqReference.
                        //Toast.makeText(BorrowLendActivity.this, ""+key, Toast.LENGTH_SHORT).show();
                        // Lent  List
//                    Map<String,Object> map=new HashMap<>();
//                    map.put("bookID",bookId.toString());
//                    map.put("req_type","");
//                    map.put("req_by",mUser.getUid());
//                    FirebaseDatabase.getInstance().getReference().child("Requests")
//                            .setValue(map);


                        reqReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("Requests");//.child("bookID");
                        reqReference.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild("bookID")) {//child(userId).child(bookId).child("req_type")
                                    String req_type = snapshot.child("req_type").getValue().toString();
                                    String book = snapshot.child("bookID").getValue().toString();
                                    if (book.equals(bookId)) {
                                        if (req_type.equals("received")) {
                                            mCurrentSate = "req_received";
                                            sendRequestButton.setText("Accept Borrow Request");
                                        } else if (req_type.equals("sent")) {
                                            mCurrentSate = "req_sent";
                                            sendRequestButton.setText("Cancel Request");

                                        }
                                    }
                                }
                                mProgressDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            sendRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sendRequestButton.setEnabled(false);
                    //not lent state

                    if (mCurrentSate.equals("not_lent")) {//child(userId).child(bookId).child("request_type").setValue("sent")
                        Map<String, Object> map = new HashMap<>();
                        map.put("bookID", bookId.toString());
                        map.put("req_type", "sent");
                        map.put("req_by", mUser.getUid());
                        reqReference.child(mUser.getUid()).push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Toast.makeText(BorrowLendActivity.this, "task successful", Toast.LENGTH_SHORT).show();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("bookID", bookId.toString());
                                    map.put("req_type", "received");
                                    map.put("req_by", mUser.getUid());
                                    //reqReference.child(userId).child(mUser.getUid()).child(bookId).child("request_type").setValue("received")
                                    reqReference.child(userId).push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            sendRequestButton.setEnabled(true);
                                            mCurrentSate = "req_sent";
                                            sendRequestButton.setText("Request Sent");
                                            sendRequestButton.setEnabled(false);

                                            Toast.makeText(BorrowLendActivity.this, "Opening Email App to Send Request", Toast.LENGTH_SHORT).show();
                                            String SUBJECT = "BORROW REQUEST VIA BOOK BARTER APP FOR ";
                                            String MESSAGE = "Hey fellow Book Barter App User \n Request to Borrow " +displayBookName +" from "+dateformat.getText().toString()+" \n Thank you.";

                                           // String emailsend = userId.child("email").getE().toString();
//                                        ref.addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                                String emailsend = dataSnapshot.child("email").getValue(String.class);
//                                                String bookName =  dataSnapshot.child("course").getValue(String.class);

                                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                                            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                                            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{userEmail});
                                            intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT + displayBookName);
                                            intent.putExtra(Intent.EXTRA_TEXT , MESSAGE);
                                            // if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                            startActivity(intent);
                                        }
//                                        @Override
//                                        public void onCancelled(DatabaseError databaseError) {
//                                            Toast.makeText(BorrowLendActivity.this, "Error", Toast.LENGTH_SHORT).show();
//
//                                        }
                                    });
                                } else {
                                    Toast.makeText(BorrowLendActivity.this, "Failed to Send Request", Toast.LENGTH_SHORT).show();
                                }
                            }

                            //});


                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(BorrowLendActivity.this, "failure", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    //cancel request state
                    //add on failure listener later(to handle errors)
                    if (mCurrentSate.equals("req_sent")) {//.child(userId).child(bookId).
                        reqReference.child(mUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {//child(mUser.getUid()).child(bookId)
                                reqReference.child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        sendRequestButton.setEnabled(true);
                                        mCurrentSate = "not_lent";
                                        sendRequestButton.setText("Send Request to Borrow");

                                        Toast.makeText(BorrowLendActivity.this, "Request Cancelled", Toast.LENGTH_SHORT).show();


                                    }
                                });
                            }
                        });
                    }

                    //PerformAction(userId);
                }

            });


            calenderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE, title.getText().toString());
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, description.getText().toString());
                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location.getText().toString());
                    intent.putExtra(CalendarContract.Events.ALL_DAY, true);
                    intent.putExtra(Intent.EXTRA_EMAIL, userEmail);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);


                    } else {
                        Toast.makeText(BorrowLendActivity.this, "No app to handle this action", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            dateButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(BorrowLendActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            dateformat.setText(SimpleDateFormat.getDateInstance().format(c.getTime()));

                        }
                    }, year, month,day);
                    datePickerDialog.show();


                }
            });


        }


        }


// Check Avalabilty of book method - to check if book is avilable to borrow
//    private void CheckBookAvailabiltiy(String userId) {
//        lentReference.child(mUser.getUid()).child(userId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    currentSate = "lent";
//                    sendRequestButton.setText("Lent");
//                    sendRequestButton.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        reqReference.child(userId).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    currentSate = "borrowed";
//                    sendRequestButton.setText("Borrowed");
//                   sendRequestButton.setVisibility(View.GONE); }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        reqReference.child(mUser.getUid()).child(userId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    if (snapshot.child("status").getValue().toString().equals("pending")) {
//                        currentSate = "sent/Pending";
//                        sendRequestButton.setText("Cancel Request");
//                        declineRequestButton.setVisibility(View.GONE);
//                    }
//                    if (snapshot.child("status").getValue().toString().equals("declined")) {
//                    }
//                    currentSate = "declined";
//                    sendRequestButton.setText("Cancel Request");
//                    declineRequestButton.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        reqReference.child(userId).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    if(snapshot.child("status").getValue().toString().equals("pending")){
//                        currentSate = "received/pending";
//                        sendRequestButton.setText("Accepts Borrow Request");
//                        declineRequestButton.setText("Decline Borrow Request");
//                        declineRequestButton.setVisibility(View.VISIBLE);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        if(currentSate.equals("nil")){
//            currentSate="nil";
//            sendRequestButton.setText("Send Request to Borrow Book");
//            declineRequestButton.setVisibility(View.GONE);
//        }
//    }
//// Perform action method - to be executed when user sends request
//    private void PerformAction(String userId) {
//        if (currentSate.equals("nil")) {
//            HashMap hashmap = new HashMap();
//            hashmap.put("satus", "pending");
//            reqReference.child(mUser.getUid()).child(userId).updateChildren(hashmap).addOnCompleteListener(new OnCompleteListener() {
//                @Override
//                public void onComplete(@NonNull Task task) {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(BorrowLendActivity.this, "Request Sent", Toast.LENGTH_LONG).show();
//                         //declineRequestButton.setVisibility(View.GONE);
//                         currentSate = "sent/Pending";
//                         sendRequestButton.setText("Cancel Request");
//                    } else {
//                        Toast.makeText(BorrowLendActivity.this, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//        }
//        if (currentSate.equals("sent/Pending") || currentSate.equals(("declined"))) {
//            reqReference.child(mUser.getUid()).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(BorrowLendActivity.this, "Request Cancelled", Toast.LENGTH_SHORT).show();
//                        currentSate = "nil";
//                        sendRequestButton.setText(("Send Request to Borrow Book"));
//                       declineRequestButton.setVisibility(View.GONE);
//
//                    } else {
//                        Toast.makeText(BorrowLendActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//        if (currentSate.equals("received/pending")) {
//            reqReference.child(mUser.getUid()).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        HashMap hashmap = new HashMap();
//                        hashmap.put("status", "Lent");
//                        hashmap.put("name", bookName);
//                        lentReference.child(mUser.getUid()).child(userId).updateChildren(hashmap).addOnCompleteListener(new OnCompleteListener() {
//                            @Override
//                            public void onComplete(@NonNull Task task) {
//                                if (task.isSuccessful()) {
//                                    lentReference.child(userId).child(mUser.getUid()).updateChildren(hashmap).addOnCompleteListener(new OnCompleteListener() {
//                                        @Override
//                                        public void onComplete(@NonNull Task task) {
//                                            Toast.makeText(BorrowLendActivity.this, "Book Lent", Toast.LENGTH_SHORT).show();
//                                            currentSate = "lent";
//
//                                        }
//                                    });
//                                }
//                            }
//                        });
//                    }
//
//                }
//            });
//        }
//        if (currentSate.equals("lent")) {
//            //
//        }
//    }
//





