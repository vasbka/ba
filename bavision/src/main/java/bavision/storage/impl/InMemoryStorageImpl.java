package bavision.storage.impl;

import bavision.entity.Service;
import bavision.entity.Specialist;
import bavision.entity.TimeSlot;
import bavision.storage.Storage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.IntConsumer;

public class InMemoryStorageImpl implements Storage {
    private Map<Integer, TimeSlot> timeSlots;
    private Map<Integer, Specialist> specialists;
    private Map<Integer, Service> services;

    public InMemoryStorageImpl() {
        timeSlots = new HashMap<>();
        specialists = new HashMap<>();
        services = new HashMap<>();

        LocalDateTime date = LocalDateTime.of(LocalDate.of(2020, 2, 25), LocalTime.of(16, 00));
        services.put(0, new Service(0, "Depeliation", "Some description what is it."));
        services.put(1, new Service(1, "some other job", "Some description what is it."));
        services.put(2, new Service(2, "some other job", "Some description what is it."));

        Specialist specialist = new Specialist(0, "Maria", "Denisenko", new HashSet<>(services.values()));
        Specialist specialist1 = new Specialist(1, "T1", "T1F", new HashSet<>(services.values()));
        Specialist specialist2 = new Specialist(2, "T2", "T2F", new HashSet<>(services.values()));

        specialists.put(specialist.getId(), specialist);
        specialists.put(specialist1.getId(), specialist1);
        specialists.put(specialist2.getId(), specialist2);

        timeSlots.put(0, new TimeSlot(0, LocalDateTime.of(LocalDate.of(2020, 2, 25), LocalTime.of(16, 00)), specialist, services.get(0), null));
    }

    @Override
    public Map<Integer, TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    @Override
    public Map<Integer, Specialist> getSpecialists() {
        return this.specialists;
    }

    @Override
    public Map<Integer, Service> getServices() {
        return this.services;
    }

    @Override
    public Service addService(Service service) {
        OptionalInt max = this.getServices().keySet().stream().mapToInt(value -> value).max();
        int id = -1;
        try {
            id = max.getAsInt();
        } catch (NoSuchElementException e ){ }
        if (id == -1) {
            id = 0;
        } else {
            id++;
        }
        service.setId(id);
        return this.services.put(id, service);
    }

    @Override
    public Specialist addSpecialist(Specialist specialist) {
        return null;
    }

}
