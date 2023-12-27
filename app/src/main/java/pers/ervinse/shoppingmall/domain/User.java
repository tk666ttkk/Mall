package pers.ervinse.shoppingmall.domain;


public class User {

    private String name, password, description;
    private int id;
    private static User instance;

    public User() {
    }
    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
    // 添加设置用户信息的方法
    public void setUser(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    public User(String name, String password, String description) {
        this.name = name;
        this.password = password;
        this.description = description;
    }


    public User(int id,String name, String password, String description) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.description = description;
    }



    public int getId() {
        return id;
    }

    public void setId(int  id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
