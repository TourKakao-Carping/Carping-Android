package com.tourkakao.carping.newcarping.DataClass;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewCarping {
    @SerializedName("id")
    int id;
    @SerializedName("user")
    int user;
    @SerializedName("latitude")
    double latitude;
    @SerializedName("longitude")
    double longitude;
    @SerializedName("image1")
    String image1;
    @SerializedName("image2")
    String image2;
    @SerializedName("image3")
    String image3;
    @SerializedName("image4")
    String image4;
    @SerializedName("title")
    String title;
    @SerializedName("text")
    String text;
    @SerializedName("views")
    int views;
    @SerializedName("tags")
    ArrayList<String> tags;
    @SerializedName("review_count")
    int review_count;
    @SerializedName("is_bookmarked")
    boolean check_bookmark;
    @SerializedName("my_review_count")
    int my_review_cnt;
    @SerializedName("my_star_avg")
    float my_star_avg;
    @SerializedName("total_star_avg")
    float total_star;
    @SerializedName("star1_avg")
    float star1;
    @SerializedName("star2_avg")
    float star2;
    @SerializedName("star3_avg")
    float star3;
    @SerializedName("star4_avg")
    float star4;
    @SerializedName("review")
    ArrayList<Newcarping_Review> reviews;

    public NewCarping(int id, int user, double latitude, double longitude, String image1, String image2, String image3, String image4, String title, String text, int views, ArrayList<String> tags, int review_count, boolean check_bookmark, int my_review_cnt, float my_star_avg, float total_star, float star1, float star2, float star3, float star4, ArrayList<Newcarping_Review> reviews) {
        this.id = id;
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.title = title;
        this.text = text;
        this.views = views;
        this.tags = tags;
        this.review_count = review_count;
        this.check_bookmark = check_bookmark;
        this.my_review_cnt = my_review_cnt;
        this.my_star_avg = my_star_avg;
        this.total_star = total_star;
        this.star1 = star1;
        this.star2 = star2;
        this.star3 = star3;
        this.star4 = star4;
        this.reviews = reviews;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public boolean isCheck_bookmark() {
        return check_bookmark;
    }

    public void setCheck_bookmark(boolean check_bookmark) {
        this.check_bookmark = check_bookmark;
    }

    public int getMy_review_cnt() {
        return my_review_cnt;
    }

    public void setMy_review_cnt(int my_review_cnt) {
        this.my_review_cnt = my_review_cnt;
    }

    public float getMy_star_avg() {
        return my_star_avg;
    }

    public void setMy_star_avg(float my_star_avg) {
        this.my_star_avg = my_star_avg;
    }

    public float getTotal_star() {
        return total_star;
    }

    public void setTotal_star(float total_star) {
        this.total_star = total_star;
    }

    public float getStar1() {
        return star1;
    }

    public void setStar1(float star1) {
        this.star1 = star1;
    }

    public float getStar2() {
        return star2;
    }

    public void setStar2(float star2) {
        this.star2 = star2;
    }

    public float getStar3() {
        return star3;
    }

    public void setStar3(float star3) {
        this.star3 = star3;
    }

    public float getStar4() {
        return star4;
    }

    public void setStar4(float star4) {
        this.star4 = star4;
    }

    public ArrayList<Newcarping_Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Newcarping_Review> reviews) {
        this.reviews = reviews;
    }
}
