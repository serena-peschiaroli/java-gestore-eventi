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
        this.price = price;

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
        this.time = time;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    //methodi

    //metodo per restituire data ed ora formattata
    public String getDateTimeFormatted(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " " + time.format(formatter);

    }

    //metodo per restituire il prezzo formattato
    public String getPriceFormatted(){
        return price.setScale(2, RoundingMode.HALF_EVEN) + "€";
    }
}
