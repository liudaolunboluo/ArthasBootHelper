package com.arthasboothelper.pojo.param;

import com.arthasboothelper.enumeration.ActionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangyunfan@fiture.com
 * @version 1.0
 * @ClassName: ArthasRequestParam
 * @Description: arthas请求参数
 * @date 2022/4/21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArthasRequestParam {

    /**
     * 执行动作
     *
     * @see ActionEnum
     */
    private String action;

    /**
     * 执行命令
     */
    private String command;

    /**
     * 执行超时时间
     */
    private Integer execTimeout;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 消费者ID
     */
    private String consumerId;
}
