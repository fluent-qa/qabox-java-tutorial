package io.fluentqa.seeds.dto;

import lombok.Getter;
import lombok.Setter;

/**
 */
@Getter
@Setter
public class PreviewFeedsInfo {
    private String title;

    private String description;

    private String siteLink;

    private String feedUrl;

    private String siteFaviconUrl;

    private Boolean subscribed;
}
