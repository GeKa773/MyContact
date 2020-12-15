package com.mobiledatabase.saveinformation.contactaddtobd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobiledatabase.saveinformation.contactaddtobd.databinding.ItemRecyclerViewBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private ArrayList<Contact> contactArrayList = new ArrayList<>();
    private MainActivity mainActivity;

    public ContactAdapter(ArrayList<Contact> contactArrayList, MainActivity mainActivity) {
        this.contactArrayList = contactArrayList;
        this.mainActivity = mainActivity;
    }

    public void setContactArrayList(ArrayList<Contact> contactArrayList) {
        this.contactArrayList = contactArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_recycler_view, parent, false);
//
//        return new ContactViewHolder(itemView);

        ItemRecyclerViewBinding itemRecyclerViewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_recycler_view,
                parent,
                false
        );
        return new ContactViewHolder(itemRecyclerViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, final int position) {
        final Contact contact = contactArrayList.get(position);
//        holder.firstNameTextView.setText(contact.getFirstName());
//        holder.lastNameTextView.setText(contact.getLastName());
//        holder.emailTextView.setText(contact.getEmail());
//        holder.phoneTextView.setText(contact.getPhone());

        holder.itemRecyclerViewBinding.setContact(contact);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.addAndEditContact(true, contact, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        //        private TextView firstNameTextView;
//        private TextView lastNameTextView;
//        private TextView emailTextView;
//        private TextView phoneTextView;
        private ItemRecyclerViewBinding itemRecyclerViewBinding;


        public ContactViewHolder(@NonNull ItemRecyclerViewBinding itemRecyclerViewBinding) {
            super(itemRecyclerViewBinding.getRoot());

            this.itemRecyclerViewBinding = itemRecyclerViewBinding;

//            firstNameTextView = itemView.findViewById(R.id.firstNameTextView);
//            lastNameTextView = itemView.findViewById(R.id.lastNameTextView);
//            emailTextView = itemView.findViewById(R.id.emailTextView);
//            phoneTextView = itemView.findViewById(R.id.phoneTextView);
        }
    }


}
