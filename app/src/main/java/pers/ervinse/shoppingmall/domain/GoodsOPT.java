package pers.ervinse.shoppingmall.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * 商品实体类
 */
public class GoodsOPT implements Serializable {

    private String name, description,location,image,type,username;
    private double price;

    public GoodsOPT() {
    }

    public GoodsOPT(String name, String description, String location, String image,double price,String type,String username) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.image = image;
        this.price = price;
        this.type = type;
        this.username = username;
    }

    public String getuserName() {
        return username;
    }

    public void setuserName(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", image='" + image + '\''   +
                ", price=" +  price +  '\''   +
                ", type=" +   type +
                '}';
    }
}
