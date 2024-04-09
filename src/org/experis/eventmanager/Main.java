package org.experis.eventmanager;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    //file path dove sono salvati i dettagli
    private final static String FILE_PATH = "./resources/event_list.txt";

    public static void main(String[] args) {

        //apre lo scanner
        Scanner scanner = new Scanner(System.in);
        //inizializza collection eventi
       //dichiarazione arraylist e caricamento degli eventi precedentemente inseriti
        List<Event> events = readFile();
        //flag per il loop
        boolean exit = false;

        while(!exit){
            //menu opzioni
            System.out.println("Welcome! Would you like to: 1-Create an event; 2-Reserve seats; 3-Cancel reservation; 4-See all the events; -5 Exit: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    //crea un nuovo evento
                    createEvent(scanner, events);
                    break;
                case "2":
                    //prenota dei posti
                    reserveSeats(events, scanner);
                    break;
                case "3":
                    //cancella prenotazioni
                    cancelReservation(events, scanner);
                    break;
                case "4":
                    //mostra tutti gli eventi
                    showEvents(events);
                    break;
                case "5":
                    //esce dal menu
                    exit = true;
                    System.out.println("Goodbye");
                    //salva gli eventi modificati
                    writeToFile(events);
                    break;
                default:
                    //gestisce input non validi
                    System.out.println("Invalid choice");
                    break;
            }

        }
        //chiude lo scanner
        scanner.close();

        //controllare se il salvataggio va a buon fine
        if(!writeToFile(events)){
            System.out.println("Error writing to file");
        }

    }

    //metodo per creare eventi ed aggiungerli alla lista
    private static void createEvent(Scanner scanner, List<Event> events) {
        System.out.println("Insert event title");
        String title = scanner.nextLine();
        LocalDate date = null;
        //loop per validare la data senza interrompere il programma
        while(date == null){
            System.out.println("Insert event date: ");
            String dateString = scanner.nextLine();

            try {
                //cerca di parsare le date
                date = LocalDate.parse(dateString);
            }catch(Exception e){
                System.out.println("Invalid date format, please use YYYY-mm-dd");

            }

        }

        System.out.println("Insert the total capacity: ");
        int seatCapacity = Integer.parseInt(scanner.nextLine());
        try{
            //aggiunge un nuovo evento alla lista
            Event event = new Event(title, date, seatCapacity, 0);
            events.add(event);
            System.out.println("Event created successfully");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //metodo per selezionare un evento dall'elenco
    private static Event selectEvent(List<Event> events, Scanner scanner) {
        if(events.isEmpty()) {
            System.out.println("No events to select.");
            return null;
        }

        Event selectedEvent = null;

        do {
            System.out.println("Select an event (or 0 to cancel");
            //visualizza tutti gli eventi con un indice
            for (int i = 0; i < events.size(); i++) {
                System.out.println((i + 1) + ": " + events.get(i).getTitle());
            }
            //input dall'utente per scegliere evento
            String input = scanner.nextLine();
            int eventIndex;
            try {
                eventIndex = Integer.parseInt(input) - 1;
                if(eventIndex == -1){
                    //cancella la selezione ddell'utente
                    return null;
                }
                //se la selezione è valida, assegna l'evento selezionato
                if (eventIndex >= 0 && eventIndex < events.size()) {

                    selectedEvent = events.get(eventIndex);
                    return selectedEvent;
                }else{
                    System.out.println("Invalid selection, please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }while(selectedEvent == null);
        return selectedEvent;
    }

    //metodo per creare una prenotazione per un evento specifico
    private static void reserveSeats(List<Event> events, Scanner scanner) {
        Event selectedEvent = selectEvent(events, scanner);
        //se nessun evento selezionato, termina il metodo
        if (selectedEvent == null) return;

        int seatsToReserve = 0;
        try {
            seatsToReserve = Integer.parseInt(scanner.nextLine());
            //controlla che il numero di posto sia positivo
            if (seatsToReserve <= 0) {
                throw new IllegalArgumentException("Number of seats to reserve must be positive.");
            }
            //effettua la prenotazione e informa del successo
            selectedEvent.makeReservation(seatsToReserve);
            System.out.println("Reservation successful. Reserved seats: " + selectedEvent.getReservedSeat() + ". Available seats: " + (selectedEvent.getSeatCapacity() - selectedEvent.getReservedSeat()));
        } catch (NumberFormatException e) {
            //gestisce l'input non numerico
            System.out.println("Please enter a valid number.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            //gestisce errori specifi alla prenotazione
            System.out.println(e.getMessage());
        }
    }

    //metodo per cancellare una prenotazione per evento specifico
    private static void cancelReservation(List<Event> events, Scanner scanner) {
        Event selectedEvent = selectEvent(events, scanner);
        //termina metodo se nessun evento è selezionato
        if (selectedEvent == null) return;

        int reservedSeatsToCancel = 0;
        try {
            reservedSeatsToCancel = Integer.parseInt(scanner.nextLine());
            //controlla che il numero di posti da cancellare sia positivo
            if (reservedSeatsToCancel <= 0) {
                throw new IllegalArgumentException("number of seats to cancel must be positive");
            }
            //controlla che le cancellazioni non siano superiori ai posti prenotati
            if (reservedSeatsToCancel > selectedEvent.getReservedSeat()) {
                throw new IllegalArgumentException("cannot cancel more seats than are currently reserved");
            }
            selectedEvent.cancelReservation(reservedSeatsToCancel);
            System.out.println("cancellation succesful. " + reservedSeatsToCancel + " seats cancelled. Now " + selectedEvent.getReservedSeat() + " seats are reserved. Available: " + (selectedEvent.getSeatCapacity() - selectedEvent.getReservedSeat()));
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    //mostra gli eventi
    private static void showEvents(List<Event> events) {
        if(events.isEmpty()) {
            System.out.println("No events available.");
            return;
        }
        System.out.println("All events:");
        events.forEach(event -> System.out.println(event.toString()));
    }

    //metodo per salvare gli eventi e renderli permanenti
     private static boolean writeToFile(List<Event> events){
        File eventsFile = new File(FILE_PATH);
        boolean written = false;
        try(FileWriter writer = new FileWriter(eventsFile, false)){
            for (Event event : events){
                writer.write(event.getTitle() + ";" + event.getDate() + ";" + event.getSeatCapacity() + "; " + event.getReservedSeat() + "\n");

            }
            written = true;

        }catch (IOException e){
            System.out.println("Unbale to write file");
        }
        return written;
     }

     //metodo per leggere i dettagli degli eventi da un file e ripopolarli in una collection

    private static List<Event> readFile(){
        //inizlizza una lista vuota
        List<Event> events = new ArrayList<>();
        //crea un oggetto file
        File eventFile = new File(FILE_PATH);

        //apre il file per la lettura
        try(Scanner scanner = new Scanner(eventFile)){
            //legge il file linea per linea
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                //divide la linea in parti
                String[] parts = line.split(";");
                if(parts.length == 4){
                    String title = parts[0];
                    LocalDate date =  LocalDate.parse(parts[1]);
                    int seatCapacity = Integer.parseInt(parts[2]);
                    int reservedSeat = Integer.parseInt(parts[3]);
                    events.add(new Event(title, date, seatCapacity, reservedSeat));

                }
            }
        }catch(FileNotFoundException e){
            System.out.println("Unable to read file");
        }
        return events;
    }

}
