package com.socket.common.web;

import com.utils.excel.Rownum;
import com.utils.util.Dates;
import com.utils.util.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author 谢长春 2018/12/2
 */
@RequestMapping("/sse")
@Controller
@Slf4j
public class SseController {
    @Autowired
    private ExecutorService multiThread;

    /**
     * 异步推送消息
     * https://www.mxgw.info/t/html5-eventsource.html
     * https://www.cnblogs.com/tmax/p/7738702.html
     * https://www.cnblogs.com/ludashi/p/6520692.html
     * URL:/open/test/{id}
     * 参数：{id}数据ID；
     *
     * @param id 数据ID
     * @return Result<E>
     */
    @GetMapping("/{id}")
    public SseEmitter sse(@PathVariable final String id) {
        final SseEmitter emitter = new SseEmitter(0L); //timeout设置为0表示不超时
        emitter.onError(e -> log.error(e.getMessage(), e));
        emitter.onTimeout(() -> log.error("连接超时"));
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("message")
                    .data(Maps.ofSO()
                            .put("id", id)
                            .put("comment", "服务端推送开启")
                            .buildJSONObject(), MediaType.APPLICATION_JSON_UTF8
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            emitter.completeWithError(e);
        }
        multiThread.execute(() -> repet(id, emitter, new Timer(), Rownum.ofOne()));
        return emitter;
    }

    private void repet(final String id, final SseEmitter emitter, final Timer timer, final Rownum num) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    emitter.send(SseEmitter.event()
                            .id(id)
                            .name("push")
                            .data(Maps.ofSO()
                                    .put("id", id)
                                    .put("number", num.get())
                                    .put("timestamp", Dates.now().formatDateTime())
                                    .buildJSONObject(), MediaType.APPLICATION_JSON_UTF8
                            )
                    );
                    log.info(String.format("第%d次:%s", num.get(), id));
                    if (num.next().get() > 10) {
                        emitter.send(SseEmitter.event().id(id).name("end").data(Maps.ofSO().put("id", id).put("comment", "服务端推送关闭").buildJSONObject(), MediaType.APPLICATION_JSON_UTF8));
                        emitter.complete();
                        timer.cancel();
                    } else {
                        repet(id, emitter, timer, num);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    emitter.completeWithError(e);
                }
            }
        }, 1500 * 10);
    }


}
