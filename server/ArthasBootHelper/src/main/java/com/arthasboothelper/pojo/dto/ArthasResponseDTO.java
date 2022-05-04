package com.arthasboothelper.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangyunfan@fiture.com
 * @version 1.0
 * @ClassName: ArthasResponseDTO
 * @Description: arthas响应参数
 * @date 2022/4/21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArthasResponseDTO {

    /**
     * 响应体
     */
    private ArthasResponseBodyDTO body;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 消费者ID
     */
    private String consumerId;

    /**
     * 状态
     */
    private String state;
}
