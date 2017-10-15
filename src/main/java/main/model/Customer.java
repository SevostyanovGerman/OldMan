package main.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="customers")
public class Customer {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "sec_name")
    private String secName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @OneToMany (fetch = FetchType.LAZY, targetEntity = Delivery.class)
    @JoinTable(name = "keys_customer_delivery",
            joinColumns = {@JoinColumn(name = "customer_id")},
            inverseJoinColumns = {@JoinColumn(name = "delivery_id")})
    private List<Delivery> deliveries;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return firstName;
    }

    public void setFirst_name(String first_name) {
        this.firstName = first_name;
    }

    public String getSec_name() {
        return secName;
    }

    public void setSec_name(String sec_name) {
        this.secName = sec_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return firstName+" "+secName;
    }

    public List <Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List <Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (id != null ? !id.equals(customer.id) : customer.id != null) return false;
        if (firstName != null ? !firstName.equals(customer.firstName) : customer.firstName != null) return false;
        if (secName != null ? !secName.equals(customer.secName) : customer.secName != null) return false;
        if (email != null ? !email.equals(customer.email) : customer.email != null) return false;
        if (phone != null ? !phone.equals(customer.phone) : customer.phone != null) return false;
        return deliveries != null ? deliveries.equals(customer.deliveries) : customer.deliveries == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (secName != null ? secName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (deliveries != null ? deliveries.hashCode() : 0);
        return result;
    }
}
