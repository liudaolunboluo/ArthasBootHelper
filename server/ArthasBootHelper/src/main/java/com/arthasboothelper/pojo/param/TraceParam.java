package com.arthasboothelper.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhangyunfan@fiture.com
 * @version 1.0
 * @ClassName: TraceParam
 * @Description: trace命令参数
 * @date 2022/4/21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraceParam  {

    private String methodAddress;
}
