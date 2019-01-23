# common-mvc-parent

## 动态环境打包
```
# 开发环境打包
mvn clean package
# mvn clean package -Pdeploy -Denv=dev
# 测试环境打包
mvn clean package -Pbeta
# mvn clean package -Pdeploy -Denv=beta
# 生产环境打包
mvn clean package -Pprod
# mvn clean package -Pdeploy -Denv=prod
```

### 注解说明
```
>> 类头部注解
@Table(name = "table_name")：注解映射数据库表名
@Entity：声明数据库映射实体类，将会被Spring JPA扫描
@Document：声明 MongoDB 映射实体类，将会被Spring Mongo扫描
@QueryEntity：声明 QueryDSL 实体类，将会被 QueryDSL 通用查询框架扫描，生成Q{ClassName}.java
@DynamicInsert：声明，编译生成 insert 语句时，当字段为 null ，则被忽略 
@DynamicUpdate：声明，编译生成 update 语句时，当字段为 null ，则被忽略
// @DynamicUpdate
// 这里有坑，当数据库字段有值，希望把数据库字段设置为 null，这种情况需要使用其他解决方案；
// 方案1：数据库设置时，数字默认 0，字符串默认 '' ；需要置空时，实体类设置属性为默认的 0 和 ''；
//   优点：代码量少逻辑简单；
//   缺点：JPA 只支持 ID 字段作为更新条件
// 方案2【推荐】：代码构建需要更新的字段，因为数据库有些字段可能不适合设置默认值
//   优点：更灵活，场景可适配；大部分场景下，只有ID作为更新匹配条件无法满足需求
//   缺点：代码量增加

@NoArgsConstructor：生成无参构造函数
@AllArgsConstructor：生成全参构造函数
@RequiredArgsConstructor(staticName = "of")：生成 final 修饰字段或者是以 @Nonnull 声明字段的静态构造函数，函数名为 of ；前提是类不能有以下注解 @NoArgsConstructor，@AllArgsConstructor
@Builder：生成链式类构造器
@Data：生成 get & set & toString & hashCode & equals 方法
@Accessors(chain = {true|false})：声明 @Data & @Setter 注解生成 set 方法时返回 this，便于链式调用；
// @Accessors(chain = true)
// 这里有坑，使用 QueryDSL 框架的 Projections.bean 时，因为 set 方法都带返回值，所以出现检测不到 set 方法
// 所以不建议在数据库映射的实体类中使用该注解；下面是采坑后的测试代码
// Stream.of(Introspector.getBeanInfo(Item.class).getPropertyDescriptors()).forEach(prop -> System.out.println(String.format("%s :----> %s", prop.getWriteMethod(), prop.getReadMethod())));
 
@Accessors(fluent = {true|false})：声明 @Data & @Setter & @Getter 注解生成 get & set 方法时不要 get & set 前缀
@JSONType(orders = {"id","name"})：声明实体类属性在 JSON 序列化时的排序；警告：必须声明所有返回字段的顺序，否则此声明不起作用；版本升级后再次测试发现该 bug 有修正，会对 orders 的字段优先，orders 不存在的字段才会乱序
@Slf4j：注解生成 log 属性，可在类中通过 log.{debug|info} 输出日志
 
>> 方法注解
@Synchronized：给方法加上同步锁
@SneakyThrows：声明自动抛异常，不需要 在方法上加 throw {Exception|NullPointException|IOException}
 
>> 属性注解
@Setter：生成当前属性的 get 方法
@Getter：生成当前属性的 set 方法
@Transient > @org.springframework.data.annotation.Transient：spirng-data mongodb 声明 JPA + Mongo 不与数据库建立映射，且 insert 和 update 忽略该属性
@Transient > @javax.persistence.Transient：spirng-data jpa hibernate 声明 JPA + Hibernate 不与数据库建立映射，且 insert 和 update 忽略该属性
@Indexed > @org.springframework.data.mongodb.core.index.Indexed：声明 mongodb 数据库生成索引
 
>> 属性或 get 方法都适用的注解
@JSONField(serialize = {true|false})：声明该属性或 get 方法在 JSON.toJSONString() 时是否被忽略，默认 true；为 false 表示忽略
@JSONField(deserialize = {true|false})：声明该属性或 set 方法在 JSON.parseObject() 时是否被忽略，默认 true；为 false 表示忽略
@JSONField(fromat = "yyyy-MM-dd HH:mm:ss")：声明 JSON.toJSONString() | JSON.parseObject() 时的日期格式
@Column(insertable = {true|false})：声明当 JPA 执行数据库 insert 时是否强制忽略（不论是否有值），默认 true，为 false 则强制忽略
@Column(updatable = {true|false})：声明当 JPA 执行数据库 update 时是否强制忽略（不论是否有值），默认 true，为 false 则强制忽略
@Id > @org.springframework.data.annotation.Id：注解声明 mongodb 实体ID
@Id > @javax.persistence.Id：注解声明 jpa hibernate 实体ID
@QueryTransient：声明生成 Q{ClassName}.java 时忽略该属性

>> 参数校验注解
@Valid：声明实体类参数参与校验；实体类的泛型集合可以通过该注解声明级联校验
@Validated：
  注解在类头部表示该 bean 开启注解校验，该 bean 所有方法中带有 @Valid @Min @Max 等注解的参数参与校验
  方法注解验证需要显式声明开启：@Bean public MethodValidationPostProcessor methodValidationPostProcessor() {return new MethodValidationPostProcessor();}
  ############### 
  # @Validated(groups={ISave.class})：注解在方法参数的 @Valid 前面，可以指定 @Valid 标记的实体将会采用分组校验，只校验实体中该分组标记的字段；
  # 测试发现在该注解在参数上不起作用，只能在方法头部指定分组才有效；但是方法头部@Validated(groups={Default.calss,ISave.class}), 必须加上Default.calss，否则方法参数上的校验注解必须加上ISave.class才会起作用
  ############### 
@Null：参数或属性必须是null
@NotNull：参数或属性不能为null
@NotBlank：参数或属性不能为 null 或 "" ,用于String类型
@NotEmpty：集合不能为null或empty,用于List集合

# 以下注解，值为 null 时都不会校验
@Size：字符串最大长度和最小长度，集合元素数量
@Pattern：字符串正则校验
@Min：数字最小值
@Max：数字最大值
@DecimalMin：数字最小值，BigDecimal
@DecimalMax：数字最大值，BigDecimal
@Digits：浮点数字，整数位和小数位长度
@Negative：必须是负数
@NegativeOrZero：必须是0或负数
@Positive：必须是正数
@PositiveOrZero：必须是0或正数
@AssertTrue：Boolean值必须为true
@AssertFalse：Boolean值必须为false
@Email：验证邮箱格式
@Past：过去的时间：必须小于当前时间
@Future：未来的时间：必须大于当前时间
```

### 初始化流程
```
# 源码下载地址，以下地址2选1
git clone https://gitee.com/xcc/common-utils.git
git clone https://github.com/x403368945/common-utils.git
cd {git代码下载位置}/common-utils/
mvn clean install # 安装到本地仓库

git clone https://gitee.com/xcc/common-mvc-parent.git
cd {git代码下载位置}/common-mvc-parent/
mvn clean install # 安装依赖包到本地仓库，web子项目打成war包

# 需要把以下目录设置为 Generated Sources Root，对着该目录右键 =》Mark Directory as =》 Generated Sources Root  
demo-main/target/generated-sources/java  
demo-service/target/generated-sources/java  
demo-security/target/generated-sources/java  
demo-socket/target/generated-sources/java  
```
* common-utils[jar]：工具类封装
* common-mvc[jar]：spring mvc 基础配置封装
* demo-main[war&jar]：应用入口【主模块】
* demo-service[war&jar]：参考案例【依赖于主模块】
* demo-security[war]：参考案例【Spring Security 基本应用】，可独立运行
* demo-socket[war]：参考案例【Spring Socket 基本应用】，可独立运行

