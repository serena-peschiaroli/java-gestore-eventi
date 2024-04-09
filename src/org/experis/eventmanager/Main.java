package org.experis.eventmanager;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //apre lo scanner
        Scanner scanner = new Scanner(System.in);
        //inizializza collection eventi
        List<Event> events = new ArrayList<>();
        //flag per il loop
        boolean exit = false;

        while(!exit){
            System.out.println("Welcome! Would you like to: 1-Create an event; 2-Reserve seats; 3-Cancel reservation; 4-See all the events; -5 Exit: ");
            String choice = scanner.nextLine();

            switch (choice){
                //per creare un evento
                case "1":
                    System.out.println("Insert event title");
                    String title = scanner.nextLine();
                    // TODO: gestire le eccezioni per le date con formato errato senza interrompere il programma
                    System.out.println("Insert event date: ");
                    LocalDate date = LocalDate.parse(scanner.nextLine());
                    System.out.println("Insert the total capacity: ");
                    int seatCapacity = Integer.parseInt(scanner.nextLine());
                    try{
                        Event event = new Event(title, date, seatCapacity, 0);
                        events.add(event);
                        System.out.println("Event created successfully");
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }

                    break;
                    //per prenotare dei posti
                case "2":
                    System.out.println("To make a reservation, select an event: ");
                    for (int i = 0; i <events.size() ; i++) {
                        System.out.println(i + 1 + ": " + events.get(i).toString());

                    }
                    //indice dell'evento da scegliere
                    int eventIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    //gestione selezione non valida senza interrompere il loop
                    if(eventIndex < 0 || eventIndex >= events.size()){
                        System.out.println("Invalid selection");
                        //continue per non interrompere il loop
                        continue;
                    }
                    //evento selezionato
                    Event selectedEvent = events.get(eventIndex);
                    System.out.println("How many seats would like to reserve for " + selectedEvent);
                    // variabile per posti da prenotare
                    int seatsToReserve = Integer.parseInt(scanner.nextLine());
                    try{
                        selectedEvent.makeReservation(seatsToReserve);
                        System.out.println("Reservation successful, reserved seats: " + selectedEvent.getReservedSeat() + " available seats: " + (selectedEvent.getSeatCapacity() - selectedEvent.getReservedSeat()));
                    }catch(Exception e) {
                        System.out.println(e.getMessage());
                    }

                    break;
                    //per cancellare le prenotazioni
                case "3":
                    System.out.println("To cancel a reservation, select an event: ");
                    for (int i = 0; i <events.size() ; i++) {
                        System.out.println(i + 1 + ": " + events.get(i).toString());

                    }
                    eventIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    if(eventIndex < 0 || eventIndex >= events.size()){
                        System.out.println("Invalid selection");
                        continue;
                    }
                    selectedEvent = events.get(eventIndex);
                    System.out.println("How many reserved seats would you like to ancel for " + selectedEvent);
                    int reservedSeatsToCancel = Integer.parseInt(scanner.nextLine());
                    try{
                        selectedEvent.cancelReservation(reservedSeatsToCancel);
                        System.out.println("Cancellation successful, " + reservedSeatsToCancel);
                    }catch(Exception e) {
                        System.out.println(e.getMessage());
                    }

                    break;

                case "4":
                    //stampa di tutti gli eventi
                    System.out.println("All events: ");
                    for (Event e: events) {
                        System.out.println(e.toString());
                    }

                    break;
                    //uscita
                case "5":
                    exit = true;
                    System.out.println("Goodbye");
                    break;

                default:
                    System.out.println("Invalid choice");
                    break;





            }


        }

        scanner.close();



    }
}
