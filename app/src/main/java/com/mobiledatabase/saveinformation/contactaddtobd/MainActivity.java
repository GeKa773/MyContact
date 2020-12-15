package com.mobiledatabase.saveinformation.contactaddtobd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobiledatabase.saveinformation.contactaddtobd.data.ContactDatabase;
import com.mobiledatabase.saveinformation.contactaddtobd.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Contact> contactArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private ContactDatabase contactDatabase;
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMainBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_main);

        recyclerView = activityMainBinding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        contactAdapter = new ContactAdapter(contactArrayList, MainActivity.this);
        recyclerView.setAdapter(contactAdapter);


        contactDatabase = Room.databaseBuilder(
                getApplicationContext(),
                ContactDatabase.class,
                "ContactsDB")
                .build();

        loadContacts();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Contact contact = contactArrayList.get(viewHolder.getAdapterPosition());
                deleteContact(contact);
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void loadContacts() {
        new GetAllContactAsyncTask().execute();

    }

    private void deleteContact(Contact contact) {
        new DeleteContactAsyncTask().execute(contact);
    }

    private void addContact(String firstName, String lastName, String email, String phone) {

        Contact contact = new Contact(
                0,
                firstName,
                lastName,
                email,
                phone
        );

        new AddContactAsyncTask().execute(contact);
    }

    private void updateContact(String firstName, String lastName, String email, String phone, int position) {

        Contact contact = contactArrayList.get(position);
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setEmail(email);
        contact.setPhone(phone);
        new UpdateContactAsyncTask().execute(contact);

        contactArrayList.set(position, contact);
    }

    public void addAndEditContact(final boolean isUpdate, final Contact contact, final int position) {

        LayoutInflater layoutInflater = LayoutInflater
                .from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.add_edit_contact, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        TextView contactTitleTextView = view.findViewById(R.id.contactTitleTextView);
        final EditText firstNameEditText = view.findViewById(R.id.firstNameEditText);
        final EditText lastNameEditText = view.findViewById(R.id.lastNameEditText);
        final EditText emailEditText = view.findViewById(R.id.emailEditText);
        final EditText phoneEditText = view.findViewById(R.id.phoneEditText);

        contactTitleTextView.setText(!isUpdate ? "Add contact" : "Edit contact");
        if (isUpdate && contact != null) {
            firstNameEditText.setText(contact.getFirstName());
            lastNameEditText.setText(contact.getLastName());
            emailEditText.setText(contact.getEmail());
            phoneEditText.setText(contact.getPhone());
        }
        builder.setCancelable(false).setPositiveButton(isUpdate ? "Update" : "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(firstNameEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter first name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(lastNameEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter last name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(emailEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter email name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(phoneEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter phone name", Toast.LENGTH_SHORT).show();
                } else {
                    if (isUpdate && contact != null) {
                        updateContact(
                                firstNameEditText.getText().toString(),
                                lastNameEditText.getText().toString(),
                                emailEditText.getText().toString(),
                                phoneEditText.getText().toString(),
                                position
                        );
                    } else {
                        addContact(
                                firstNameEditText.getText().toString(),
                                lastNameEditText.getText().toString(),
                                emailEditText.getText().toString(),
                                phoneEditText.getText().toString()
                        );
                    }
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void addAndEditContactClick(View view) {
        addAndEditContact(false, null, -1);
    }

    private class GetAllContactAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            contactArrayList = (ArrayList<Contact>) contactDatabase.getContactDAO().getAllContacts();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            contactAdapter.setContactArrayList(contactArrayList);
        }
    }

    private class DeleteContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDatabase.getContactDAO().deleteContact(contacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadContacts();
        }
    }

    private class AddContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDatabase.getContactDAO().addContact(contacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadContacts();
        }
    }

    private class UpdateContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDatabase.getContactDAO().updateContact(contacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadContacts();
        }
    }
}