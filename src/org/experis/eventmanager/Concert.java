package org.experis.eventmanager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Concert extends Event{

    //attributi
    private LocalTime time;
    private BigDecimal price;

    //costrutture
    public Concert(String title, LocalDate date, int seatCapacity, int reservedSeat, LocalTime time, BigDecimal price) {
        super(title, date, seatCapacity, reservedSeat);
        this.time = time;
        validateTime(time);
        this.price = price;
        validatePrice(price);

    }

    //getters

    public LocalTime getTime() {
        return time;
    }

    public BigDecimal getPrice() {
        return price;
    }

    //setters

    public void setTime(LocalTime time) {
        validateTime(time);
        this.time = time;
    }

    public void setPrice(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    //methodi

    //metodo per restituire data ed ora formattata
    public String getDateTimeFormatted(){
        // fomrattazione della data
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = getDate().format(dateFormatter);

        // formattazione dell'ora
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = time.format(timeFormatter);

        return formattedDate + " " + formattedTime;

    }

    //metodo per restituire il prezzo formattato
    public String getPriceFormatted(){
        return price.setScale(2, RoundingMode.HALF_EVEN) + "€";
    }

    //override di tostring


    @Override
    public String toString() {
        return  getDateTimeFormatted() + " - " + getTitle() + " - " + getPriceFormatted();
    }

    //validare l'ora
    private void validateTime(LocalTime time) {
        // l'ora dell'evento non deve essere passata
        if (getDate().isEqual(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("The concert time cannot be in the past.");
        }
    }

    //validare il prezzo
    private void validatePrice(BigDecimal price){
        if (price.compareTo(BigDecimal.ZERO) < 0 ) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
    }

    //metodo per cacolare il prezzo
    public BigDecimal calculatePrice(int seats){
        return price.multiply(new BigDecimal(seats));
    }

    //override di makeReservation


    @Override
    public void makeReservation(int seats) {
        super.makeReservation(seats);
    }
}
