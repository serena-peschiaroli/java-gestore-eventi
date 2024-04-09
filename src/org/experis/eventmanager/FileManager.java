package org.experis.eventmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManager {

    //il percorso del file dove gli eventi sono salvati
    private final String filePath;

    //costrutture che inizializza FileManager con un file path
    public FileManager(String filePath) {
        this.filePath = filePath;
    }
    //metodo che scrive la lista degli eventi sul file, ritornado true
    public boolean writeEvents(List<Event> events) {
        //try-with-resources che chiude autmaticamente il fileWriter
        try (FileWriter writer = new FileWriter(filePath, false)) {
            for (Event event : events) {
                //converte ogni evento in una stringa
                String line = formatLine(event);
                writer.write(line + "\n");
            }
            return true;
        } catch (IOException e) {
            System.out.println("Unable to write to file: " + e.getMessage());
            return false;
        }
    }
    //legge gli eventi dal file e li ritorna come una collection List
    public List<Event> readEvents() {
        List<Event> events = new ArrayList<>();
        File eventFile = new File(filePath);

        try (Scanner scanner = new Scanner(eventFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                //divide la linea in parti
                String[] parts = line.split(";");

                //converte le parti in un Event obj
                Event event = parseLine(parts);
                if (event != null) {
                    //aggiunge l'evento alla lista
                    events.add(event);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unable to read file: " + e.getMessage());
        }

        return events;
    }

    private Event parseLine(String[] parts) {
        //estrae attributi comuni a tutti gli eventi
        String type = parts[0];
        String title = parts[1];
        LocalDate date = LocalDate.parse(parts[2]);
        int seatCapacity = Integer.parseInt(parts[3]);
        int reservedSeat = Integer.parseInt(parts[4]);
        //determina il tipo di evento e ritorna l'oggetto appropriato
        if ("Event".equals(type)) {
            return new Event(title, date, seatCapacity, reservedSeat);
        } else if ("Concert".equals(type)) {
            LocalTime time = LocalTime.parse(parts[5]);
            BigDecimal price = new BigDecimal(parts[6]);
            return new Concert(title, date, seatCapacity, reservedSeat, time, price);
        }

        return null;
    }

    //formatta un oggetto Evento in una stringa per scriverlo nel file
    private String formatLine(Event event){
        //inizia con il typo di evento
        String line = event instanceof Concert ? "Concert;" : "Event;";
        //aggiunge gli attributi comuni
        line += event.getTitle() + ";" + event.getDate() + ";" + event.getSeatCapacity() + ";" + event.getReservedSeat();
       //aggiunge gli attributi specifici del concerto
        if (event instanceof Concert) {
            Concert concert = (Concert) event;
            line += ";" + concert.getTime() + ";" + concert.getPrice();
        }
        return line;
    }
}
