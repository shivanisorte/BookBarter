package com.shivani.bookbarter;

import android.Manifest;
import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shivani.bookbarter.R;

import java.util.HashMap;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String storagepath = "Users_Profile_Cover_image/";
    String uid;
    ImageView set;
    TextView editname, editpassword;
    ProgressDialog pd;
    private static final int STORAGE_REQUEST = 200;
    String storagePermission[];
    private Button profilebutton;

    TextView username;
    TextView showemail;
    TextView showphonenum;
    TextView showpin;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();

//    String profileOrCoverPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        profilebutton=(Button)findViewById(R.id.profilebutton);

        username=(TextView) findViewById(R.id.username);
        showemail=(TextView)findViewById(R.id.showemail);
        showphonenum=(TextView)findViewById(R.id.showphonenum);
        showpin=(TextView)findViewById(R.id.showpin);




//        if(options.equals(null))
//        {
//            profilebutton.setVisibility(View.GONE);
//        }




////        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//////        DatabaseReference userNameRef = rootRef.child("Users").child("-McsGiaE75pRxcvppxSz");
////        DatabaseReference userNameRef = rootRef.child("Users").getRef().child("email").child(email);
//        ValueEventListener eventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()) {
//                    profilebutton.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        };
//        userNameRef.addListenerForSingleValueEvent(eventListener);



//
//        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//        Query query = rootRef.child("Users").orderByChild("email").equalTo(email);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()) {
//                    profilebutton.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        });


        profilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ProfileData.class));
            }
        });



        editname = findViewById(R.id.editname);
//        set = findViewById(R.id.setting_profile_image);
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        editpassword = findViewById(R.id.changepassword);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = firebaseDatabase.getReference("Users");
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    profilebutton.setVisibility(View.GONE);
//                    String uname = firebaseDatabase.getInstance().getReference().child("User").child("firstname").getValue();
//                    username.setText(uname);
                    showemail.setText(email);
//                    showphonenum.setText("new");
//                    showpin.setText("new");



//                    String uname= (String) dataSnapshot.child("Users").child(firebaseUser.getUid()).getValue(String.class);
//                    username.setText(uname);


//                    FirebaseDatabase.getInstance().getReference().child("Users").orderByKey().equalTo("firstname")
////                    databaseReference.orderByKey().equalTo("firstname")
//                            .addValueEventListener(new ValueEventListener() {
//                      @Override
//                      public void onDataChange(DataSnapshot dataSnapshot) {
//
//                          String uname = null;
//                          for(DataSnapshot data : dataSnapshot.getChildren()) {
//
//                              uname = data.getKey().toString();
//                          }
//
//                          if(query.equals("firstname")) {
//
//                              username.setText(uname);
//
//                          }
//                          else {
//                              Toast.makeText(AccountActivity.this, "Try restarting or adding profile info", Toast.LENGTH_SHORT).show();
//                          }
//
//                      }
//
//                      @Override
//                      public void onCancelled(DatabaseError databaseError) {
//
//                      }
//                    });



                    HashMap<String, Object> hashmap = new HashMap<>();
                    Iterator<DataSnapshot> myiterator = dataSnapshot.getChildren().iterator();
                    while (myiterator.hasNext()) {

                        DataSnapshot mysnapshot = myiterator.next();

                        if (mysnapshot.getKey().equals("name")) {
                           String uname=mysnapshot.getValue().toString();
                            username.setText(uname);
                        } else if (mysnapshot.getKey().equals("phonenumber")) {
                            String phone= mysnapshot.getValue().toString();
                            showphonenum.setText(phone);
                        } else if (mysnapshot.getKey().equals("pincode")) {
                            String pinnum = mysnapshot.getValue().toString();
                            showphonenum.setText(pinnum);
                        }
                    }


                }


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String image = "" + dataSnapshot1.child("image").getValue();

                    try {
                        Glide.with(AccountActivity.this).load(image).into(set);
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        editpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Changing Password");
                showPasswordChangeDailog();
            }
        });


        editname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Updating Name");
                showNamephoneupdate("name");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String image = "" + dataSnapshot1.child("image").getValue();

                    try {
                        Glide.with(AccountActivity.this).load(image).into(set);
                    } catch (Exception e) {
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        editpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Changing Password");
                showPasswordChangeDailog();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String image = "" + dataSnapshot1.child("image").getValue();

                    try {
                        Glide.with(AccountActivity.this).load(image).into(set);
                    } catch (Exception e) {
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        editpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Changing Password");
                showPasswordChangeDailog();
            }
        });
    }


    // We will show an alert box where we will write our old and new password
    private void showPasswordChangeDailog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_update_password, null);
        final EditText oldpass = view.findViewById(R.id.oldpasslog);
        final EditText newpass = view.findViewById(R.id.newpasslog);
        Button editpass = view.findViewById(R.id.updatepass);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        editpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldp = oldpass.getText().toString().trim();
                String newp = newpass.getText().toString().trim();
                if (TextUtils.isEmpty(oldp)) {
                    Toast.makeText(AccountActivity.this, "Current Password cant be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(newp)) {
                    Toast.makeText(AccountActivity.this, "New Password cant be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                dialog.dismiss();
                updatePassword(oldp, newp);
            }
        });
    }

    // Now we will check that if old password was authenticated
    // correctly then we will update the new password
    private void updatePassword(String oldp, final String newp) {
        pd.show();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldp);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(newp)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        pd.dismiss();
                                        Toast.makeText(AccountActivity.this, "Changed Password", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(AccountActivity.this, "Failed", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AccountActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Updating name
    private void showNamephoneupdate(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update " + key);

        // creating a layout to write the new name
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);
        final EditText editText = new EditText(this);
        editText.setHint("Enter " + key);
        layout.addView(editText);
        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    pd.show();

                    // Here we are updating the new name
                    HashMap<String, Object> result = new HashMap<>();
//                    result.put(key, value);
                    result.put("done", "True");



                    databaseReference.child("updates").updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();

                            // after updated we will show updated
                            Toast.makeText(AccountActivity.this, " updated ", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(AccountActivity.this, "Unable to update", Toast.LENGTH_LONG).show();
                        }
                    });
                    if (key.equals("name")) {
                        final DatabaseReference databaser = FirebaseDatabase.getInstance().getReference("Users");
                        Query query = databaser.orderByChild("email").equalTo(user.getEmail());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String child = databaser.getKey();
                                    dataSnapshot1.getRef().child("name").setValue(value); //imp
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(AccountActivity.this, "Unable to update", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
            }
        });
        builder.create().show();
    }



}