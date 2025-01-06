package edu.hsog.db.DTOs;

public class GadgetDTO {
    String url;
    String email;
    String keywords;
    String description;
    byte[] image;

    public GadgetDTO(String url, String email, String keywords, String description, byte[] image) {
        this.url = url;
        this.email = email;
        this.keywords = keywords;
        this.description = description;
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


}
