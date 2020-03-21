package bavision.entity;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.util.Objects;
import java.util.Set;

@Document(collection = "Specialist", schemaVersion= "1.0")
public class Specialist implements Entity {
    @Id
    private int id;
    private String firstName;
    private String lastName;
    private Set<Service> services;

    public Specialist() {
    }

    public Specialist(int id, String firstName, String lastName, Set<Service> services) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.services = services;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Specialist that = (Specialist) o;
        return id == that.id &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(services, that.services);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, services);
    }

    @Override
    public String toString() {
        return "Specialist{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", services=" + services +
                '}';
    }

    public String worksToBeaty() {
        StringBuilder works = new StringBuilder();
        if (this.services == null) {
            return "У специалиста еще не добавлены виды работ.";
        }
        for (Service service : this.services) {
            works.append(" ").append(service.getName()).append(",");
        }
        return works.toString().substring(0, works.toString().length() - 1) + ".";
    }

    public String toBeatyString() {
        return "Специалист - " + firstName + " " + lastName + System.lineSeparator() + System.lineSeparator() + "Занимается:" + this.worksToBeaty();
    }
}
