package com.arthasboothelper.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangyunfan@fiture.com
 * @version 1.0
 * @ClassName: ArthasResponseBodyDTO
 * @date 2022/4/21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArthasResponseBodyDTO {

    private String jobStatus;

    private Integer jobId;

    private String command;

    private String results;
}
