package bavision.entity;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

public class OrderInfo {
    private String specialistId;
    private Set<String> servicesId;
    private String date;
    private String time;

    public OrderInfo() {
    }

    public String getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(String specialistId) {
        this.specialistId = specialistId;
    }

    public Set<String> getServicesId() {
        return servicesId;
    }

    public void setServicesId(Set<String> servicesId) {
        this.servicesId = servicesId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderInfo orderInfo = (OrderInfo) o;
        return Objects.equals(specialistId, orderInfo.specialistId) &&
                Objects.equals(servicesId, orderInfo.servicesId) &&
                Objects.equals(date, orderInfo.date) &&
                Objects.equals(time, orderInfo.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specialistId, servicesId, date, time);
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "specialistId='" + specialistId + '\'' +
                ", servicesId=" + servicesId +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
