package tuannt.appweather.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tuan on 22/02/2016.
 */
public class WeatherVideo {

    @SerializedName("date")
    private String date;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("videoId")
    private String videoId;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
