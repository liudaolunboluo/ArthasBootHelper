package com.arthasboothelper.executor;

import com.alibaba.fastjson.JSON;
import com.arthasboothelper.pojo.dto.ArthasResponseDTO;
import com.arthasboothelper.enumeration.ActionEnum;
import com.arthasboothelper.pojo.param.ArthasRequestParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhangyunfan@fiture.com
 * @version 1.0
 * @ClassName: ArthasExecutor
 * @Description: arthas执行器
 * @date 2022/4/21
 */
@Service
@Slf4j
public class ArthasExecutor {

    private static final String ARTHAS_NETWORK_ADDRESS = "http://localhost:%s";

    private static final String ARTHAS_BASE_URL = "/api";

    private static final String STATUS_SCHEDULED = "SCHEDULED";

    private static final String STATUS_SUCCEEDED = "SUCCEEDED";

    @Value("${arthas.httpPort:-1}")
    private Integer httpPort;

    @Autowired
    @Qualifier("arthasBootHelperRestTemplate")
    private RestTemplate restTemplate;

    public ArthasResponseDTO execCommand(String command, Integer execTimeout) {
        String arthasResponseDTOJson = restTemplate.postForObject(String.format(ARTHAS_NETWORK_ADDRESS, httpPort) + ARTHAS_BASE_URL,
                ArthasRequestParam.builder().action(ActionEnum.exec.name()).execTimeout(execTimeout).command(command).build(), String.class);
        ArthasResponseDTO arthasResponseDTO = JSON.parseObject(arthasResponseDTOJson, ArthasResponseDTO.class);
        return arthasResponseDTO;
    }

    public ArthasResponseDTO traceCommandAsync(String methodAddress) {
        String arthasResponseDTOJson = restTemplate.postForObject(String.format(ARTHAS_NETWORK_ADDRESS, httpPort) + ARTHAS_BASE_URL,
                ArthasRequestParam.builder().action(ActionEnum.init_session.name()).execTimeout(5000).build(), String.class);
        ArthasResponseDTO arthasResponseDTO = JSON.parseObject(arthasResponseDTOJson, ArthasResponseDTO.class);
        String traceResultStr = restTemplate.postForObject(String.format(ARTHAS_NETWORK_ADDRESS, httpPort) + ARTHAS_BASE_URL,
                ArthasRequestParam.builder().action(ActionEnum.async_exec.name()).command("trace --skipJDKMethod false " + methodAddress)
                        .sessionId(arthasResponseDTO.getSessionId()).build(), String.class);
        ArthasResponseDTO traceResult = JSON.parseObject(traceResultStr, ArthasResponseDTO.class);
        if (STATUS_SCHEDULED.equals(traceResult.getState())) {
            //把JobId吐出去
            arthasResponseDTO.setBody(traceResult.getBody());
        }
        return arthasResponseDTO;
    }

    public ArthasResponseDTO pullRequests(String sessionId, String consumerId) {
        return JSON.parseObject(restTemplate.postForObject(String.format(ARTHAS_NETWORK_ADDRESS, httpPort) + ARTHAS_BASE_URL,
                ArthasRequestParam.builder().action(ActionEnum.pull_results.name()).execTimeout(5000).sessionId(sessionId).consumerId(consumerId)
                        .build(), String.class), ArthasResponseDTO.class);
    }

    public boolean interruptAndStopSession(String sessionId) {
        String sinterruptStr = restTemplate.postForObject(String.format(ARTHAS_NETWORK_ADDRESS, httpPort) + ARTHAS_BASE_URL,
                ArthasRequestParam.builder().action(ActionEnum.interrupt_job.name()).execTimeout(5000).sessionId(sessionId).build(), String.class);
        ArthasResponseDTO arthasResponseDTO = JSON.parseObject(sinterruptStr, ArthasResponseDTO.class);
        if (STATUS_SUCCEEDED.equals(arthasResponseDTO.getState())) {
            String stopStr = restTemplate.postForObject(String.format(ARTHAS_NETWORK_ADDRESS, httpPort) + ARTHAS_BASE_URL,
                    ArthasRequestParam.builder().action(ActionEnum.close_session.name()).execTimeout(5000).sessionId(sessionId).build(),
                    String.class);
            ArthasResponseDTO stop = JSON.parseObject(stopStr, ArthasResponseDTO.class);
            return STATUS_SUCCEEDED.equals(stop.getState());
        }
        log.error("sessionId为{}的任务关闭失败", sessionId);
        return false;

    }
}
