package main.model;

import groovy.time.BaseDuration;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="orders")
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "price")
    private double price;

    @Column(name = "payment")
    private String payment;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "status")
    private String status;

    @Column(name = "deleted")
    private int deleted;

    @Column(name = "creator")
    private String creator;

    @Column(name = "created")
    private Date created;

    @Column(name = "delivery_type")
    private String deliveryType;

    @Column(name = "date_recieved")
    private Date dateRecieved;

    @Column(name = "date_transferred")
    private Date dateTransferred;

    @Column(name = "from")
    private String from;

    @Column(name = "to")
    private String to;

    @OneToMany (fetch = FetchType.EAGER, targetEntity = Comment.class)
    @JoinTable(name = "keys_order_comment",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "comment_id")})
    private List<Comment> comments;

    @OneToMany (fetch = FetchType.LAZY, targetEntity = Item.class)
    @JoinTable(name = "keys_order_item",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "item_id")})
    private List<Item> items;

    @ManyToOne (fetch = FetchType.EAGER, targetEntity = Customer.class)
    @JoinTable(name = "keys_order_customer",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "customer_id")})
    private Customer customer;

    @OneToMany (fetch = FetchType.LAZY, targetEntity = History.class)
    @JoinTable(name = "keys_order_history",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "history_id")})
    private List<History> histories;

    @ManyToOne (fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinTable(name = "keys_order_manager",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "manager_id")})
    private User manager;

    @ManyToOne (fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinTable(name = "keys_order_master",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "master_id")})
    private User master;

    @ManyToOne (fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinTable(name = "keys_order_designer",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "designer_id")})
    private User designer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public List <Comment> getComments() {
        return comments;
    }

    public void setComments(List <Comment> comments) {
        this.comments = comments;
    }

    public List <Item> getItems() {
        return items;
    }

    public void setItems(List <Item> items) {
        this.items = items;
    }

    public User getDesigner() {
        return designer;
    }

    public void setDesigner(User designer) {
        this.designer = designer;
    }

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public Date getDateRecieved() {
        return dateRecieved;
    }

    public void setDateRecieved(Date dateRecieved) {
        this.dateRecieved = dateRecieved;
    }

    public Date getDateTransferred() {
        return dateTransferred;
    }

    public void setDateTransferred(Date dateTransferred) {
        this.dateTransferred = dateTransferred;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCreated() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return    dateFormat.format(created);
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List <History> getHistories() {
        return histories;
    }

    public void setHistories(List <History> histories) {
        this.histories = histories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (Double.compare(order.price, price) != 0) return false;
        if (deleted != order.deleted) return false;
        if (id != null ? !id.equals(order.id) : order.id != null) return false;
        if (number != null ? !number.equals(order.number) : order.number != null) return false;
        if (payment != null ? !payment.equals(order.payment) : order.payment != null) return false;
        if (paymentType != null ? !paymentType.equals(order.paymentType) : order.paymentType != null) return false;
        if (status != null ? !status.equals(order.status) : order.status != null) return false;
        if (creator != null ? !creator.equals(order.creator) : order.creator != null) return false;
        if (created != null ? !created.equals(order.created) : order.created != null) return false;
        if (deliveryType != null ? !deliveryType.equals(order.deliveryType) : order.deliveryType != null) return false;
        if (dateRecieved != null ? !dateRecieved.equals(order.dateRecieved) : order.dateRecieved != null) return false;
        if (dateTransferred != null ? !dateTransferred.equals(order.dateTransferred) : order.dateTransferred != null)
            return false;
        if (from != null ? !from.equals(order.from) : order.from != null) return false;
        if (to != null ? !to.equals(order.to) : order.to != null) return false;
        if (comments != null ? !comments.equals(order.comments) : order.comments != null) return false;
        if (items != null ? !items.equals(order.items) : order.items != null) return false;
        if (customer != null ? !customer.equals(order.customer) : order.customer != null) return false;
        if (histories != null ? !histories.equals(order.histories) : order.histories != null) return false;
        if (manager != null ? !manager.equals(order.manager) : order.manager != null) return false;
        if (master != null ? !master.equals(order.master) : order.master != null) return false;
        return designer != null ? designer.equals(order.designer) : order.designer == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (payment != null ? payment.hashCode() : 0);
        result = 31 * result + (paymentType != null ? paymentType.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + deleted;
        result = 31 * result + (creator != null ? creator.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (deliveryType != null ? deliveryType.hashCode() : 0);
        result = 31 * result + (dateRecieved != null ? dateRecieved.hashCode() : 0);
        result = 31 * result + (dateTransferred != null ? dateTransferred.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (histories != null ? histories.hashCode() : 0);
        result = 31 * result + (manager != null ? manager.hashCode() : 0);
        result = 31 * result + (master != null ? master.hashCode() : 0);
        result = 31 * result + (designer != null ? designer.hashCode() : 0);
        return result;
    }
}
