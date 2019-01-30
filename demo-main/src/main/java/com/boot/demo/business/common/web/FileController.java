package com.boot.demo.business.common.web;

import com.boot.demo.business.common.entity.FileInfo;
import com.boot.demo.business.common.entity.FileInfo.Props;
import com.boot.demo.business.common.entity.FileUpload;
import com.boot.demo.config.init.AppConfig.URL;
import com.support.mvc.entity.base.Result;
import com.utils.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

import static com.support.mvc.enums.Code.FILE_EMPTY;

/**
 * 文件处理
 *
 *
 * @author 谢长春 2017年7月12日 下午2:01:20
 */
@Controller
@RequestMapping("/file/{version}")
@Slf4j
public class FileController {

    public Logger getLog() {
        return log;
    }

    @PostMapping("/upload")
    @ResponseBody
    public Result<?> uploadFile(@PathVariable final int version, @RequestParam(required = false) final MultipartFile file) {
        return new Result<FileInfo>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(Props.list())
                        .notes(Arrays.asList(
                                "单文件上传接口, body 中 file=单个文件对象"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl())))
                )
                .execute(result -> {
                    result.versionAssert(version);
                    if (Util.isEmpty(file) || Util.isEmpty(file.getOriginalFilename())) {
                        throw FILE_EMPTY.exception("上传文件为空,字段【file】只能是单个文件");
                    }
                    result.setSuccess(
                            FileUpload.ofTemp()
                                    .files(file)
                                    .upload()
                                    .get()
                    );
                });
    }

    @PostMapping("/uploads")
    @ResponseBody
    public Result<?> uploadFiles(@PathVariable final int version, @RequestParam(required = false) final MultipartFile[] files) {
        return new Result<FileInfo>(1) // 指定接口最新版本号
                .version(this.getClass(), builder -> builder
                        .props(Props.list())
                        .notes(Arrays.asList(
                                "文件批量上传接口, body 中 files=[文件对象数组]"
                        ))
                        .build()
                        .demo(v -> v.setDemo(URL.SERVER.append(v.formatUrl())))
                )
                .execute(result -> {
                    result.versionAssert(version);
                    if (Util.isEmpty(files) || Util.isEmpty(files[0].getOriginalFilename())) {
                        throw FILE_EMPTY.exception("上传文件为空,字段【files】为数组");
                    }
                    result.setSuccess(
                            FileUpload.ofTemp()
                                    .files(files)
                                    .upload()
                                    .getResultList()
                    );
                });
    }
}