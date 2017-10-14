package main.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="items")
public class Item {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "model")
    private String model;

    @Column(name = "type")
    private String type;

    @Column(name = "comment")
    private String comment;

    @Column(name = "count")
    private int count;

    @Column(name = "price")
    private double price;

    @OneToMany (fetch = FetchType.EAGER, targetEntity = File.class)
    @JoinTable(name = "keys_item_file",
            joinColumns = {@JoinColumn(name = "item_id")},
            inverseJoinColumns = {@JoinColumn(name = "file_id")})
    private List<File> files;

    @OneToMany (fetch = FetchType.LAZY, targetEntity = Image.class)
    @JoinTable(name = "keys_item_image",
            joinColumns = {@JoinColumn(name = "item_id")},
            inverseJoinColumns = {@JoinColumn(name = "image_id")})
    private List<Image> images;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List <File> getFiles() {
        return files;
    }

    public void setFiles(List <File> files) {
        this.files = files;
    }

    public List <Image> getImages() {
        return images;
    }

    public void setImages(List <Image> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (count != item.count) return false;
        if (Double.compare(item.price, price) != 0) return false;
        if (id != null ? !id.equals(item.id) : item.id != null) return false;
        if (name != null ? !name.equals(item.name) : item.name != null) return false;
        if (model != null ? !model.equals(item.model) : item.model != null) return false;
        if (type != null ? !type.equals(item.type) : item.type != null) return false;
        if (comment != null ? !comment.equals(item.comment) : item.comment != null) return false;
        if (files != null ? !files.equals(item.files) : item.files != null) return false;
        return images != null ? images.equals(item.images) : item.images == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + count;
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (files != null ? files.hashCode() : 0);
        result = 31 * result + (images != null ? images.hashCode() : 0);
        return result;
    }
}