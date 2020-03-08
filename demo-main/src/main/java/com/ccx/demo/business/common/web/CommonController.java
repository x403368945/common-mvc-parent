package com.ccx.demo.business.common.web;

import com.ccx.demo.business.common.vo.FileInfo;
import com.ccx.demo.business.common.vo.FileUpload;
import com.ccx.demo.business.common.vo.UserFileInfo;
import com.ccx.demo.config.init.AppConfig.Path;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.google.common.collect.Lists;
import com.support.mvc.entity.base.Item;
import com.support.mvc.entity.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.support.mvc.enums.Code.FILE_EMPTY;

@Api(tags = "通用模块")
@ApiSort(5)
@Controller
@RequestMapping("/1/common")
@Slf4j
@RequiredArgsConstructor
public class CommonController {
    @GetMapping("/enum/{clazz}")
    @ApiOperation(value = "1.获取枚举所有选项", tags = {"0.0.0"})
    @ApiOperationSupport(order = 1)
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    public Result<Item> enums(
            @ApiParam(required = true, value = "枚举类路径", example = "com.ccx.demo.business.user.entity.TabUser$OrderBy")
            @PathVariable final String clazz
    ) {
        return new Result<Item>().execute(result -> {
            try {
                final List<Item> items = Lists.newArrayList();
                final Class<Enum> enumClass = (Class<Enum>) Class.forName(clazz);
                for (Enum constant : enumClass.getEnumConstants()) {
                    try {
                        /*
                        com.support.mvc.enums.Code
                        com.ccx.demo.enums.Bool
                        */
                        items.add((Item) enumClass.getMethod("getObject").invoke(constant));
                    } catch (NoSuchMethodException e) {
                        /*
                        com.ccx.demo.business.user.entity.TabUser$OrderBy
                        */
                        items.add(Item.builder().key(constant.name()).build());
                    }
                }
                result.setSuccess(items);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }

    @GetMapping("/system")
    @ApiOperation(value = "2.系统环境变量", tags = {"0.0.0"})
    @ApiOperationSupport(order = 2)
    @ResponseBody
    public Result<SortedMap<String, Object>> system() {
        return new Result<SortedMap<String, Object>>().execute(result -> {
            final Properties properties = System.getProperties();
            final SortedMap<String, Object> map = new TreeMap<>();
            properties.forEach((key, value) -> map.put(Objects.toString(key), value));
            result.setSuccess(map);
        });
    }

    @ApiOperation(value = "3.上传到临时目录，单个上传")
    @ApiOperationSupport(order = 3)
    @PostMapping("/upload/temp")
    @ResponseBody
    public Result<FileInfo> uploadFileTemp(@RequestParam(required = false) final MultipartFile file) {
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

    @ApiOperation(value = "4.上传到临时目录，批量上传")
    @ApiOperationSupport(order = 4)
    @PostMapping("/uploads/temp")
    @ResponseBody
    public Result<FileInfo> uploadFilesTemp(@RequestParam(required = false) final MultipartFile[] files) {
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

    @ApiOperation(value = "5.上传到用户目录，单个上传：用户头像")
    @ApiOperationSupport(order = 5)
    @PostMapping("/upload/user")
    @ResponseBody
    public Result<UserFileInfo> uploadFileUser(@RequestParam(required = false) final MultipartFile file) {
        return new Result<UserFileInfo>().execute(result -> {
            if (Objects.isNull(file) || StringUtils.isBlank(file.getOriginalFilename())) {
                throw FILE_EMPTY.exception("上传文件为空,字段【file】只能是单个文件");
            }
            result.setSuccess(
                    FileUpload.of(UserFileInfo.class, Path.USER.absolute())
                            .files(file)
                            .upload()
                            .get()
            );
        });
    }

    @ApiOperation(value = "6.上传到用户目录，批量上传：用户头像")
    @ApiOperationSupport(order = 6)
    @PostMapping("/uploads/user")
    @ResponseBody
    public Result<UserFileInfo> uploadFilesUser(@RequestParam(required = false) final MultipartFile[] files) {
        return new Result<UserFileInfo>().execute(result -> {
            if (Objects.isNull(files) || StringUtils.isBlank(files[0].getOriginalFilename())) {
                throw FILE_EMPTY.exception("上传文件为空,字段【files】为数组");
            }
            result.setSuccess(
                    FileUpload.of(UserFileInfo.class, Path.USER.absolute())
                            .files(files)
                            .upload()
                            .getResultList()
            );
        });
    }
}
