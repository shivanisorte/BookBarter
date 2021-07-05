package com.shivani.bookbarter;
import android.app.Activity;
import android.content.Intent;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class UploadLibActivity extends AppCompatActivity {

    RecyclerView recview;
    MySecondadapter adapter;
    FloatingActionButton fb;
    FirebaseAuth mAuth;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_lib);

        dl = (DrawerLayout)findViewById(R.id.my_drawer_layout);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.nav_dashboard:
                        redirectActivity(UploadLibActivity.this, DashboardActivity.class);
                        return true;
                    case R.id.nav_personal_library:
                        redirectActivity(UploadLibActivity.this, UploadLibActivity.class);
                        return true;
                    case R.id.nav_account:
                        redirectActivity(UploadLibActivity.this, AccountActivity.class);
                        return true;
                    case R.id.aboutapp:
                        redirectActivity(UploadLibActivity.this, Aboutapp.class);
                        return true;
                    default:
                        return true;
                }


            }
        });



        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
//        mAuth = FirebaseAuth.getInstance();
//        if (mAuth.getCurrentUser() != null){
//            String EMAIL= mAuth.getCurrentUser().getEmail();



        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Books").orderByChild("email").startAt(email).endAt(email+"\uf8ff"), model.class)
                        .build();
        Log.v(email ,"Current user email obtained" );

        adapter=new MySecondadapter(options);
        recview.setAdapter(adapter);

//        }


        fb=(FloatingActionButton)findViewById(R.id.fadd);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddData.class));
            }
        });


    }


    public static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity, aClass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity(intent);

    }

    public void OnLogoutClick(MenuItem item) {
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getBaseContext(), gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(UploadLibActivity.this,
                new OnCompleteListener<Void>() {
                    //signout Google
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut(); //signout firebase
                        Intent setupIntent = new Intent(getBaseContext(), LoginActivity.class);
                        Toast.makeText(getBaseContext(), "Logged Out", Toast.LENGTH_LONG).show(); //if u want to show some text
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);
                        finish();
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.searchmenu,menu);

        MenuItem item=menu.findItem(R.id.search);

        android.widget.SearchView searchView=(android.widget.SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                displayuserbooks(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                displayuserbooks(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void displayuserbooks(String s)
    {
        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Books").orderByChild("email").startAt(s).endAt(s+"\uf8ff"), model.class)
                        .build();

        adapter=new MySecondadapter(options);
        adapter.startListening();
        recview.setAdapter(adapter);

    }
}




