package com.aisw.community.model.network.request.admin;


import com.aisw.community.model.enumclass.InformationCategory;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SiteInformationApiRequest {

    private Long id;

    private String name;

    private String content;

    private Boolean publishStatus;

    private String linkUrl;

    private InformationCategory category;
}