package Model;

import java.util.Hashtable;

public class Contact {
    private int contactId;
    private String contact;
    private String email;


    public Contact(int contactId, String contact, String email){
        this.contactId = contactId;
        this.contact = contact;
        this.email = email;
    }

    public int getContactId() {return contactId;}
    public String getContact() {return contact;}
    public String getEmail() {return email;}


}
