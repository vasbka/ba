package bavision.entity;

import java.util.List;
import java.util.Objects;

public class Client {
    private int id;
    private List<TimeSlot> timeSlots;
    private String firstName;
    private String secondName;
    private String lastName;
    private String phoneNumber;

    public Client() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id &&
                Objects.equals(timeSlots, client.timeSlots) &&
                Objects.equals(firstName, client.firstName) &&
                Objects.equals(secondName, client.secondName) &&
                Objects.equals(lastName, client.lastName) &&
                Objects.equals(phoneNumber, client.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeSlots, firstName, secondName, lastName, phoneNumber);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", timeSlots=" + timeSlots +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
