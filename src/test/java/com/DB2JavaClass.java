package com;

import com.alibaba.fastjson.annotation.JSONType;
import com.google.common.base.Strings;
import com.utils.IJson;
import com.utils.util.*;
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
            final IAdapter adapter = // TODO 这里切换不同的适配器
                    new IAdapter.DefaultAdapter();
//                    new IAdapter.RdAdapter();

            Supplier<Module> supplier = () -> {
                final Module[] modules = Stream
                        .of(
                                "demo-main",
                                "demo-service"
                        )
                        .map(name -> Module.builder().source(name + "/src/main/resources/db/db.sql").output(name + "/src/main/java/com/ccx/business").build())
                        .toArray(Module[]::new);
                System.out.println("请选择输出模块目录，完成之后使用命令编译该模块：mvn clean compile");
                for (int i = 0; i < modules.length; i++) {
                    System.out.println(String.format("%d:%s【%s】", i, modules[i].output, modules[i].source));
                }
                final int index = new Scanner(System.in).nextInt();
                if (index < 0 || index >= modules.length) {
                    throw new NullPointerException("请选择有效的模块输出目录");
                }
                return modules[index];
            };
            final Module module = supplier.get();

            Supplier<String> templateSupplier = () -> {
                final Module[] modules = new Module[]{
                        Module.builder().source("all-id-long").output("全部 CRUD 代码[id:Long]").build(),
                        Module.builder().source("all-id-long-uid").output("全部 CRUD 代码[id:Long,uid:String]").build(),
                        Module.builder().source("all-id-string").output("全部 CRUD 代码[id:String]").build(),
                        Module.builder().source("search-simple-id-long").output("仅支持查询代码[id:Long]").build(),
                        Module.builder().source("search-simple-id-string").output("仅支持查询代码[id:String]").build()
                };
                System.out.println("请选择模板目录");
                for (int i = 0; i < modules.length; i++) {
                    System.out.println(String.format("%d:%s【%s】", i, modules[i].output, modules[i].source));
                }
                final int index = new Scanner(System.in).nextInt();
                if (index < 0 || index >= modules.length) {
                    throw new NullPointerException("请选择有效的模板目录");
                }
                return modules[index].source;
            };
            final String templateDir = templateSupplier.get();
            Supplier<String> packageSupplier = () -> {
                System.out.println("请输入包目录，例：com.ccx.demo");
                return new Scanner(System.in).nextLine();
            };
            final String pkg = packageSupplier.get();

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
                            final String tableComment = line.trim().toUpperCase().replaceAll(".* COMMENT '(.*)'.*", "$1").replace("\"", "'");
                            // Entity
                            {
                                FWrite.of(module.output, Names.javaname, "entity", Names.TabName.concat(".java"))
                                        .write( // Tab.java
                                                Names.format(templateDir, "Entity.java")
                                                        .replace("{pkg}", pkg)
                                                        .replace("{comment}", tableComment)
                                                        .replace("{IUser}", hasIUser ? "IUser," : "")
                                                        .replace("{ITimestamp}", hasModifyTime ? "ITimestamp, // 所有需要更新时间戳的实体类" : "")
                                                        .replace("{orders}", rows.stream().map(Row::getName).collect(Collectors.joining("\", \"")))
                                                        .replace("{orderBy}", rows.stream().map(Row::orderBy).map(v -> v.replace("{tabName}", Names.tabName)).collect(Collectors.joining(",\n")))
                                                        .replace("{props}", rows.stream().map(row -> row.props(hasLongId, adapter)).collect(Collectors.joining(",\n")))
                                                        .replace("{fields}", rows.stream().map(row -> row.fields(hasLongId, adapter)).collect(Collectors.joining("")))
                                                        .replace("{update}", rows.stream().map(Row::update).filter(Objects::nonNull).collect(Collectors.joining("\n")))
                                                        .replace("{where}", rows.stream().map(Row::where).filter(Objects::nonNull).collect(Collectors.joining("\n")))
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
                                                Optional
                                                        .of(Names.format(templateDir, "Repository.java")
                                                                .replace("{pkg}", pkg)
                                                                .replace("{comment}", tableComment)
                                                                .replace("{ID}", hasLongId ? "Long" : "String")
                                                        )
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
                                 (\S+)?IService => com.support.mvc.service.str.IService
                                */
                                /* ISimpleService:String id
                                 (\S+)?IService => com.support.mvc.service.str.ISimpleService
                                 , userId => null
                                 , \w+ \w+ userId =>
                                */
                                FWrite.of(module.output, Names.javaname, "service", Names.JavaName.concat("Service.java"))
                                        .write( // Service.java
                                                Optional
                                                        .of(Names.format(templateDir, "Service.java")
                                                                .replace("{pkg}", pkg)
                                                                .replace("{comment}", tableComment)
                                                                .replace("{ID}", hasLongId ? "Long" : "String")
                                                        )
                                                        .map(text -> {
                                                            if (hasLongId) {
                                                                return hasIUser ? text : text.replace("IService", "ISimpleService")
                                                                        .replace(", userId", ", null")
                                                                        .replaceAll(", \\w+ \\w+ userId", "");
                                                            } else {
                                                                if (hasIUser)
                                                                    return text.replaceAll("(\\S+)?IService", "com.support.mvc.service.str.IService");
                                                                else
                                                                    return text.replaceAll("(\\S+)?IService", "com.support.mvc.service.str.ISimpleService")
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
                                 (\S+)?IAuthController          => com.support.mvc.web.IController
                                @AuthenticationPrincipal.*\s+   =>
                                ,\s+user.getId\(\)              =>
                                */
                                FWrite.of(module.output, Names.javaname, "web", Names.JavaName.concat("Controller.java"))
                                        .write( // Controller.java
                                                Optional
                                                        .of(Names.format(templateDir, "Controller.java")
                                                                .replace("{pkg}", pkg)
                                                                .replace("{comment}", tableComment)
                                                                .replace("{ID}", hasLongId ? "Long" : "String")
                                                        )
                                                        .map(text -> hasIUser
                                                                ? text
                                                                : text.replaceAll("(\\S+)?IAuthController", "com.support.mvc.web.IController")
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
            java_name = tab_name.replaceFirst("^[a-zA-Z]+_", "").replace("_", "-");
            JavaName = Stream.of(tab_name.replaceFirst("^[a-zA-Z]+_", "").split("_")).map(Util::firstUpper).collect(Collectors.joining());
            javaName = Util.firstLower(JavaName);
            javaname = JavaName.toLowerCase();
        }

        private static String format(final String dir, final String filename) {
            return Objects.requireNonNull(FPath.of(Paths.get("src/test/files/template", dir, filename)).read()) // TODO 读取模板文件，替换占位参数
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
        JSON("String"),
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
            comment = Optional.ofNullable(m.group(9)).map(v -> v.replace("\"", "'")).orElse(null);
        }

        private String name;
        private DataType type;
        private Integer length;
        private Integer fixed;
        private String unsigned;
        private boolean notNull;
        private String comment;

        public String fields(final boolean hasLongId, final IAdapter adapter) {
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
                    sb.append("\t@NotNull(groups = {IUpdate.class, IMarkDelete.class})\n")
                            .append("\t@Size(min = 32, max = 32)\n")
                            .append(MessageFormat.format("\tprivate String {0};\n", name));
                    break;
                case "deleted":
                    sb.append("\t@Column(insertable = false, updatable = false)\n")
                            .append("\t@Null(groups = {ISave.class})\n")
                            .append(MessageFormat.format("\tprivate Radio {0};\n", name));
                    break;
                case "createTime":
                case "modifyTime":
                    sb.append("\t@Column(insertable = false, updatable = false)\n")
                            .append("\t@JSONField(format = \"yyyy-MM-dd HH:mm:ss\")\n")
                            .append("\t@Null(groups = {ISave.class})\n")
                            .append(MessageFormat.format("\tprivate Timestamp {0};\n", name));
                    break;
                case "createUserId":
                    sb.append("\t@Column(updatable = false)\n")
                            .append("\t@NotNull(groups = {ISave.class})\n")
                            .append("\t@Positive\n")
                            .append(MessageFormat.format("\tprivate Long {0};\n", name));
                    break;
                case "modifyUserId":
                    sb.append("\t@NotNull(groups = {ISave.class, IUpdate.class})\n")
                            .append("\t@Positive\n")
                            .append(MessageFormat.format("\tprivate Long {0};\n", name));
                    break;
                case "createUserName":
                    sb.append("\t@Column(updatable = false)\n")
                            .append("\t@NotNull(groups = {ISave.class})\n")
                            .append(MessageFormat.format("\t@Size(max = {0})\n", length))
                            .append(MessageFormat.format("\tprivate String {0};\n", name));
                    break;
                case "modifyUserName":
                    sb.append("\t@NotNull(groups = {ISave.class, IUpdate.class})\n")
                            .append(MessageFormat.format("\t@Size(max = {0})\n", length))
                            .append(MessageFormat.format("\tprivate String {0};\n", name));
                    break;
                default:
                    Op.ofNullable(adapter.fields(this, hasLongId))
                            .ifPresent(sb::append)
                            .elsePresent(str -> {
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
                            });
            }
            return sb.toString();
        }

        public String props(final boolean hasLongId, final IAdapter adapter) {
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
                    return Optional.ofNullable(adapter.props(this, hasLongId))
                            .orElseGet(() -> MessageFormat.format("\t\t{0}({1}.build({2}\"{3}\"))", name, type.type.toUpperCase(), notNull ? "true, " : "", comment));
            }
        }

        public String orderBy() {
            return String.format("\t\t%s({tabName}.%s)", name, name);
        }

        public String update() {
            return Arrays.asList("id", "uid", "deleted", "createTime", "createUserId", "modifyTime").contains(name)
                    ? null
                    : "//                .then({name}, update -> update.set(q.{name}, {name}))".replace("{name}", name);
        }

        public String where() {
            if (Arrays.asList("id").contains(name)) return null;
            switch (type) {
                case CHAR:
                    return "//                .and({name}, () -> {name}.endsWith(\"%\") || {name}.startsWith(\"%\") ? q.{name}.like({name}) : q.{name}.eq({name}))".replace("{name}", name);
                case VARCHAR:
                    return length < 100
                            ? "//                .and({name}, () -> {name}.endsWith(\"%\") || {name}.startsWith(\"%\") ? q.{name}.like({name}) : q.{name}.eq({name}))".replace("{name}", name)
                            : "//                .and({name}, () -> {name}.endsWith(\"%\") || {name}.startsWith(\"%\") ? q.{name}.like({name}) : q.{name}.endsWith({name}))".replace("{name}", name);

                case TEXT:
                    return "//                .and({name}, () -> {name}.endsWith(\"%\") || {name}.startsWith(\"%\") ? q.{name}.like({name}) : q.{name}.contains({name}))".replace("{name}", name);
                case DECIMAL:
                    return "//                .and({name}Range, () -> q.{name}.between({name}Range.getMin(), {name}Range.getMax()))".replace("{name}", name);
                case DATE:
                case DATETIME:
                case TIMESTAMP:
                    return "//                .and({name}Range, () -> q.{name}.between({name}Range.rebuild().getBegin(), {name}Range.getEnd()))".replace("{name}", name);

            }
            return "//                .and({name}, () -> q.{name}.eq({name}))".replace("{name}", name);
        }
//                                                        .replace("{update}", rows.stream().map(Row::getName)
//                                                                .filter(name -> !Arrays.asList("id", "uid", "deleted", "createTime", "createUserId", "modifyTime").contains(name))
//                .map(name -> "//                .then({name}, update -> update.set(q.{name}, {name}))".replace("{name}", name))
//                .collect(Collectors.joining("\n")))
//                .replace("{where}", rows.stream()
//                                                                .filter(row -> !Arrays.asList("id").contains(row.name))
//                .map(row -> "//                .and({name}, () -> q.{name}.{action}({name}))"
//                .replace("{name}", row.name)
//                                                                        .replace("{action}", Arrays.asList(DataType.CHAR, DataType.VARCHAR, DataType.TEXT).contains(row.type) ? "contains" : "eq"))
//                .collect(Collectors.joining("\n")))

        @Override
        public String toString() {
            return json();
        }
    }

    interface IAdapter {
        String fields(final Row row, final boolean hasLongId);

        String props(final Row row, final boolean hasLongId);

        class DefaultAdapter implements IAdapter {

            @Override
            public String fields(final Row row, final boolean hasLongId) {
                return null;
            }

            @Override
            public String props(final Row row, final boolean hasLongId) {
                return null;
            }
        }

        class RdAdapter implements IAdapter {

            @Override
            public String fields(final Row row, final boolean hasLongId) {
                switch (row.name) {
                    case "yyyy":
                        return "\t@NotNull\n"
                                .concat("\t@Min(2013)\n\t@Max(9999)\n")
                                .concat(MessageFormat.format("\tprivate Integer {0};\n", row.name));
                    case "mm":
                        return "\t@NotNull\n"
                                .concat("\t@Min(1)\n\t@Max(12)\n")
                                .concat(MessageFormat.format("\tprivate Integer {0};\n", row.name));
                    case "regionCode":
                        return MessageFormat.format("\tprivate RegionCode {0};\n", row.name);
                    case "bsBookCode":
                    case "pfsBookCode":
                        return MessageFormat.format("\tprivate BookCode {0};\n", row.name);
                }
                return null;
            }

            @Override
            public String props(final Row row, final boolean hasLongId) {
                switch (row.name) {
                    case "yyyy":
                    case "mm":
                        return MessageFormat.format("\t\t{0}(INTEGER.build(true, \"{1}\"))", row.name, row.comment);
                    case "regionCode":
                        return MessageFormat.format("\t\t{0}(ENUM.build(\"{1}\").setOptions(RegionCode.comments()))", row.name, row.comment);
                    case "bsBookCode":
                    case "pfsBookCode":
                        return MessageFormat.format("\t\t{0}(ENUM.build(\"{1}\").setOptions(BookCode.comments()))", row.name, row.comment);
                }
                return null;
            }
        }
    }
}
