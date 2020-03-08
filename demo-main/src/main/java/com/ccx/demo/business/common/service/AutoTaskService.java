package com.ccx.demo.business.common.service;

import com.ccx.demo.config.init.AppConfig;
import com.utils.util.FPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 定时任务服务类
 *
 * @author 谢长春 on 2020-03-08
 */
@Service
public class AutoTaskService {
    /**
     * 清除临时文件目录
     */
    @Transactional(propagation = Propagation.NEVER)
    public void clearTempDirectory() {
        FPath.of(AppConfig.Path.TEMP.file()).deleteAll();
//        FPath.of(System.getProperty("java.io.tmpdir")).deleteAll();
    }
}
