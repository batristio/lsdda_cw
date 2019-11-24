package cw

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId

/**
 * Created by chris on 22/11/19.
 */

class Program {
    private ObjectId _id
    private String pid
    private Integer start_time
    private Integer end_time
    private Date epoch_start
    private Date epoch_end
    private String complete_title
    private String media_type
    private String masterbrand
    private String service
    private String brand_pid
    private Integer is_clip
    private String categories
    private String tags

    @JsonCreator
    @BsonCreator
    Program (@JsonProperty("_id")
             @BsonProperty("_id") ObjectId _id,
             @JsonProperty("pid")
             @BsonProperty("pid") String pid,
             @JsonProperty("start_time")
             @BsonProperty("start_time") Integer start_time,
             @JsonProperty("end_time")
             @BsonProperty("end_time") Integer end_time,
             @JsonProperty("epoch_start")
             @BsonProperty("epoch_start") Date epoch_start,
             @JsonProperty("epoch_end")
             @BsonProperty("epoch_end") Date epoch_end,
             @JsonProperty("complete_title")
             @BsonProperty("complete_title") String complete_title,
             @JsonProperty("media_type")
             @BsonProperty("media_type") String media_type,
             @JsonProperty("masterbrand")
             @BsonProperty("masterbrand") String masterbrand,
             @JsonProperty("service")
             @BsonProperty("service") String service,
             @JsonProperty("brand_pid")
             @BsonProperty("brand_pid") String brand_pid,
             @JsonProperty("is_clip")
             @BsonProperty("is_clip") Integer is_clip,
             @JsonProperty("categories")
             @BsonProperty("categories") String categories,
             @JsonProperty("tags")
             @BsonProperty("tags") String tags ) {
        this._id = _id
        this.pid = pid
        this.start_time = start_time
        this.end_time = end_time
        this.epoch_start = epoch_start
        this.epoch_end = epoch_end
        this.complete_title = complete_title
        this.media_type = media_type
        this.masterbrand = masterbrand
        this.service = service
        this.brand_pid = brand_pid
        this.is_clip = is_clip
        this.categories = categories
        this.tags = tags
    }

    String get_id(){
        return _id
    }

    String getPid() {
        return pid
    }

    String getStartTime() {
        return start_time
    }

    String getEndTime() {
        return end_time
    }

    String getEpochStart() {
        return epoch_start
    }

    String getEpochEnd() {
        return epoch_end
    }

    String getCompleteTitle() {
        return complete_title
    }

    String getMediaType() {
        return media_type
    }

    String getMasterbrand() {
        return masterbrand
    }

    String getService() {
        return service
    }

    String getBrandPid() {
        return brand_pid
    }

    String getIsClip() {
        return is_clip
    }

    String getCategories () {
        return categories
    }

    String getTags () {
        return tags
    }

    void setCompleteTitle(String complete_title) {
        this.complete_title = complete_title
    }

    //ToDo: if you want to insert anything - you need to provide setters for each object property
}
