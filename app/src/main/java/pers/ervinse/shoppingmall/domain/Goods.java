package pers.ervinse.shoppingmall.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * 商品实体类
 */
public class Goods implements Serializable {

    private String name, description,location,image,type,username;
    private int number,id;
    private double price;
    public Boolean isSelected = false;

    public Goods() {
    }

    public Goods(int id,String name, String description, String location, String image, int number, double price, Boolean isSelected,String type,String username) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.image = image;
        this.number = number;
        this.price = price;
        this.isSelected = isSelected;
        this.type =type;
        this.username = username;
    }

    public Goods(String name, String description,double price,String image) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
    }

    public String getuserName() {
            return username;
        }
        public void setuserName(String username) {
            this.username = username;
        }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return "Goods{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", image='" + image + '\'' +
                ", number=" + number +
                ", type=" + type +
                ", price=" + price +
                ", isSelected=" + isSelected +
                '}';
    }
}
