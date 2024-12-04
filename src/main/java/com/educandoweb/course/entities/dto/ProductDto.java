package com.educandoweb.course.entities.dto;

import com.educandoweb.course.entities.Product;

public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imgUrl;

    private Integer qunatityInStock;

    public ProductDto(){}

    public ProductDto(Long id, String name, String description, Double price, String imgUrl, Integer qunatityInStock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.qunatityInStock = qunatityInStock;
    }

    public ProductDto(Product product) {
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getQunatityInStock() {
        return qunatityInStock;
    }

    public void setQunatityInStock(Integer qunatityInStock) {
        this.qunatityInStock = qunatityInStock;
    }
}
