package bavision.entity;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "TimeSlot", schemaVersion= "1.0")
public class TimeSlot {
    @Id
    private int id;
    private LocalDateTime date;
    private Specialist specialist;
    private Service service;
    private Client client;

    public TimeSlot(int id, LocalDateTime date, Specialist specialist, Service service, Client client) {
        this.id = id;
        this.date = date;
        this.specialist = specialist;
        this.service = service;
        this.client = client;
    }

    public TimeSlot() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Specialist getSpecialist() {
        return specialist;
    }

    public void setSpecialist(Specialist specialist) {
        this.specialist = specialist;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return id == timeSlot.id &&
                Objects.equals(date, timeSlot.date) &&
                Objects.equals(specialist, timeSlot.specialist) &&
                Objects.equals(service, timeSlot.service) &&
                Objects.equals(client, timeSlot.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, specialist, service, client);
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "id=" + id +
                ", date=" + date +
                ", specialist=" + specialist +
                ", service=" + service +
                ", client=" + client +
                '}';
    }

}
