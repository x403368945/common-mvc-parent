package com.support.mvc.enums;

import com.support.mvc.entity.base.Item;
import com.support.mvc.entity.base.Result;
import com.support.mvc.exception.CodeException;
import com.utils.util.Dates;
import com.utils.util.FWrite;
import lombok.val;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 定义{@link Result#setCode(Code)}返回编码
 *
 * @author 谢长春 2019-1-9
 */
public enum Code {
    SUCCESS("成功"),
    FAILURE("失败"),
    CUSTOMIZE("自定义异常，将会以 exception 内容替换 message 内容，一般用于抛出带动态参数消息，直接在前端弹窗"),
    VALIDATED("参数校验失败"),
    VERSION("接口版本号不匹配"),
    LAMBDA("lambda 表达式抛出异常"),
    TIMEOUT("会话超时"),
    BAD_REQUEST("请求地址不存在"),
    ARGUMENT("请求缺少必要的参数"),
    CONVERT("请求参数类型不匹配或 JSON 格式不符合规范"),
    MAPPING("请求方式不支持"),
    URL_MAPPING("请求不存在"),
    ACCESS_DENIED("无操作权限"),
    PAGE_MAX_SIZE("分页查询，每页显示数据量超过最大值"),
    USER_PWD("用户名密码错误"),
    USER_DISABLED("账户已禁用"),
    USER_LOCKED("账户已锁定"),
    USER_EXPIRED("账户已过期"),
    CREDENTAILS_EXPIRED("证书已过期"),
    PWD("密码错误"),
    EMAIL_EXIST("邮箱已存在"),
    PHONE_EXIST("手机号已被占用"),
    IMAGE_CODE("图片验证码输入错误"),
    PHONE_CODE("短信验证码输入错误"),
    FILE_SUBFIX("文件格式不支持"),
    FILE_EMPTY("上传文件列表为空"),
    DOWNLOAD_PERMISSION("无下载权限"),
    URL_INVALID("链接已失效"),
    EMAIL("邮件发送失败，请检查邮箱是否正确"),
    OLD_PASSWORD("原密码输入错误"),
    ORDER_BY("排序字段不在可选范围"),
    FILE_COPY("文件复制失败"),

    ;
    /**
     * 枚举属性说明
     */
    public final String comment;

    Code(String comment) {
        this.comment = comment;
    }

    /**
     * 转换为 {@link Item} 对象
     *
     * @return {@link Item}
     */
    public Item getObject() {
        return Item.builder()
                .key(this.name())
                .value(this.ordinal())
                .comment(this.comment)
                .build();
    }

    /**
     * 通过 Code 构建 Result 对象；注：只能构建 Result<Object>，若要指定泛型，请使用 new Result<?> 指定泛型
     *
     * @return Result<Object>
     */
    public <E> Result<E> toResult() {
        return new Result<>(this);
    }

    /**
     * 通过 Code 构建 Result 对象；注：只能构建 Result<Object>，若要指定泛型，请使用 new Result<?> 指定泛型
     *
     * @param exception String 异常消息，可选参数，
     * @return Result<Object>
     */
    public <E> Result<E> toResult(final String exception) {
        return new Result<E>(this).setException(exception);
    }

    /**
     * 构造并返回 CodeException
     *
     * @param exception String 异常消息内容
     * @return CodeException
     */
    public CodeException exception(final String exception) {
        return new CodeException(this, exception);
    }

    /**
     * 构造并返回 CodeException
     *
     * @param exception String 异常消息内容
     * @param throwable Throwable 异常堆栈
     * @return CodeException
     */
    public CodeException exception(final String exception, final Throwable throwable) {
        return new CodeException(this, exception, throwable);
    }

    public static void main(String[] args) {
        { // 构建 js 枚举文件
            val name = "枚举：响应状态码";
            StringBuilder sb = new StringBuilder();
            sb.append("/**\n")
                    .append(" * ").append(name).append("\n")
                    .append(String.format(" * Created by 谢长春 on %s.%n", Dates.now().formatDate()))
                    .append(" */\n");
            sb.append("// 枚举值定义").append("\n");
            sb.append("const status = Object.freeze({").append("\n");
            Stream.of(Code.values()).forEach(item -> sb.append(
                    "\t{name}: {value: '{name}', comment: '{comment}'},"
                            .replace("{name}", item.name())
                            .replace("{comment}", item.comment)
                    ).append("\n")
            );
            sb.append("\tgetComment:function(key){return (this[key]||{}).comment}\n});").append("\n");
            sb.append("// 枚举值转换为选项集合").append("\n");
            sb.append("const options = [").append("\n");
            Stream.of(Code.values()).forEach(item -> sb.append(
                    "\t{value: status.{name}.value, label: status.{name}.comment},"
                            .replace("{name}", item.name())
                    ).append("\n")
            );
            sb.append("];").append("\n");
            sb.append("export default status;");
            System.out.println("JS文件输出路径：\n" +
                    FWrite.of("logs", Code.class.getSimpleName().concat(".js"))
                            .write(sb.toString())
                            .getAbsolute().orElse(null)
            );
        }

        System.out.println(
                Stream.of(Code.values())
                        .map(item -> String.format("%s【%s】", item.name(), item.comment))
                        .collect(Collectors.joining("|"))
        );
    }
}