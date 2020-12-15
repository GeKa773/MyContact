package com.mobiledatabase.saveinformation.contactaddtobd.data;

import com.mobiledatabase.saveinformation.contactaddtobd.Contact;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface ContactDAO {
    @Insert
    public void addContact(Contact contact);

    @Update
    public void updateContact(Contact contact);

    @Delete
    public void deleteContact(Contact contact);

    @Query("select * from contacts_table")
    public List<Contact> getAllContacts();

    @Query("select * from contacts_table where contact_id ==:contactId")
    public Contact getContact(long contactId);
}
