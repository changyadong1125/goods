package com.goods.common.vo.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class MusicVO {
    @JsonProperty(value = "title")
    private String name;
    @JsonProperty(value = "src")
    private String url;
    private String artist;
    @JsonProperty(value = "pic")
    private String cover;
    private String lrc;
}
