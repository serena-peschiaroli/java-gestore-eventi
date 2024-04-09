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

    private final String filePath;

    public FileManager(String filePath) {
        this.filePath = filePath;
    }

    public boolean writeEvents(List<Event> events) {
        try (FileWriter writer = new FileWriter(filePath, false)) {
            for (Event event : events) {
                String line = formatLine(event);
                writer.write(line + "\n");
            }
            return true;
        } catch (IOException e) {
            System.out.println("Unable to write to file: " + e.getMessage());
            return false;
        }
    }
    public List<Event> readEvents() {
        List<Event> events = new ArrayList<>();
        File eventFile = new File(filePath);

        try (Scanner scanner = new Scanner(eventFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");


                Event event = parseLine(parts);
                if (event != null) {
                    events.add(event);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unable to read file: " + e.getMessage());
        }

        return events;
    }

    private Event parseLine(String[] parts) {
        String type = parts[0];
        String title = parts[1];
        LocalDate date = LocalDate.parse(parts[2]);
        int seatCapacity = Integer.parseInt(parts[3]);
        int reservedSeat = Integer.parseInt(parts[4]);

        if ("Event".equals(type)) {
            return new Event(title, date, seatCapacity, reservedSeat);
        } else if ("Concert".equals(type)) {
            LocalTime time = LocalTime.parse(parts[5]);
            BigDecimal price = new BigDecimal(parts[6]);
            return new Concert(title, date, seatCapacity, reservedSeat, time, price);
        }

        return null;
    }

    private String formatLine(Event event){
        String line = event instanceof Concert ? "Concert;" : "Event;";
        line += event.getTitle() + ";" + event.getDate() + ";" + event.getSeatCapacity() + ";" + event.getReservedSeat();
        if (event instanceof Concert) {
            Concert concert = (Concert) event;
            line += ";" + concert.getTime() + ";" + concert.getPrice();
        }
        return line;
    }
}
