package com.aisw.community.model.network.request.post.notice;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadToUniversityDTO {

    private UniversityApiRequest universityApiRequest;

    private MultipartFile[] files;
}