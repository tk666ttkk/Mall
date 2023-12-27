package pers.ervinse.shoppingmall.domain;// Comment.java


import java.sql.Timestamp;

import java.io.Serializable;
import java.util.Objects;

public class Comment {
    private int id;
    private int userId;
    private int productId;
    private String commentText,userName;
    private Timestamp timestamp;

    public Comment(int id, int userId, int productId, String commentText,String userName, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.commentText = commentText;
        this.userName = userName;
        this.timestamp = timestamp;
    }

    public Comment(int id, int userId, int productId, String commentText) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.commentText = commentText;
    }

    public Comment( int userId, int productId, String userName,String commentText) {

        this.userId = userId;
        this.productId = productId;
        this.userName =  userName;
        this.commentText = commentText;
    }

    public Comment() {
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getProductId() {
        return productId;
    }

    public String getCommentText() {
        return commentText;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setUserName(String userName) {
       this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", commentText='" + commentText + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
