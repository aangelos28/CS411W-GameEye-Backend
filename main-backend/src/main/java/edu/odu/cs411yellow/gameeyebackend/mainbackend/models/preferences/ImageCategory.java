package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import org.springframework.data.annotation.PersistenceConstructor;

import java.util.ArrayList;
import java.util.List;

public class ImageCategory {
    private Integer count;

    private List<String> imageIds;

    @PersistenceConstructor
    public ImageCategory(Integer count, List<String> imageIds) {
        this.count = count;
        this.imageIds = imageIds;
    }

    public ImageCategory() {
        this.count = 0;
        this.imageIds = new ArrayList<>();
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<String> getImageIds() {
        return this.imageIds;
    }

    public void setImageIds(List<String> imageIds) {
        this.imageIds = imageIds;
    }
}
