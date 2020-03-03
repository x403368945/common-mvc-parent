package com.ccx.demo.business.common.web;

import com.ccx.demo.business.common.vo.FileInfo;
import com.ccx.demo.business.common.vo.FileUpload;
import com.support.mvc.entity.base.Result;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.support.mvc.enums.Code.FILE_EMPTY;

/**
 * 文件处理
 *
 * @author 谢长春 2017年7月12日 下午2:01:20
 */
@Controller
@RequestMapping("/1/file")
@Slf4j
public class FileController {

    public Logger getLog() {
        return log;
    }

    @PostMapping("/upload")
    @ResponseBody
    public Result<?> uploadFile(@RequestParam(required = false) final MultipartFile file) {
        return new Result<FileInfo>().execute(result -> {
            if (Objects.isNull(file) || StringUtils.isBlank(file.getOriginalFilename())) {
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
    public Result<?> uploadFiles(@RequestParam(required = false) final MultipartFile[] files) {
        return new Result<FileInfo>().execute(result -> {
            if (Objects.isNull(files) || StringUtils.isBlank(files[0].getOriginalFilename())) {
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