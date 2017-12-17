package com.costa.androidmobileapp.Model;

/**
 * Created by CristianCosmin on 08.11.2017.
 */

public class Event {

    int idEvent;
    String cardName;
    int nrOfPeople;
    String location;
    String nameOrganizor;
    int imageResourceId;

    public Event() {
    }

    public Event(int idEvent, String cardName, int nrOfPeople, String location, String nameOrganizor, int imageResourceId) {
        this.idEvent = idEvent;
        this.cardName = cardName;
        this.nrOfPeople = nrOfPeople;
        this.location = location;
        this.nameOrganizor = nameOrganizor;
        this.imageResourceId = imageResourceId;
    }

    public Event(String cardName, int nrOfPeople, String location, String nameOrganizor, int imageResourceId) {
        this.cardName = cardName;
        this.nrOfPeople = nrOfPeople;
        this.location = location;
        this.nameOrganizor = nameOrganizor;
        this.imageResourceId = imageResourceId;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public int getNrOfPeople() {
        return nrOfPeople;
    }

    public void setNrOfPeople(int nrOfPeople) {
        this.nrOfPeople = nrOfPeople;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNameOrganizor() {
        return nameOrganizor;
    }

    public void setNameOrganizor(String nameOrganizor) {
        this.nameOrganizor = nameOrganizor;
    }
}