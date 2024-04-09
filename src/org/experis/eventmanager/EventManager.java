package org.experis.eventmanager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EventManager {
    private String title;
    private List<Event> events;

    public EventManager(String title) {
        this.title = title;
        this.events = new ArrayList<>();
    }

    public List<Event> getEvents() {
        return events;
    }

    //aggiunge un evento alla lista

    public void addEvent(Event event){
        events.add(event);
    }
    //restituisce una lista di eventi in una certa data e li inserisce in una lista:
    public List<Event> eventsOnDate(LocalDate date){
        return events.stream().filter(e -> e.getDate().isEqual(date)).collect(Collectors.toList());
    }

    //restituisce il numero di eventi in programma
    public int eventsNumber(){
        return events.size();
    }

    //svuota la lista
    public void eventsClear(){
        events.clear();
    }

    //override di tostring
    @Override
    public String toString() {

        //ordina gli eventi basandosi sulla data e mappa ogni evento in una stringa

        return title + "\n" + events.stream().sorted(Comparator.comparing(Event::getDate)).map(Event::toString).collect(Collectors.joining("\n"));
    }
}
