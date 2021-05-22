package com.shivani.bookbarter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class UploadLibActivity extends AppCompatActivity {
    public static final String ARTIST_NAME = "net.simplifiedcoding.firebasedatabaseexample.artistname";
    public static final String ARTIST_ID = "net.simplifiedcoding.firebasedatabaseexample.artistid";

    //view objects
    EditText editTextName;
    Spinner spinnerGenre;
    Button buttonAddAuthor;
    ListView listViewAuthor;
    List<Authors> authors;

    DatabaseReference databaseAuthors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_lib);
        databaseAuthors = FirebaseDatabase.getInstance().getReference("authors");

        //getting views by ID
        editTextName = (EditText) findViewById(R.id.editName);
        spinnerGenre = (Spinner) findViewById(R.id.spinnerGenres);
        listViewAuthor = (ListView) findViewById(R.id.listViewAuthors);

        buttonAddAuthor = (Button) findViewById(R.id.buttonAddAuthor);

        //list to store authors
        authors = new ArrayList<>();


        //adding an onclicklistener to button
        buttonAddAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addAuthor()
                //the method is defined below
                //this method is actually performing the write operation
                addAuthor();
            }
    });
}
    private void addAuthor() {
        //getting the values to save
        String name = editTextName.getText().toString().trim();
        String genre = spinnerGenre.getSelectedItem().toString();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseAuthors.push().getKey();

            //creating an Author Object
            Authors author = new Authors(id, name, genre);

            //Saving the Author
            databaseAuthors.child(id).setValue(author);

            //setting edittext to blank again
            editTextName.setText("");

            //displaying a success toast
            Toast.makeText(this, "Author added successfully", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseAuthors.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                authors.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Authors author = postSnapshot.getValue(Authors.class);
                    //adding artist to the list
                    authors.add(author);
                }

                //creating adapter
                AuthorList artistAdapter = new AuthorList(UploadLibActivity.this, authors);
                //attaching adapter to the listview
                listViewAuthor.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
