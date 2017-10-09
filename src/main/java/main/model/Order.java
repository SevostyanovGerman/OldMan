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
    private Long number;

    @Column(name = "manager")
    private String manager;

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


    @ManyToOne (fetch = FetchType.LAZY, targetEntity = Customer.class)
    @JoinTable(name = "keys_order_customer",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "customer_id")})
    private Customer customer;

    //private List<History> histories;

    @Column(name = "designer")
    private String designer;

    @Column(name = "master")
    private String master;

    @Column(name = "date_recieved")
    private Date dateRecieved;

    @Column(name = "date_transferred")
    private Date dateTransferred;

    @Column(name = "from")
    private String from;

    @Column(name = "to")
    private String to;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
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

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
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


}
