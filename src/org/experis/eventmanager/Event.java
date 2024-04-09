package org.experis.eventmanager;

import java.time.LocalDate;

public class Event {

    //attributi
    private String title;
    private LocalDate date;
    private int seatCapacity;
    private int reservedSeat;
    //costrutture
    public Event(String title, LocalDate date, int seatCapacity, int reservedSeat) {
        this.title = title;
        this.date = date;
        this.seatCapacity = seatCapacity;
        this.reservedSeat = 0;
        validateDate();
        validateSeatCapacity();
    }
    //getters

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getSeatCapacity() {
        return seatCapacity;
    }

    public int getReservedSeat() {
        return reservedSeat;
    }

    //setters

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    //metodi

    private void validateDate(){
        if(date.isBefore(LocalDate.now())){
            throw new IllegalArgumentException("The event date cannot be in the past.");
        }
    }

    private void validateSeatCapacity(){
        if(seatCapacity <= 0){
            throw new IllegalArgumentException("Total seat capacity must be a positive number.");
        }
    }

    //metodo per prenotazione
    public void makeReservation(int seatsToReserve){
        if(date.isBefore(LocalDate.now()) || reservedSeat + seatsToReserve > seatCapacity){
            throw new IllegalStateException("Unable to reserve the seats you requested.");
        }
        reservedSeat += seatsToReserve ;
    }

    //metodo per disdire
    public void cancelReservation(int reservedSeatsToCancel){
        if(date.isBefore(LocalDate.now())||reservedSeatsToCancel > reservedSeat){
            throw new IllegalStateException("Unable to cancel the reserved seats");
        }
        reservedSeat -= reservedSeatsToCancel;

    }

    //override di toString
    @Override
    public String toString() {
        return date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " + title;
    }

}
