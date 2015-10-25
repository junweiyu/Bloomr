package bloom.bloomr;

import android.location.Location;
import android.media.Image;

import java.util.ArrayList;

public class Event
{
    //Data fields
    private int id; //Account Balance
    private ArrayList<User> volunteers = new ArrayList<User>(); //Account annual interest rate
    private User owner;
    private String title;
    private Location location;
    private ArrayList<Image> images = new ArrayList<Image>();
    private int privacy;
    private String capacity;
    private String description;
    private String website;
    private String dates;
    private String place;

    /**
     * Constructor
     */
    public Event(User own, String titl, int priv, String descrip, String site, String date, String cap)
    {
        this.owner = own;
        this.title = titl;
        this.description = descrip;
        this.website = site;
        this.capacity = cap;
        this.privacy = priv;
        this.dates = date;
    }

    public void addVolunteer(User u) {
        volunteers.add(u);
    }


    //end of Constructor

    public ArrayList<User> getVolunteers() {
        return this.volunteers;
    }

    public User getOwner() {
        return this.owner;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getWebsite() {
        return this.website;
    }

    public String getCapacity() {
        return this.capacity;
    }

    public int getPrivacy() {
        return this.privacy;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getPlace() {
        return this.place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setLocation(Location loc) {
        this.location = loc;
    }

}