package com;

import com.alibaba.fastjson.annotation.JSONType;
import com.google.common.base.Strings;
import com.utils.IJson;
import com.utils.util.Dates;
import com.utils.util.FPath;
import com.utils.util.FWrite;
import com.utils.util.Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author 谢长春 on 2018/3/15.
 */
public class DB2JavaClass {
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    static class Module {
        private String output;
        private String source;
    }

    public static void main(String[] args) {
        try {
            Supplier<Module> supplier = () -> {
                final Module[] modules = new Module[]{
                        Module.builder().output("demo-main/src/main/java/com/demo/business").source("demo-main/src/main/resources/db/db.sql").build(),
                        Module.builder().output("demo-service/src/main/java/com/demo/business").source("demo-service/src/main/resources/db-demo-service.sql").build(),
                };
                System.out.println("请选择输出模块目录，完成之后使用命令编译该模块：mvn clean compile");
                for (int i = 0; i < modules.length; i++) {
                    System.out.println(String.format("%d:%s【%s】", i, modules[i].output, modules[i].source));
                }
                final int index = Integer.valueOf(new Scanner(System.in).nextLine());
                if (index <= 0 || index >= modules.length) {
                    throw new NullPointerException("请选择有效的模块输出目录");
                }
                return modules[index];
            };
            final Module module = supplier.get();

            final List<Row> rows = new ArrayList<>();
            System.out.println(Paths.get(module.source).toAbsolutePath().toString());
            Files.readAllLines(Paths.get(module.source))
                    .stream()
                    .map(String::trim)
                    .filter(Util::isNotEmpty)
//                    .peek(System.out::println)
                    .forEach(line -> {
                        if (line.toLowerCase().startsWith("create table")) { // 开始
                            Names.init(line.toLowerCase().replaceAll("(create table)|\\(", "").trim());
                            rows.clear();
                        } else if (line.startsWith("`")) { // 字段
                            rows.add(Row.of(line.trim()));
                        } else if (line.trim().startsWith(")")) {
                            // TODO 结束：生成 Controller、Service、Entity、Repository
                            final boolean hasIUser = rows.stream().anyMatch(row -> Arrays.asList("createUserId", "modifyUserId").contains(row.getName()));
                            final boolean hasLongId = rows.stream().filter(row -> Objects.equals("id", row.getName())).findFirst().map(row -> row.getType() == DataType.BIGINT || row.getType() == DataType.INT).orElseThrow(() -> new RuntimeException("集合中【id】行不存在"));
                            final boolean hasModifyTime = rows.stream().anyMatch(row -> Objects.equals("modifyTime", row.getName()));
                            // Entity
                            {
                                FWrite.of(module.output, Names.javaname, "entity", Names.TabName.concat(".java"))
                                        .write( // Tab.java
                                                Names.format("Entity.java")
                                                        .replace("{IUser}", hasIUser ? "IUser," : "")
                                                        .replace("{ITimestamp}", hasModifyTime ? "ITimestamp, // 所有需要更新时间戳的实体类" : "")
                                                        .replace("{orders}", rows.stream().map(Row::getName).collect(Collectors.joining("\", \"")))
                                                        .replace("{orderBy}", rows.stream().map(Row::orderBy).map(v -> v.replace("{tabName}", Names.tabName)).collect(Collectors.joining(",\n")))
                                                        .replace("{props}", rows.stream().map(row -> row.props(hasLongId)).collect(Collectors.joining(",\n")))
                                                        .replace("{fields}", rows.stream().map(row -> row.fields(hasLongId)).collect(Collectors.joining("")))
                                        );
                            }
                            // Repository
                            {
                                 /*
                                .and(q.createUserId.eq(userId)) =>
                                .createUserId(userId)           =>
                                .set(q.modifyUserId, userId)    =>
                                * modifyTime 字段不存在
                                .and(q.modifyTime.eq(obj.getModifyTime())) =>
                                */
                                FWrite.of(module.output, Names.javaname, "dao", "jpa", Names.JavaName.concat("Repository.java"))
                                        .write( // Repository.java
                                                Optional.of(Names.format("Repository.java").replace("{ID}", hasLongId ? "Long" : "String"))
                                                        .map(text -> hasIUser ? text
                                                                : text.replace(".and(q.createUserId.eq(userId))", "")
                                                                .replace(".createUserId(userId)", "")
                                                                .replace(".set(q.modifyUserId, userId)", "")
                                                        )
                                                        .map(text -> hasModifyTime ? text : text.replace(".and(q.modifyTime.eq(obj.getModifyTime()))", ""))
                                                        .map(text -> hasLongId ? text : text.replace("Long[]::new", "String[]::new"))
                                                        .orElse("")
                                        );
                            }
                            // Service
                            {
                                /* ISimpleService
                                 IService => ISimpleService
                                 , userId => null
                                 , \w+ \w+ userId =>
                                */
                                /* IService:String id
                                 (\S+)?IService => com.mvc.service.str.IService
                                */
                                /* ISimpleService:String id
                                 (\S+)?IService => com.mvc.service.str.ISimpleService
                                 , userId => null
                                 , \w+ \w+ userId =>
                                */
                                FWrite.of(module.output, Names.javaname, "service", Names.JavaName.concat("Service.java"))
                                        .write( // Service.java
                                                Optional.of(Names.format("Service.java").replace("{ID}", hasLongId ? "Long" : "String"))
                                                        .map(text -> {
                                                            if (hasLongId) {
                                                                return hasIUser ? text : text.replace("IService", "ISimpleService")
                                                                        .replace(", userId", ", null")
                                                                        .replaceAll(", \\w+ \\w+ userId", "");
                                                            } else {
                                                                if (hasIUser)
                                                                    return text.replaceAll("(\\S+)?IService", "com.mvc.service.str.IService");
                                                                else
                                                                    return text.replaceAll("(\\S+)?IService", "com.mvc.service.str.ISimpleService")
                                                                            .replace(", userId", ", null")
                                                                            .replaceAll(", \\w+ \\w+ userId", "");
                                                            }
                                                        })
                                                        .orElse("")
                                        );
                            }
                            // Controller
                            {
                                /*
                                 (\S+)?IAuthController          => com.mvc.web.IController
                                @AuthenticationPrincipal.*\s+   =>
                                ,\s+user.getId\(\)              =>
                                */
                                FWrite.of(module.output, Names.javaname, "web", Names.JavaName.concat("Controller.java"))
                                        .write( // Controller.java
                                                Optional.of(Names.format("Controller.java").replace("{ID}", hasLongId ? "Long" : "String"))
                                                        .map(text -> hasIUser
                                                                ? text
                                                                : text.replaceAll("(\\S+)?IAuthController", "com.mvc.web.IController")
                                                                .replaceAll("@AuthenticationPrincipal.*\\s+", "")
                                                                .replaceAll(",\\s+user.getId\\(\\)", "")
                                                        )
                                                        .map(text -> hasLongId ? text : text.replaceAll("(\\d+)L", "\"$1\""))
                                                        .orElse("")
                                        );
                            }
                            Names.clear();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static class Names {
        private static void clear() {
            tab_name = null;
            TabName = null;
            tabName = null;
            java_name = null;
            JavaName = null;
            javaName = null;
            javaname = null;
        }

        private static void init(final String name) {
            tab_name = name;
            TabName = Stream.of(tab_name.split("_")).map(Util::firstUpper).collect(Collectors.joining());
            tabName = Util.firstLower(TabName);
            java_name = tab_name.replaceFirst("tab_", "").replace("_", "-");
            JavaName = Stream.of(tab_name.replaceFirst("tab_", "").split("_")).map(Util::firstUpper).collect(Collectors.joining());
            javaName = Util.firstLower(JavaName);
            javaname = JavaName.toLowerCase();
        }

        private static String format(final String filename) {
            return FPath.of(Paths.get("src/test/files/template", filename)).read() // TODO 读取模板文件，替换占位参数
                    .replace("{date}", date)
                    .replace("{tab_name}", tab_name)
                    .replace("{TabName}", TabName)
                    .replace("{tabName}", tabName)
                    .replace("{javaName}", javaName)
                    .replace("{JavaName}", JavaName)
                    .replace("{java_name}", java_name)
                    .replace("{javaname}", javaname)
                    ;
        }

        private static String date = Dates.now().formatDate();
        private static String tab_name;
        private static String TabName;
        private static String tabName;
        private static String javaName;
        private static String JavaName;
        private static String java_name;
        private static String javaname;
    }

    /**
     * mysql 与 java 数据类型映射
     */
    private enum DataType {
        TINYINT("Short"),
        SMALLINT("Short"),
        MEDIUMINT("Integer"),
        INT("Integer"),
        BIGINT("Long"),
        DECIMAL("Double"),
        CHAR("String"),
        VARCHAR("String"),
        TEXT("String"),
        DATE("Timestamp"),
        TIMESTAMP("Timestamp"),
        DATETIME("Timestamp"),
        ;
        final String type;

        DataType(final String type) {
            this.type = type;
        }
    }

    @NoArgsConstructor
    @Data
    @JSONType(orders = {"name", "type", "length", "fixed", "unsigned", "notNull", "defaultValue", "comment"})
    static class Row implements IJson {
        static final Pattern pattern = Pattern.compile("(?i)`(\\w+)` (\\w+)(\\((\\d+)(, (\\d+))?\\))?( UNSIGNED)?(.*) COMMENT '(.+)'.*$");

        static Row of(final String line) {
            final Matcher m = pattern.matcher(line.replaceAll("\\s+", " ").trim());
            if (m.find()) {
//                System.out.println(JSON.toJSONString(
//                        RangeInt.of(1, m.groupCount())
//                                .map(i -> Optional.ofNullable(m.group(i)).map(String::trim).orElse("")).collect(Collectors.toList())
//                ));
                return new Row(m);
            } else {
                throw new RuntimeException("正则匹配字段行失败：" + line);
            }
        }

        private Row(final Matcher m) {
            name = Objects.requireNonNull(m.group(1), "正则匹配【name】为空");
            type = DataType.valueOf(Objects.requireNonNull(m.group(2), "正则匹配【type】为空").toUpperCase());
            length = Optional.ofNullable(m.group(4)).map(Integer::valueOf).orElse(null);
            fixed = Optional.ofNullable(m.group(6)).map(Integer::valueOf).orElse(null);
            unsigned = Optional.ofNullable(m.group(7)).orElse(null);
            Optional.ofNullable(m.group(8)).map(String::trim).map(String::toUpperCase)
                    .ifPresent(content -> notNull = content.contains("NOT NULL") && !content.contains("DEFAULT "));
            comment = Optional.ofNullable(m.group(9)).orElse(null);
        }

        private String name;
        private DataType type;
        private Integer length;
        private Integer fixed;
        private String unsigned;
        private boolean notNull;
        private String comment;

        public String fields(final boolean hasLongId) {
            final StringBuffer sb = new StringBuffer(String.format("\t/**\n\t * %s\n\t */\n", comment));
            switch (name) {
                case "id":
                    sb.append("\t@Id\n");
                    if (hasLongId) {
                        sb.append("\t@GeneratedValue(strategy = GenerationType.IDENTITY)\n");
                        sb.append("\t@NotNull(groups = {IUpdate.class, IMarkDelete.class})\n");
                        sb.append("\t@Positive\n");
                        sb.append(MessageFormat.format("\tprivate Long {0};\n", name));
                    } else {
                        sb.append("\t@NotBlank(groups = {IUpdate.class, IMarkDelete.class})\n");
                        sb.append(MessageFormat.format("\t@Size(max = {0})\n", length));
                        sb.append(MessageFormat.format("\tprivate String {0};\n", name));
                    }
                    break;
                case "uid":
                    sb.append("\t@NotNull(groups = {IUpdate.class, IMarkDelete.class})\n");
                    sb.append("\t@Size(min = 32, max = 32)\n");
                    sb.append(MessageFormat.format("\tprivate String {0};\n", name));
                    break;
                case "deleted":
                    sb.append("\t@Column(insertable = false, updatable = false)\n");
                    sb.append("\t@Null(groups = {ISave.class})\n");
                    sb.append(MessageFormat.format("\tprivate Radio {0};\n", name));
                    break;
                case "createTime":
                case "modifyTime":
                    sb.append("\t@Column(insertable = false, updatable = false)\n");
                    sb.append("\t@JSONField(format = \"yyyy-MM-dd HH:mm:ss\")\n");
                    sb.append("\t@Null(groups = {ISave.class})\n");
                    sb.append(MessageFormat.format("\tprivate Timestamp {0};\n", name));
                    break;
                case "createUserId":
                    sb.append("\t@Column(updatable = false)\n");
                    sb.append("\t@NotNull(groups = {ISave.class})\n");
                    sb.append("\t@Positive\n");
                    sb.append(MessageFormat.format("\tprivate Long {0};\n", name));
                    break;
                case "modifyUserId":
                    sb.append("\t@NotNull(groups = {ISave.class, IUpdate.class})\n");
                    sb.append("\t@Positive\n");
                    sb.append(MessageFormat.format("\tprivate Long {0};\n", name));
                    break;
                case "createUserName":
                    sb.append("\t@Column(updatable = false)\n");
                    sb.append("\t@NotNull(groups = {ISave.class})\n");
                    sb.append(MessageFormat.format("\t@Size(max = {0})\n", length));
                    sb.append(MessageFormat.format("\tprivate String {0};\n", name));
                    break;
                case "modifyUserName":
                    sb.append("\t@NotNull(groups = {ISave.class, IUpdate.class})\n");
                    sb.append(MessageFormat.format("\t@Size(max = {0})\n", length));
                    sb.append(MessageFormat.format("\tprivate String {0};\n", name));
                    break;
                default:
                    if (notNull) sb.append("\t@NotNull\n");
                    switch (type) {
                        case TINYINT:
                            if (Objects.isNull(unsigned)) {
                                Optional.ofNullable(length).map(v -> new BigDecimal("-" + Strings.repeat("9", length / 2))).ifPresent(v -> sb.append(String.format("\t@Min(%d)\n", Math.max(-128, v.intValue()))));
                                Optional.ofNullable(length).map(v -> new BigDecimal(Strings.repeat("9", length / 2))).ifPresent(v -> sb.append(String.format("\t@Max(%d)\n", Math.min(Short.MAX_VALUE, v.intValue()))));
                            } else {
                                sb.append("\t@Min(0)\n");
                                Optional.ofNullable(length).map(v -> new BigDecimal(Strings.repeat("9", length))).ifPresent(v -> sb.append(String.format("\t@Max(%d)\n", Math.min(Short.MAX_VALUE, v.intValue()))));
                            }
                            break;
                        case SMALLINT:
                            if (Objects.isNull(unsigned)) {
                                Optional.ofNullable(length).map(v -> new BigDecimal("-" + Strings.repeat("9", length / 2))).ifPresent(v -> sb.append(String.format("\t@Min(%d)\n", Math.max(Short.MIN_VALUE, v.intValue()))));
                                Optional.ofNullable(length).map(v -> new BigDecimal(Strings.repeat("9", length / 2))).ifPresent(v -> sb.append(String.format("\t@Max(%d)\n", Math.min(Short.MAX_VALUE, v.intValue()))));
                            } else {
                                sb.append("\t@Min(0)\n");
                                Optional.ofNullable(length).map(v -> new BigDecimal(Strings.repeat("9", length))).ifPresent(v -> sb.append(String.format("\t@Max(%d)\n", Math.min(Short.MAX_VALUE, v.intValue()))));
                            }
                            break;
                        case MEDIUMINT:
                            if (Objects.isNull(unsigned)) {
                                Optional.ofNullable(length).map(v -> new BigDecimal("-" + Strings.repeat("9", length / 2))).ifPresent(v -> sb.append(String.format("\t@Min(%d)\n", Math.max(Integer.MIN_VALUE, v.intValue()))));
                                Optional.ofNullable(length).map(v -> new BigDecimal(Strings.repeat("9", length / 2))).ifPresent(v -> sb.append(String.format("\t@Max(%d)\n", Math.min(Integer.MAX_VALUE, v.intValue()))));
                            } else {
                                sb.append("\t@Min(0)\n");
                                Optional.ofNullable(length).map(v -> new BigDecimal(Strings.repeat("9", length))).ifPresent(v -> sb.append(String.format("\t@Max(%d)\n", Math.min(Integer.MAX_VALUE, v.longValue()))));
                            }
                            break;
                        case INT:
                            if (Objects.isNull(unsigned)) {
                                Optional.ofNullable(length).map(v -> new BigDecimal("-" + Strings.repeat("9", length / 2))).ifPresent(v -> sb.append(String.format("\t@Min(%d)\n", Math.max(Integer.MIN_VALUE, v.longValue()))));
                                Optional.ofNullable(length).map(v -> new BigDecimal(Strings.repeat("9", length / 2))).ifPresent(v -> sb.append(String.format("\t@Max(%d)\n", Math.min(Integer.MAX_VALUE, v.longValue()))));
                            } else {
                                sb.append("\t@Min(0)\n");
                                Optional.ofNullable(length).map(v -> new BigDecimal(Strings.repeat("9", length))).ifPresent(v -> sb.append(String.format("\t@Max(%d)\n", Math.min(Integer.MAX_VALUE, v.longValue()))));
                            }
                            break;
                        case BIGINT:
                            if (Objects.isNull(unsigned)) {
                                Optional.ofNullable(length).map(v -> new BigDecimal("-" + Strings.repeat("9", length / 2))).ifPresent(v -> sb.append(String.format("\t@Min(%d)\n", v.max(BigDecimal.valueOf(Long.MIN_VALUE)).longValue())));
                                Optional.ofNullable(length).map(v -> new BigDecimal(Strings.repeat("9", length / 2))).ifPresent(v -> sb.append(String.format("\t@Max(%d)\n", v.min(BigDecimal.valueOf(Long.MAX_VALUE)).longValue())));
                            } else {
                                sb.append("\t@Min(0)\n");
                                Optional.ofNullable(length).map(v -> new BigDecimal(Strings.repeat("9", length))).ifPresent(v -> sb.append(String.format("\t@Max(%d)\n", v.min(BigDecimal.valueOf(Long.MAX_VALUE)).longValue())));
                            }
                            break;
                        case DECIMAL:
//                            if (Objects.nonNull(length))
//                                sb.append(String.format("\t@Digits(integer = %d, fraction = %d)\n", length, Objects.nonNull(fixed) ? fixed : 0));
                            break;
                        case CHAR:
                            sb.append(String.format("\t@Size(min = %d, max = %d)\n", length, length));
                            break;
                        case VARCHAR:
                            sb.append(String.format("\t@Size(max = %d)\n", length));
                            break;
                        case TEXT:
                            sb.append(String.format("\t@Size(max = %d)\n", Math.min(65535, Objects.nonNull(length) ? length : 65535)));
                            break;
                        case DATE:
                            sb.append("\t@JSONField(format = \"yyyy-MM-dd\")\n");
                            break;
                        case DATETIME:
                        case TIMESTAMP:
                            sb.append("\t@JSONField(format = \"yyyy-MM-dd HH:mm:ss\")\n");
                            break;
                    }
                    sb.append(String.format("\tprivate %s %s;\n", type.type, name));
            }
            return sb.toString();
        }

        public String props(final boolean hasLongId) {
            switch (name) {
                case "id":
                    return MessageFormat.format("id({0}.build(true, \"{1}\"))", hasLongId ? "LONG" : "STRING", comment);
                case "uid":
                    return MessageFormat.format("\t\tuid(STRING.build(true, \"{0}\"))", comment);
                case "deleted":
                    return "\t\tdeleted(ENUM.build(\"是否逻辑删除\").setOptions(Radio.comments()))";
                case "createTime":
                case "modifyTime":
                    return MessageFormat.format("\t\t{0}(TIMESTAMP.build(\"{1}\"))", name, comment);
                case "createUserId":
                case "modifyUserId":
                    return MessageFormat.format("\t\t{0}(LONG.build(\"{1}\"))", name, comment);
                case "createUserName":
                case "modifyUserName":
                    return MessageFormat.format("\t\t{0}(STRING.build(\"{1}\"))", name, comment);
                default:
                    return MessageFormat.format("\t\t{0}({1}.build({2}\"{3}\"))", name, type.type.toUpperCase(), notNull ? "true, " : "", comment);
            }
        }

        public String orderBy() {
            return String.format("\t\t%s({tabName}.%s.asc(), {tabName}.%s.desc())", name, name, name);
        }

        @Override
        public String toString() {
            return json();
        }
    }
}
