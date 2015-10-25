package bloom.bloomr;


import java.util.ArrayList;

public class User
{
    //Data fields
    private int id; //Account Balance
    private ArrayList<Event> upcomingEvents = new ArrayList<Event>(); //Account annual interest rate
    private ArrayList<Event> completedEvents;
    private String password;
    private String name;
    private String username;
    private double rating = 5;
    private ArrayList<Event> ownEvents = new ArrayList<Event>(); //Account annual interest rate
    private ArrayList<String> interests = new ArrayList<String>();

    /**
     * Constructor
     */
    public User(int userid, String user, String pass, String n)
    {
        this.id = userid;
        this.username = user;
        this.password = pass;
        this.name = n;
    }
    //end of Constructor

    /**
     */
    public int getId()
    {
        return this.id;
    }

    public ArrayList<Event> getUpcomingEvents() {
        return this.upcomingEvents;
    }

    public ArrayList<Event> getOwnEvents() {
        return this.ownEvents;
    }

    public double getRating() {
        return this.rating;
    }

    public ArrayList<String> getInterests() {
        return this.interests;
    }

    public String getName() {
        return this.name;
    }

    public void addEvent(Event e) {
        upcomingEvents.add(e);
    }

    public void makeEvent(Event e) {
        ownEvents.add(e);
    }
}
//end of SavingsAccount class