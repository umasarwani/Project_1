package com.hotelreservation.model;

import java.util.*;

public class Room implements IRoom {
    protected String roomNumber;
    protected double price;
    protected RoomType roomType;

    public Room(String roomNumber, Double price, RoomType roomType) {
              this.roomNumber = roomNumber;
               this.price = price;
               this.roomType = roomType;
    }


    public final String getRoomNumber() {
         return roomNumber;

    }
    public  Double getRoomPrice() {

        return price;
    }

    public final RoomType getRoomType() {

        return roomType;
    }


    public boolean isFree() {

        return false;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", price=" + price +
                ", roomType=" + roomType +
                '}';
    }

}
