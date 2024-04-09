package org.experis.eventmanager;



import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    //file path dove sono salvati i dettagli
    private final static String FILE_PATH = "./resources/event_list.txt";

    public static void main(String[] args) {

        //apre lo scanner
        Scanner scanner = new Scanner(System.in);
        //inizializza collection eventi
        EventManager eventManager = new EventManager("EventManager Title");
        // Initialize FileManager with the path to your events file.
        FileManager fileManager = new FileManager(FILE_PATH);

        // Use FileManager to load events into EventManager.
        List<Event> loadedEvents = fileManager.readEvents();
        loadedEvents.forEach(eventManager::addEvent);

        //flag per il loop
        boolean exit = false;

        while(!exit){
            //menu opzioni
            System.out.println("Welcome! Would you like to: 1-Create an event; 2-Reserve seats; 3-Cancel reservation; 4-See all the events; -5 Exit: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    //crea un nuovo evento
                    createEvent(scanner, eventManager);
                    break;
                case "2":
                    //prenota dei posti
                    reserveSeats(eventManager, scanner);
                    break;
                case "3":
                    //cancella prenotazioni
                    cancelReservation(eventManager, scanner);
                    break;
                case "4":
                    //mostra tutti gli eventi
                    showEvents(eventManager);
                    break;
                case "5":
                    //esce dal menu
                    exit = true;
                    System.out.println("Goodbye");
                    if (fileManager.writeEvents(eventManager.getEvents())) {
                        System.out.println("Events saved successfully.");
                    } else {
                        System.out.println("Failed to save events.");
                    }
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
        if (!fileManager.writeEvents(eventManager.getEvents())) {
            System.out.println("Error writing to file");
        }

    }

    //metodo per creare eventi ed aggiungerli alla lista
    private static void createEvent(Scanner scanner, EventManager eventManager) {
        String eventType = getEventType(scanner);
        String title = getEventTitle(scanner);
        LocalDate date = getEventDate(scanner);
        int seatCapacity = getSeatCapacity(scanner);

        if ("1".equals(eventType)) {
            eventManager.addEvent(new Event(title, date, seatCapacity, 0));
            System.out.println("Event created successfully.");
        } else if ("2".equals(eventType)) {
            Concert concert = getConcertDetails(title, date, seatCapacity, scanner);
            eventManager.addEvent(concert);
            System.out.println("Concert created successfully.");
        }
    }
    private static int getSeatCapacity(Scanner scanner) {
        System.out.println("Insert the total capacity: ");
        //TODO: try catch per validare input
        return Integer.parseInt(scanner.nextLine());
    }
    private static Concert getConcertDetails(String title, LocalDate date, int seatCapacity, Scanner scanner) {
        LocalTime time = null;
        BigDecimal price = getConcertPrice(scanner);
        while(time == null){
            System.out.println("Insert concert time (HH:MM): ");
            try {
                time = LocalTime.parse(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid time format, please use hh:mm.");
            }

        }

        return new Concert(title, date, seatCapacity, 0, time, price);
    }
    private static BigDecimal getConcertPrice(Scanner scanner) {
        BigDecimal price = null;
        while (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Insert concert ticket price: ");
            try {
                price = new BigDecimal(scanner.nextLine());
                if (price.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Price must be positive.");
                    price = null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid price format, please enter a valid numeric value.");
            }
        }
        return price;
    }

    private static LocalDate getEventDate(Scanner scanner) {
        LocalDate date = null;
        while (date == null) {
            System.out.println("Insert event date (YYYY-MM-DD): ");
            try {
                date = LocalDate.parse(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid date format, please use YYYY-MM-DD.");
            }
        }
        return date;
    }

    private static String getEventType(Scanner scanner) {
        System.out.println("Insert event type (1 for generic Event, 2 for Concert): ");
        return scanner.nextLine();
    }

    private static String getEventTitle(Scanner scanner) {
        System.out.println("Insert event title: ");
        return scanner.nextLine();
    }

    //metodo per selezionare un evento dall'elenco
    private static Event selectEvent(EventManager eventManager, Scanner scanner) {
        // Ottiene la lista di eventi da EventManager
        List<Event> events = eventManager.getEvents();
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
                    System.out.println("selection cancelled");
                    return null;
                }
                //se la selezione è valida, assegna l'evento selezionato
                if (eventIndex >= 0 && eventIndex < events.size()) {

                    selectedEvent = events.get(eventIndex);
                    System.out.printf("You've selected: " + selectedEvent.getTitle() + " ");
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
    private static void reserveSeats(EventManager eventManager, Scanner scanner) {
        Event selectedEvent = selectEvent(eventManager, scanner);
        //se nessun evento selezionato, termina il metodo
        if (selectedEvent == null) return;
        System.out.println("Enter the number of seat to reserve: ");

        int seatsToReserve = getIntInput(scanner);
        try {
            //controlla che il numero di posto sia positivo
            if (seatsToReserve <= 0) {
                throw new IllegalArgumentException("Number of seats to reserve must be positive.");
            }
            //effettua la prenotazione e informa del successo
            selectedEvent.makeReservation(seatsToReserve);
            if (selectedEvent instanceof Concert) {
                Concert concert = (Concert) selectedEvent;
                BigDecimal totalPrice = concert.calculatePrice(seatsToReserve);
                System.out.println("Reservation successful. Total price: " + totalPrice + "Reserved seats:  " + concert.getReservedSeat() + " Available seats: " + (concert.getSeatCapacity() - concert.getReservedSeat()));
            } else {
                System.out.println("Reservation successful. Reserved seats: " + selectedEvent.getReservedSeat() + ". Available seats: " + (selectedEvent.getSeatCapacity() - selectedEvent.getReservedSeat()));
            }

        } catch (NumberFormatException e) {
            //gestisce l'input non numerico
            System.out.println("Please enter a valid number.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            //gestisce errori specifi alla prenotazione
            System.out.println(e.getMessage());
        }
    }

    //metodo per cancellare una prenotazione per evento specifico
    private static void cancelReservation(EventManager eventManager, Scanner scanner) {
        Event selectedEvent = selectEvent(eventManager, scanner);
        //termina metodo se nessun evento è selezionato
        if (selectedEvent == null){
            System.out.println("Cancellation process terminated");
            return;
        }
        System.out.println("enter the number of seats to cancel: ");

        int reservedSeatsToCancel = getIntInput(scanner);
        try {
            //controlla che il numero di posti da cancellare sia positivo
            if (reservedSeatsToCancel <= 0) {
                throw new IllegalArgumentException("number of seats to cancel must be positive");
            }
            //controlla che le cancellazioni non siano superiori ai posti prenotati
            if (reservedSeatsToCancel > selectedEvent.getReservedSeat()) {
                throw new IllegalArgumentException("cannot cancel more seats than are currently reserved");
            }
            selectedEvent.cancelReservation(reservedSeatsToCancel);
            if (selectedEvent instanceof Concert) {
                Concert concert = (Concert) selectedEvent;
                BigDecimal refundAmount = concert.calculatePrice(reservedSeatsToCancel);
                System.out.println("cancellation succesful. " + reservedSeatsToCancel + " seats cancelled, refunded amount :" + refundAmount + " available seats: "  + (concert.getSeatCapacity() - concert.getReservedSeat()));

            } else {
                System.out.println("cancellation succesful. " + reservedSeatsToCancel + " seats cancelled. Now " + selectedEvent.getReservedSeat() + " seats are reserved. Available: " + (selectedEvent.getSeatCapacity() - selectedEvent.getReservedSeat()));
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                return input;
            } catch (NumberFormatException e) {
                System.out.println("Invalid. Please enter a valid number");
            }
        }
    }

    //mostra gli eventi
    private static void showEvents(EventManager eventManager) {
        if (eventManager.getEvents().isEmpty()) {
            System.out.println("No events available.");
        } else {
            System.out.println("All events:");
            // usa il toString di EventManager per avere una lista ordinata per data di eventi
            System.out.println(eventManager.toString());
        }
    }


}
