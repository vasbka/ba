package bavision.storage;

import bavision.entity.Service;
import bavision.entity.Specialist;
import bavision.entity.TimeSlot;

import java.util.Map;

public interface Storage {
    Map<Integer, TimeSlot> getTimeSlots();

    Map<Integer, Specialist> getSpecialists();

    Map<Integer, Service> getServices();

    Service addService(Service service);

    Specialist addSpecialist(Specialist specialist);
}