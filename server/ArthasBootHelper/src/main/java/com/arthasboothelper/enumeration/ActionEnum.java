package com.arthasboothelper.enumeration;

import lombok.Getter;

/**
 * @author zhangyunfan@fiture.com
 * @version 1.0
 * @ClassName: ActionEnum
 * @Description: 请求Action枚举
 * @date 2022/4/21
 */
@Getter
public enum ActionEnum {

    /**
     * 同步执行命令，命令正常结束或者超时后中断命令执行后返回命令的执行结果
     */
    exec,

    /**
     * 异步执行命令，立即返回命令的调度结果，命令执行结果通过pull_results获取。
     */
    async_exec,

    /**
     * 中断会话当前的命令，类似Telnet Ctrl + c的功能。
     */
    interrupt_job,

    /**
     * 获取异步执行的命令的结果，以http 长轮询（long-polling）方式重复执行
     */
    pull_results,

    /**
     * 创建会话
     */
    init_session,

    /**
     * 加入会话，用于支持多人共享同一个Arthas会话
     */
    join_session,

    /**
     * 关闭会话
     */
    close_session;
}
