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

    @OneToMany (fetch = FetchType.LAZY, targetEntity = File.class)
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
}
