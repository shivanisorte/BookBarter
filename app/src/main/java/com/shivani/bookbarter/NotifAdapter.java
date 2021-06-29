package com.shivani.bookbarter;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotifAdapter extends FirebaseRecyclerAdapter<model,NotifAdapter.myviewholder>
{
    public NotifAdapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, final int position, @NonNull final model model)
    {
        holder.bookname.setText("Book Requested: "+model.getbookname());
        holder.personrequesting.setText("Person Requesting: " +model.getpersonrequesting());
        holder.timendate.setText("Time & Date: "+model.getTimendate());
        holder.location.setText("Location: "+model.getlocation());
        Glide.with(holder.img.getContext()).load("https://images.unsplash.com/photo-1476275466078-4007374efbbe?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mjh8fGJvb2t8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60").into(holder.img);

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerequest,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        CircleImageView img;
        TextView bookname, personrequesting, timendate, location;
        Button acceptbutton, declinebutton;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            img=(CircleImageView)itemView.findViewById(R.id.img1);
            bookname=(TextView)itemView.findViewById(R.id.booknametext);
            personrequesting=(TextView)itemView.findViewById(R.id.personrequesting);
            timendate=(TextView)itemView.findViewById(R.id.timendate);
            location=(TextView)itemView.findViewById(R.id.location);



            acceptbutton= (Button) itemView.findViewById(R.id.accept);
            declinebutton= (Button) itemView.findViewById(R.id.decline);
        }
    }
}

