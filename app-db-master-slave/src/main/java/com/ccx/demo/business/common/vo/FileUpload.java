package com.ccx.demo.business.common.vo;

import com.ccx.demo.config.init.AppConfig.Path;
import com.utils.util.FPath;
import com.utils.util.FPath.FileName;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 文件上传工具类
 *
 *
 * @author 谢长春  2016-11-23
 */
@Slf4j
public class FileUpload<T> {

    public static FileUpload<FileInfo> ofTemp() {
        return of(Path.TEMP.absolute());
    }

    public static FileUpload<FileInfo> of(final String absolute) {
        return new FileUpload<>(FileInfo.class).to(absolute);
    }

    public static <T> FileUpload<T> of(final Class<T> clazz, final String absolute) {
        return new FileUpload<>(clazz).to(absolute);
    }

    private FileUpload(Class<T> clazz) {
        this.clazz = clazz;
    }

    private Class<T> clazz;
    /**
     * 要上传的文件列表
     */
    private MultipartFile[] uploadFiles;
    /**
     * 文件存储目录，默认为临时目录
     */
    private String to;

    /**
     * 上传成功的文件集合
     */
    @Getter
    private List<T> resultList = new ArrayList<>();

    public FileUpload<T> files(final MultipartFile file) {
        if (Objects.nonNull(file)) {
            uploadFiles = new MultipartFile[]{file};
        }
        return this;
    }

    public FileUpload<T> files(final MultipartFile[] files) {
        if (Objects.nonNull(files)) {
            uploadFiles = files;
        }
        return this;
    }

    public FileUpload<T> to(final String path) {
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(path)) {
            this.to = path;
        }
        return this;
    }

    /**
     * 校验文件后缀名是否符合要求
     *
     * @param reg      String
     * @param consumer Consumer<String> 校验异常消息回调
     * @return FileUpload<T>
     */
    public FileUpload<T> check(final String reg, final Consumer<String> consumer) {
        if (Objects.isNull(uploadFiles)) {
            consumer.accept("上传文件集合为空");
        } else {
            for (MultipartFile uploadFile : uploadFiles) {
                if (!uploadFile.getOriginalFilename().matches(reg)) {
                    consumer.accept(String.format("【%s】文件校验失败", uploadFile.getOriginalFilename()));
                    return this;
                }
            }
        }
        return this;
    }

    /**
     * 上传文件
     *
     * @return FileUpload<T> 当前对象
     */
    @SneakyThrows
    public FileUpload<T> upload() {
        if (StringUtils.isEmpty(to)) {
            throw new NullPointerException("未指定文件上传目录");
        }
        if (Objects.isNull(uploadFiles)) {
            return this;
        }
        FPath localFile;
        FileInfo fileInfo;
        for (MultipartFile uploadFile : uploadFiles) {
            if (Objects.isNull(uploadFile)) {
                continue;
            }
            fileInfo = FileInfo.builder()
                    .name(uploadFile.getOriginalFilename())
                    .uname(FileName.of(uploadFile.getOriginalFilename()).getUuidFileName())
                    .build();
            localFile = FPath.of(to, fileInfo.getUname());
            // 将文件写入到本地
            uploadFile.transferTo(localFile.file());
            if (log.isDebugEnabled()) log.debug("上传文件路径：{}", localFile.chmod().absolute());
            resultList.add(fileInfo.parse(clazz));
        }
        return this;
    }

    public T get() {
        return get(0);
    }

    public T get(int index) {
        return resultList.get(index);
    }

}
