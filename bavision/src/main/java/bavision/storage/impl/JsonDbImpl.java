package bavision.storage.impl;

import bavision.entity.Service;
import bavision.entity.Specialist;
import bavision.entity.TimeSlot;
import bavision.storage.Storage;
import io.jsondb.JsonDBTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonDbImpl implements Storage {
    private JsonDBTemplate jsonDBTemplate;
    private Map<Class, AtomicInteger> idGenerators;

    public JsonDbImpl() {
        idGenerators = new HashMap<>();
        String dbFilesLocation = "test";
        String baseScanPackage = "bavision.entity";
        jsonDBTemplate = new JsonDBTemplate(dbFilesLocation, baseScanPackage);

        idGenerators.put(Specialist.class, new AtomicInteger(jsonDBTemplate.getCollection(Specialist.class).stream()
                .mapToInt(Specialist::getId).max().orElse(0)));
        idGenerators.put(Service.class, new AtomicInteger(jsonDBTemplate.getCollection(Service.class).stream()
                .mapToInt(Service::getId).max().orElse(0)));
        idGenerators.put(TimeSlot.class, new AtomicInteger(jsonDBTemplate.getCollection(TimeSlot.class).stream()
                .mapToInt(TimeSlot::getId).max().orElse(0)));

    }

    @Override
    public Map<Integer, TimeSlot> getTimeSlots() {
        List<TimeSlot> collection = jsonDBTemplate.getCollection(TimeSlot.class);
        Map<Integer, TimeSlot> map = new HashMap<>();
        for (TimeSlot timeSlot : collection) {
            map.put(timeSlot.getId(), timeSlot);
        }
        return map;
    }

    @Override
    public Map<Integer, Specialist> getSpecialists() {
        List<Specialist> collection = jsonDBTemplate.getCollection(Specialist.class);
        Map<Integer, Specialist> specialistMap = new HashMap<>();
        for (Specialist specialist : collection) {
            specialistMap.put(specialist.getId(), specialist);
        }
        return specialistMap;
    }

    @Override
    public Map<Integer, Service> getServices() {
        List<Service> collection = jsonDBTemplate.getCollection(Service.class);
        Map<Integer, Service> servicesMap = new HashMap<>();
        for (Service specialist : collection) {
            servicesMap.put(specialist.getId(), specialist);
        }
        return servicesMap;
    }

    public Specialist addSpecialist(Specialist specialist) {
        specialist.setId(idGenerators.get(Specialist.class).incrementAndGet());
        jsonDBTemplate.insert(specialist);
        return specialist;
    }

    @Override
    public Service addService(Service service) {
        service.setId(idGenerators.get(Service.class).incrementAndGet());
        jsonDBTemplate.insert(service);
        return service;
    }

}
