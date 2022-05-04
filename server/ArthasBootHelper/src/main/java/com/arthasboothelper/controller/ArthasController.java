package com.arthasboothelper.controller;

import com.arthasboothelper.executor.ArthasExecutor;
import com.arthasboothelper.pojo.dto.ArthasResponseDTO;
import com.arthasboothelper.pojo.param.TraceParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangyunfan@fiture.com
 * @version 1.0
 * @ClassName: ArthasController
 * @date 2022/4/21
 */
@RestController
@RequestMapping("/arthas")
public class ArthasController {

    @Value("${arthas.httpPort:-1}")
    private Integer httpPort;

    @Autowired
    private ArthasExecutor arthasExecutor;

    @GetMapping("/version")
    public ResponseEntity<ArthasResponseDTO> getVersion() {
        if (httpPort.equals(-1)) {
            return ResponseEntity.ok(ArthasResponseDTO.builder().message("未启动arthas").build());
        }
        ArthasResponseDTO arthasResponseDTO = arthasExecutor.execCommand("version", 5000);
        assert arthasResponseDTO != null;
        return ResponseEntity.ok(arthasResponseDTO);
    }

    @PostMapping("/sync/trace")
    public ResponseEntity<ArthasResponseDTO> syncTrace(@RequestBody TraceParam traceParam) {
        if (httpPort.equals(-1)) {
            return ResponseEntity.ok(ArthasResponseDTO.builder().message("未启动arthas").build());
        }
        ArthasResponseDTO arthasResponseDTO = arthasExecutor.execCommand("trace " + traceParam.getMethodAddress(), 60000);
        assert arthasResponseDTO != null;
        return ResponseEntity.ok(arthasResponseDTO);
    }

    @PostMapping("/async/trace")
    public ResponseEntity<ArthasResponseDTO> asyncTrace(@RequestBody TraceParam traceParam) {
        if (httpPort.equals(-1)) {
            return ResponseEntity.ok(ArthasResponseDTO.builder().message("未启动arthas").build());
        }
        ArthasResponseDTO arthasResponseDTO = arthasExecutor.traceCommandAsync(traceParam.getMethodAddress());
        assert arthasResponseDTO != null;
        return ResponseEntity.ok(arthasResponseDTO);
    }

    @GetMapping("/pull/{sessionId}/{consumerId}")
    public ResponseEntity<ArthasResponseDTO> asyncTrace(@PathVariable("sessionId") String sessionId, @PathVariable("consumerId") String consumerId) {
        return ResponseEntity.ok(arthasExecutor.pullRequests(sessionId, consumerId));
    }

    @GetMapping("/interrupt/stop/{sessionId}")
    public ResponseEntity<Boolean> interruptAndStopSession(@PathVariable("sessionId") String sessionId) {
        return ResponseEntity.ok(arthasExecutor.interruptAndStopSession(sessionId));
    }
}
