package com.rushabh.contactapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rushabh.contactapp.R;
import com.rushabh.contactapp.data.Contact;
import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    // creating variables for our list, context, interface and position.
    private ArrayList<Contact> arrContactList;
    private Context context;
    private ContactInterface contactInterface;
    int lastPos = -1;

    // creating a constructor.
    public ContactAdapter(ArrayList<Contact> arrContactList, Context context, ContactInterface contactInterface) {
        this.arrContactList = arrContactList;
        this.context = context;
        this.contactInterface = contactInterface;
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // setting data to our recycler view item on below line.
        Contact contact = arrContactList.get(position);
        holder.tvContactName.setText(contact.getStrFirstName() +" " +contact.getStrLastName());
//        Picasso.get().load(contact.get()).into(holder.courseIV);
        // adding animation to recycler view item on below line.
        setAnimation(holder.itemView, position);
        holder.tvContactName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactInterface.onCourseClick(position);
            }
        });
    }

    private void setAnimation(View itemView, int position) {
        if (position > lastPos) {
            // on below line we are setting animation.
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos = position;
        }
    }

    @Override
    public int getItemCount() {
        return arrContactList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variable for our image view and text view on below line.
        private ImageView idIVContact;
        private TextView tvContactName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing all our variables on below line.
            idIVContact = itemView.findViewById(R.id.idIVContact);
            tvContactName = itemView.findViewById(R.id.tvContactName);
        }
    }

    // creating a interface for on click
    public interface ContactInterface {
        void onCourseClick(int position);
    }
}
