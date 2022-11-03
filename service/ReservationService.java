package com.hotelreservation.service;

import com.hotelreservation.model.Customer;
import com.hotelreservation.model.IRoom;
import com.hotelreservation.model.Reservation;
import com.hotelreservation.model.Room;

import java.util.*;


public class ReservationService {
    private ReservationService() {
    }

    private final static ReservationService reservationServiceInstance = new ReservationService();

    public static ReservationService reservationServiceInstance() {
        return reservationServiceInstance;
    }

    private Collection<Reservation> reservationsList = new ArrayList<>();

    private Map<String, IRoom> roomMap = new HashMap<>();

    public void addRoom(IRoom room) {
        String roomNumber = room.getRoomNumber();
        if (!roomAlreadyExist(roomNumber, roomMap.values())) {
            Room NewRoom = new Room(room.getRoomNumber(), room.getRoomPrice(), room.getRoomType());
            roomMap.put(NewRoom.getRoomNumber(), NewRoom);

        }
    }


    private boolean roomAlreadyExist(String roomNumber, Collection<IRoom> roomMap) {
        boolean isSameRoom = false;
        for (IRoom rooms : roomMap) {
            if (rooms.getRoomNumber().equalsIgnoreCase(roomNumber)) {
                isSameRoom = true;
                return isSameRoom;
            }

        }

        return isSameRoom;
    }

    public IRoom getRoom(String roomId) {
        return roomMap.get(roomId);

    }


    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {

        Reservation newRoomReservation = null;

        if (requestedRoomIsNotAvailableOnCheckInCheckOutDates(room, checkInDate, checkOutDate)) {
            System.out.println(" Sorry, This room is already booked");
        } else {
            Customer customerObj = CustomerService.customerServiceInstance().getCustomer(customer.getEmail());
            IRoom roomObject = ReservationService.reservationServiceInstance.getRoom(room.getRoomNumber());

            newRoomReservation = new Reservation(customerObj, roomObject, checkInDate, checkOutDate);
            reservationsList.add(newRoomReservation);
            System.out.println("Your reservation is confirmed on " + newRoomReservation);

        }

        return newRoomReservation;
    }
    private boolean reservationHasSameRoom(Reservation reservation, String roomNumber) {

    boolean hasSameRoomNumber=false;
    if(reservation.getRoom().getRoomNumber().equalsIgnoreCase(roomNumber))
        hasSameRoomNumber=true;

        return hasSameRoomNumber;
    }


    private boolean requestedRoomIsNotAvailableOnCheckInCheckOutDates(IRoom room, Date checkInDate, Date checkOutDate) {
        boolean isAlreadyReserved = false;
        for (Reservation reservationReserved : reservationsList) {
            if (reservingSameRoom(reservationReserved, room)) {
                if (reservationNotAvailableWithinDates(reservationReserved, checkInDate, checkOutDate)) {
                    isAlreadyReserved = true;
                    break;
                }
            }


        }
        return isAlreadyReserved;
    }


    public boolean listNoReservations(IRoom room, Date checkInDate, Date checkOutDate) {
        boolean isAlreadyReservedExcluded = true;
        for (Reservation noReservations : reservationsList) {
            if (reservingSameRoom(noReservations, room)) {
                if (reservationNotAvailableWithinDates(noReservations, checkInDate, checkOutDate)) {
                    isAlreadyReservedExcluded = false;
                    break;

                }
                System.out.println(noReservations.toString());
            }


        }
        return isAlreadyReservedExcluded;
    }

    public void printNoReservations() {

        System.out.println(reservationsList.toString());
        if (reservationsList == null) {
            System.out.println(reservationsList.toString());
        }
    }


    private boolean reservingSameRoom(Reservation reservation, IRoom room) {
        boolean isSameRoom = false;
        if (reservation.getRoom().getRoomNumber().equalsIgnoreCase(room.getRoomNumber()))
            isSameRoom = true;
        return isSameRoom;

    }

    //Reserve room is occupied on same date
//Reservation has some conflict
    private boolean reservationNotAvailableWithinDates(Reservation reservation, Date checkInDate, Date checkOutDate) {
        boolean hasDateConflict = false;
        if (RequestedCheckInDateIsNotAvailable(checkInDate, reservation) ||
                RequestedCheckOutDateIsNotAvailable(checkOutDate, reservation) ||
                requestedCheckInAndOutDatesAreWithinReservationCheckInDates(reservation, checkInDate, checkOutDate) ||
                requestCheckInAndOutDatesAreWithinRequestedCheckOutDates(reservation, checkInDate, checkOutDate)
        ) {

            hasDateConflict = true;
        }


        return hasDateConflict;


    }

    private boolean RequestedCheckOutDateIsNotAvailable(Date checkOutDate, Reservation reservation) {
        boolean isBetweenTrueFlag = false;

        if (checkOutDate.compareTo(reservation.getCheckInDate()) > 0 && checkOutDate.compareTo(reservation.getCheckOutDate()) < 0)
            isBetweenTrueFlag = true;
        return isBetweenTrueFlag;
    }


    private boolean requestedCheckInAndOutDatesAreWithinReservationCheckInDates(Reservation reservation, Date checkInDate, Date checkOutDate) {
        boolean hasDateConflict = false;
        if (checkOutDate.compareTo(reservation.getCheckInDate()) > 0 && checkOutDate.compareTo(reservation.getCheckOutDate()) < 0)
            ;
        hasDateConflict = true;
        return hasDateConflict;
    }


    private boolean requestCheckInAndOutDatesAreWithinRequestedCheckOutDates(Reservation reservation, Date checkInDate, Date checkOutDate) {
        boolean hasDateConflict = false;
        if (checkOutDate.compareTo(reservation.getCheckInDate()) > 0 && checkOutDate.compareTo(reservation.getCheckOutDate()) < 0)
            ;
        hasDateConflict = true;

        return hasDateConflict;
    }

    private boolean RequestedCheckInDateIsNotAvailable(Date checkInDate, Reservation reservation) {
        boolean isBetweenTrueFlag = false;

        if (checkInDate.compareTo(reservation.getCheckInDate()) > 0 && checkInDate.compareTo(reservation.getCheckOutDate()) < 0)
            isBetweenTrueFlag = true;
        return isBetweenTrueFlag;
    }

    private Date weekAfter(Date date) {
        long datePlus7Days = date.getTime() + 7 * 24 * 60 * 1000;
        return new Date(datePlus7Days);
    }

    //using while loop here
    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {

         List<String> conflictRoomNumberList = new ArrayList<>();
        List<IRoom> availableRooms = new ArrayList<>();
        conflictRoomNumberList = getConflictRoomNumbers(reservationsList, checkInDate, checkOutDate);

        Iterator<Map.Entry<String, IRoom>> iterator = roomMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, IRoom> entry = iterator.next();
            String roomNumber = entry.getKey();
            if (noConflictWithRoom(conflictRoomNumberList, roomNumber))
                availableRooms.add(entry.getValue());

        }
        return availableRooms;


    }

    public Collection<IRoom> newRoomList(Date checkInDate, Date checkOutDate) {

        ArrayList<IRoom> bookedRoomList = new ArrayList<>();
        List<IRoom>reserved= new ArrayList<>();
        Collection<IRoom> allRoomList=new ArrayList<>(roomMap.values());
        Collection<IRoom> allRoomList1=new ArrayList<>(roomMap.values());
        reserved = getConflictRoomNumbers1( reservationsList, checkInDate, checkOutDate);
        if(reservationsList.isEmpty())
        {
        return allRoomList1;
        }else
        {
            ListIterator<IRoom> roomIterator = reserved.listIterator();
            Iterator<IRoom> filteredIterator = allRoomList1.iterator();
            while (filteredIterator.hasNext()) {
                IRoom iroom = filteredIterator.next();
                while (roomIterator.hasNext()) {
                    IRoom unAvailable = roomIterator.next();
                    if (iroom.getRoomNumber() == unAvailable.getRoomNumber()) {
                        filteredIterator.remove();
                    }
                }
            }
            return allRoomList;
        }
    }

    List<IRoom> getConflictRoomNumbers1(Collection<Reservation> reservations, Date checkInDate, Date checkOutDate) {
        ArrayList<IRoom> conflictRoomNumberList = new ArrayList<>();
        for (Reservation eachReservation : reservations) {
            if (reservationNotAvailable(eachReservation, checkInDate, checkOutDate)) {
                conflictRoomNumberList.add(eachReservation.getRoom());
            }
        }
        return conflictRoomNumberList;
    }



    private boolean noConflictWithRoom(List<String> conflictRoomNumberList, String roomNumber) {

        boolean noConflictFlag = true;
        for (String thisRoomNumber : conflictRoomNumberList) {
            if (thisRoomNumber.equalsIgnoreCase(roomNumber)) {
                noConflictFlag = false;
                break;
            }
        }
        return noConflictFlag;
    }

    List<String> getConflictRoomNumbers(Collection<Reservation> reservations, Date checkInDate, Date checkOutDate) {
        List<String> conflictRoomNumberList = new ArrayList<>();
        for (Reservation eachReservation : reservations) {
            if (reservationNotAvailable(eachReservation, checkInDate, checkOutDate)) {
                conflictRoomNumberList.add(eachReservation.getRoom().getRoomNumber());
            }
        }
        return conflictRoomNumberList;
    }

    private boolean reservationNotAvailable(Reservation eachReservation, Date checkInDate, Date checkOutDate) {
        boolean noConflictFlag = false;
        if(reservationNotAvailableWithinDates(eachReservation,checkInDate,checkOutDate)){
            noConflictFlag=true;
            }


        return noConflictFlag;
    }


    public void getAllReservations(Collection<Reservation> reservationsList) {
        int index = 1;
        for (Reservation reservation : reservationsList) {
            System.out.println("Reservation No." + index + ":");
            System.out.println("\t" + reservation);
              System.out.println(" ");
            index++;

        }



    }

    public void printAllReservation() {
        System.out.println(reservationsList.toString());
        if (reservationsList==null){
            System.out.println(reservationsList.toString());
        }
    }

    public void printRooms(Collection<IRoom> rooms) {
        int index = 1;
        for (IRoom eachRoom : rooms) {
            System.out.println("Rooms List");
            System.out.println("#" + index + ":");

            System.out.println("\t+  Room Number:" + eachRoom.getRoomNumber());
            System.out.println("\t+  Price $ :" + eachRoom.getRoomPrice());
            System.out.println("\t+  Room Type:" + eachRoom.getRoomType());
            System.out.println(" ");
            index++;

        }
    }

    public Collection<Reservation> getCustomerReservation(String customerEmail) {
        List<Reservation> customerReservationList = new ArrayList<>();

        for (Reservation reservation : reservationsList) {
             if( reservation.getCustomer().getEmail().equalsIgnoreCase(customerEmail)){
               customerReservationList.add(reservation);
           }
        }
        return customerReservationList;
    }


    public Collection<IRoom> getAllRooms() {
        return roomMap.values();

    }
   /*public void printRooms3(Collection<IRoom> rooms,String message) {
        int index = 1;
       System.out.println(message);
       System.out.println(" these are rooms after 7 days");
        for (IRoom eachRoom : rooms) {
            System.out.println("Rooms List");
            System.out.println("#" + index + ":");
            System.out.println("\t+  Room Number:" + eachRoom.getRoomNumber());
            System.out.println("\t+  Price $ :" + eachRoom.getRoomPrice());
            System.out.println("\t+  Room Type:" + eachRoom.getRoomType());
            System.out.println(" ");
            index++;
        }

        }*/





}


