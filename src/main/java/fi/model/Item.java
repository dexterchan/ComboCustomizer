package fi.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "item")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "itmid")
    private int id;

    @Column(name = "itmname", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="catid", nullable=false)
    private Category category;

    // Item dependency mapping where a depender depends on a dependee
    @ManyToMany
    @JoinTable(name = "item_dependency", joinColumns = {
            @JoinColumn(name = "depender", referencedColumnName = "itmid", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "dependee", referencedColumnName = "itmid", nullable = false)})
    private List<Item> itemDependencies;

    public Item() {}

    public Item(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Item> getItemDependencies() {
        return itemDependencies;
    }

    public void setItemDependencies(List<Item> itemDependencies) {
        this.itemDependencies = itemDependencies;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}