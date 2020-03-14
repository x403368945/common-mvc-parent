# common-mvc-parent

## 动态环境打包
```
# 开发环境打包
mvn clean install -Dmaven.test.skip=true -Pdev
# 测试环境打包
mvn clean install -Dmaven.test.skip=true -Pbeta
# 生产环境打包
mvn clean install -Dmaven.test.skip=true -Pprod
```

## 清除 .iml 文件
```
# 直接运行只要一个 % 定义变量，bat 文件运行需要两个 %% 定义变量
for /r ./ %f in (*.iml) do del /a /f "%f"

# linux 
find ./ -name \*.iml | xargs rm -rf
```

### 初始化流程
```
# 源码下载地址，以下地址2选1
git clone https://github.com/x403368945/common-utils.git
git clone https://gitee.com/xcc/common-mvc-parent.git

cd common-mvc-parent/
mvn clean compile # 编译时会生成 QueryDSL 查询实体

```
* common-mvc[jar]：spring mvc 基础配置封装
* app-demo[jar]：应用组装部署模块，将所有需要发布的模块组装之后打成 war 包，作为单体应用发布，避免循环依赖打包报错
* demo-main[jar]：应用入口【主模块】
* demo-service[jar]：参考案例【依赖于主模块】

### 注解说明

JPA  注解

|注解|位置|说明|
|---|:---:|---|
|@Table|类|注解映射数据库表名|
|-|-|name = "数据库表名"|
|-|-|uniqueConstraints = {@UniqueConstraint(columnNames = "uid")}|
|@Entity|类|声明数据库映射实体类，将会被Spring JPA扫描|
|@DynamicInsert|类|声明，编译生成 insert 语句时，当字段为 null ，则被忽略|
|@DynamicUpdate|类|声明，编译生成 update 语句时，当字段为 null ，则被忽略|
|-|-|这里有坑，当数据库字段有值，希望把数据库字段设置为 null，这种情况需要使用其他解决方案；|
|-|-|方案1：数据库设置时，数字默认 0，字符串默认 '' ；需要置空时，实体类设置属性为默认的 0 和 ''；|
|-|-|--  优点：代码量少逻辑简单；|
|-|-|--  缺点：JPA 只支持 ID 字段作为更新条件|
|-|-|方案2【推荐】：代码构建需要更新的字段，因为数据库有些字段可能不适合设置默认值|
|-|-|--  优点：更灵活，场景可适配；大部分场景下，只有ID作为更新匹配条件无法满足需求|
|-|-|--  缺点：代码量增加|
|@Transient|属性|@javax.persistence.Transient：spring-data jpa hibernate 声明 JPA + Hibernate 不与数据库建立映射，且 insert 和 update 忽略该属性|
|@Column|属性|声明实体与数据库字段映射，如果实体属性未指定该注解，则使用 spring.jpa.hibernate.naming.physical-strategy 配置的策略，所以如果实体添加扩展属性，不需要与数据库映射时，一定要加上 @Transient 注解；@Column(insertable = false, updatable = false)|
|-|-|  name = "数据库字段名"|
|-|-|  insertable = {true,false}, 声明数据库 insert 时是否强制忽略（不论是否有值），默认 true，为 false 则强制忽略|
|-|-|  updatable = {true,false}, 声明数据库 update 时是否强制忽略（不论是否有值），默认 true，为 false 则强制忽略|
|@Id|属性|@javax.persistence.Id：注解声明 jpa hibernate 实体ID|
|@Modifying|方法|声明更新方法|
|@Query|方法|声明查询方法|
|-|-|value="HQL"，支持原生SQL，但需要加上 nativeQuery=true|
|-|-|nativeQuery=true, 声明 value 上的语句为原生 SQL|
|@GeneratedValue|属性|声明 ID 生成策略|
|-|-|@GeneratedValue(strategy = GenerationType.IDENTITY)； 使用数据库自增主键|
|-|-|@GeneratedValue(generator = "uuid32")；自定义 ID 生成策略，必须要指定 @GenericGenerator|
|@GenericGenerator|属性|自定义 ID 生成策略，@GenericGenerator(name = "uuid32", strategy = "com.support.mvc.entity.UUID32Generator")|
|@EnableJpaAuditing|类|JPA 审核模式开关，开启之后实体类加上 @EntityListeners(AuditingEntityListener.class) 之后 @CreatedDate,@LastModifiedDate 才会生效|
|@EntityListeners|类|声明该实体操作数据库时将会被 JPA 审核； @EntityListeners(AuditingEntityListener.class)，必须先开启 @EnableJpaAuditing|
|@CreatedDate|属性|新增数据时，JPA 自动注入当前时间到该属性|
|@LastModifiedDate|属性|更新数据时，JPA 自动注入当前时间到该属性|
|@Enumerated|属性|声明枚举映射策略；@Enumerated(EnumType.STRING)|
|@Procedure|方法|声明存储过程调用|
|@NoRepositoryBean|类|声明 JPA 扫描时忽略该 Repository |
|@UniqueConstraint|注解|声明唯一索引，@UniqueConstraint(columnNames = "uid")|

JPA Mongo 注解

|注解|位置|说明|
|---|:---:|---|
|@EnableMongoAuditing|类|JPA 审核模式开关，开启之后实体类加上 @EntityListeners(AuditingEntityListener.class) 之后 @CreatedDate,@LastModifiedDate 才会生效|
|@Document|类|声明 MongoDB 映射实体类，将会被Spring Mongo扫描|
|@Id|属性|@org.springframework.data.annotation.Id：注解声明 mongodb 实体ID|
|@Transient|属性|@org.springframework.data.annotation.Transient：spring-data mongodb 声明 JPA + Mongo 不与数据库建立映射，且 insert 和 update 忽略该属性|
|@Indexed|属性|@org.springframework.data.mongodb.core.index.Indexed：声明 mongodb 数据库生成索引|
|@Convert|属性|声明数据库和实体映射需要使用转换器，@Convert(converter = JsonObjectConvert.class)|
|@EntityListeners|类|声明该实体操作数据库时将会被 JPA 审核； @EntityListeners(AuditingEntityListener.class)，必须先开启 @EnableJpaAuditing|
|@CreatedDate|属性|新增数据时，JPA 自动注入当前时间到该属性|
|@LastModifiedDate|属性|更新数据时，JPA 自动注入当前时间到该属性|
|@Enumerated|属性|声明枚举映射策略；@Enumerated(EnumType.STRING)|

QueryDSL 注解

|注解|位置|说明|
|---|:---:|---|
|@QueryEntity|类|声明 QueryDSL 实体类，将会被 QueryDSL 通用查询框架扫描，生成Q{ClassName}.java|
|@QueryTransient|属性|声明生成 Q{ClassName}.java 时忽略该属性|
|-|-|非数据库字段属性必须加上该注解，也就是实体上只要有 @Transient 地方一定要有 @QueryTransient|
|@QueryType|属性|声明查询类型，一般用于数据库使用 JSON 类型时，实体是用对象接收，查询时必须指定 @QueryType(PropertyType.STRING) |

lombok 注解

|注解|位置|说明|
|---|:---:|---|
|@NoArgsConstructor|类|生成无参构造函数|
|@AllArgsConstructor|类|生成全参构造函数|
|@RequiredArgsConstructor|类|生成 final 修饰字段或者是以 @Nonnull 声明字段的静态构造函数，函数名为 of ；前提是类不能有以下注解 @NoArgsConstructor，@AllArgsConstructor|
|-|-|staticName = "of" 声明静态构造函数方法名|
|@EqualsAndHashCode|类|生成类的 hash 码|
|-|-|callSuper = {true,false}，声明 hash 是否携带父类属性，建议子类必须添加 @EqualsAndHashCode(callSuper = true)|
|@ToString|类|生成 toString 方法|
|-|-|callSuper = {true,false}，声明 toString 是否携带父类属性，建议子类必须添加 @ToString(callSuper = true)|
|@Builder|类|生成链式类构造器，不支持父类继承属性|
|@Data|类|生成 get & set & toString & hashCode & equals 方法，子类必须添加 @EqualsAndHashCode(callSuper = true) 和 @ToString(callSuper = true)|
|@Accessors|类|声明 @Data & @Setter 生成 set 方法时的规则|
|-|-|chain = {true,false}，为 true 时声明 set 方法返回当前类，便于链式调用|
|-|-|--  这里有坑，使用 QueryDSL 框架的 Projections.bean 时，因为 set 方法都带返回值，所以出现检测不到 set 方法|
|-|-|--  所以不建议在数据库映射的实体类中使用该注解；下面是采坑后的测试代码|
|-|-|--  Stream.of(Introspector.getBeanInfo(Item.class).getPropertyDescriptors()).forEach(prop -> System.out.println(String.format("%s :----> %s", prop.getWriteMethod(), prop.getReadMethod())));|
|-|-|fluent = {true,false}， 声明 @Data & @Setter & @Getter 注解生成 get & set 方法时不要 get & set 前缀|
|@Slf4j|类|注解生成 log 属性，可在类中通过 log.{debug,info} 输出日志|
|@Synchronized|方法|给方法加上同步锁|
|@SneakyThrows|方法|声明自动抛异常，不需要 在方法上加 throw {Exception,NullPointException,IOException}|
|@Setter|类|声明该类的所有属性只生成 set 方法|
|-|属性|声明该属性只生成 set 方法|
|@Getter|类|声明该类的所有属性只生成 get 方法|
|-|属性|声明该属性只生成 get 方法|
|@Cleanup|局部变量|用于方法执行完成之后调用局部变量特定的方法，一般用于关闭文件流|
|-|-|@Cleanup("指定调用的方法")；默认执行 close 方法|

fastjson 注解

|注解|位置|说明|
|---|:---:|---|
|@QueryEntity|类|声明 QueryDSL 实体类，将会被 QueryDSL 通用查询框架扫描，生成Q{ClassName}.java|
|@JSONType|类|声明实体类属性序列化和反序列化规则|
|-|-|naming=PropertyNamingStrategy.{CamelCase,PascalCase,SnakeCase,KebabCase}，声明类序列化和反序列化命名规则，默认为 CamelCase|
|-|-|orders={"属性名"}，声明类序列化时属性的排序|
|-|-|includes={"属性名"}，声明类序列化包含的属性，配置该属性之后，只序列化数组中包含的属性|
|-|-|ignores={"属性名"}，声明类序列化忽略的属性，与 includes 相反|
|-|-|serialzeFeatures=SerializerFeature.{}，声明类序列化策略，默认会返回 get 方法当做属性返回(例：getUser() 会当作 user 属性返回)，可通过 SerializerFeature.IgnoreNonFieldGetter 忽略前面例子的规则|
|-|-|parseFeatures=Feature.{}，声明类反序列化策略|
|@JSONField|属性|声明属性序列化和反序列化规则，支持加在 get 方法头上|
|-|-|serialize = {true,false}，false：序列化时忽略该该属性，默认为 true|
|-|-|deserialize = {true,false}，false：反序列化时忽略该该属性，默认为 true|
|-|-|fromat = "yyyy-MM-dd HH:mm:ss" 声明日期序列化和反序列化格式|
|-|-|serialzeFeatures = {SerializerFeature.UseISO8601DateFormat}，声明属性序列化策略|
|-|-|parseFeatures = {Feature.AllowISO8601DateFormat}，声明属性反序列化策略|

validator 注解

|注解|位置|说明|
|---|:---:|---|
|@Valid|属性|声明实体类参数参与校验；实体类的泛型集合可以通过该注解声明级联校验|
|-|-|private @Valid Entity entity； 声明校验 entity 内部属性|
|-|-|method(@Valid Entity entity)； 声明校验 entity 内部属性|
|-|-|method(List<@Valid Entity> entities)； 声明校验集合中每个 entity 内部属性|
|@Validated|类|声明该 bean 开启注解校验，该 bean 所有方法中带有 @Valid @Min @Max 等注解的参数参与校验|
|-|-|方法注解验证需要显式声明开启：@Bean public MethodValidationPostProcessor methodValidationPostProcessor() {return new MethodValidationPostProcessor();}|
|-|方法|@Validated(groups={ISave.class})：注解在方法参数的 @Valid 前面，可以指定 @Valid 标记的实体将会采用分组校验，只校验实体中该分组标记的字段；|
|-|-|测试发现在该注解在参数上不起作用，只能在方法头部指定分组才有效；但是方法头部@Validated(groups={Default.calss,ISave.class}), 必须加上Default.calss，否则方法参数上的校验注解必须加上ISave.class才会起作用|
|@Null|属性|属性必须是 null|
|-|参数|方法参数必须是 null|
|@NotNull|属性|属性不能为 null|
|-|参数|方法参数不能为 null|
|@NotBlank|属性|属性不能为 null 或 "" ,用于String类型|
|-|参数|方法参数不能为 null 或 "" ,用于String类型|
|@NotEmpty|属性|集合不能为null或empty,用于List集合|
|-|参数|集合不能为null或empty,用于List集合|
|@Size|属性|字符串最大长度和最小长度，集合元素数量。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|-|参数|字符串最大长度和最小长度，集合元素数量。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|@Pattern|属性|字符串正则校验。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|-|参数|字符串正则校验。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|@Min|属性|数字最小值。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|-|参数|数字最小值。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|@Max|属性|数字最大值。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|-|参数|数字最大值。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|@DecimalMin|属性|数字最小值，BigDecimal。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|-|参数|数字最小值，BigDecimal。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|@DecimalMax|属性|数字最大值，BigDecimal。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|-|参数|数字最大值，BigDecimal。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|@Digits|属性|浮点数字，整数位和小数位长度。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|-|参数|浮点数字，整数位和小数位长度。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|@Negative|属性|必须是负数。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|-|参数|必须是负数。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|@NegativeOrZero|属性|必须是0或负数。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|-|参数|必须是0或负数。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|@Positive|属性|必须是正数。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|-|参数|必须是正数。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|@PositiveOrZero|属性|必须是0或正数。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|-|参数|必须是0或正数。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|@AssertTrue|属性|Boolean值必须为true。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|-|参数|Boolean值必须为true。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|@AssertFalse|属性|Boolean值必须为false。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|-|参数|Boolean值必须为false。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|@Email|属性|验证邮箱格式。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|-|参数|验证邮箱格式。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|@Past|属性|过去的时间：必须小于当前时间。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|-|参数|过去的时间：必须小于当前时间。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|@Future|属性|未来的时间：必须大于当前时间。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|
|-|参数|未来的时间：必须大于当前时间。 值为 null 时不会校验，如果需要校验 null 值，需要配合 @NotNull|

Spring 注解

|注解|位置|说明|
|---|:---:|---|
|@SpringBootApplication|类|声明 spring-boot 启动类|
|@Configuration|类|声明 spring-boot 启动自动装载配置|
|@AutoConfigureAfter|类|指定配置文件在指定的配置之后初始化|
|@Import|类|导入其他不带 @Configuration 且需要启动装载的配置|
|@EnableConfigurationProperties|类|自动加载配置文件开关|
|@EnableAspectJAutoProxy|类|AspectJ 开关|
|@EnableWebMvc|类|MVC 配置开关|
|@ConfigurationProperties|类|装载配置文件到实体类，实体类头部必须添加 @Component 注解，且必须打开 @EnableConfigurationProperties。 @ConfigurationProperties("app") app 前缀的配置都装载到该实体|
|@EnableTransactionManagement|类|事务注解开关|
|@EnableJpaRepositories|类|JPA 开关|
|@PersistenceContext|属性|声明 spring 注入 EntityManager|
|@Primary|类|声明 bean 的优先级比其他实现者高，在不指定特定的实现者时，优先使用该 bean|
|@Order|类|控制 @Configuration 配置类 或 bean 加载顺序|
|@WebFilter|类|声明过滤器及拦截规则|
|@Nullable|参数|声明方法参数允许传递空值|
|@ControllerAdvice|类|声明需要全局异常拦截|
|@ExceptionHandler|方法|声明全局拦截指定异常；@ExceptionHandler(value = {IllegalArgumentException.class})|
|@PropertySource|类|加载指定的配置文件；@PropertySource(value = "classpath:service.properties", encoding = "UTF-8")|
|@Value|属性|获取配置文件数据，@Value("${app.env}")|
|@ComponentScan|类|指定扫描包路径；@ComponentScan(basePackages = {"com.ccx"})|
|@EnableScheduling|类|定时任务开关|
|@Scheduled|方法|定时任务表达式|
|@Bean|方法|声明方法的返回值由 spring 管理生命周期|
|-|-|value="指定 bean 的名字"，不指定则使用默认的命名规则|
|-|-|destroyMethod = "方法名"； 制定 bean 销毁时必须执行的方法；一般用于销毁线程池|
|@Component|类|声明类由 spring 初始化，由 spring 管理生命周期|
|@ConditionalOnProperty|类|控制该 bean 初始化条件，@ConditionalOnProperty(value = "app.auto-task.enabled", havingValue = "true")|
|@ConditionalOnMissingBean|类|控制该 bean 初始化条件，当服务中已存在 bean 时不初始化|
|@@ConditionalOnExpression|类|控制该 bean 初始化条件，@ConditionalOnExpression("'prod'.equals('${app.env}')")|
|@Autowired|属性|声明 spring 注入 bean|
|@Resource|属性|声明 spring 注入 bean|
|@Aspect|类|声明 AOP 拦截|
|@Pointcut|方法|定义 AOP 拦截规则 @Pointcut("execution(* com.ccx..*.web..*.*(..))") public void point() {} |
|@Before|方法|声明 AOP 方法执行之前，@Before("point()")public void before(final JoinPoint joinPoint)|
|@After|方法|声明 AOP 方法执行之后返回之前，@After("point()")public void after(JoinPoint joinPoint)|
|@AfterReturning|方法|声明 AOP 方法返回之后，@AfterReturning("point()")public void afterReturn(JoinPoint joinPoint, Object result)|
|@Around|方法|声明 AOP 方法环绕，@Around("execution(* com.ccx..*.service..*.find*(..))") public Object around(final ProceedingJoinPoint joinPoint)|
|@Transactional|方法|声明方法事务隔离级别和传播机制|
|-|类|声明类所有方法事务，优先级低于方法上的声明|
|@Controller|类|声明类与前端交互|
|@RestController|类|声明类与前端交互，简化 @Controller 注解，不再需要 @ResponseBody 注解声明响应，但使用了该注解之后，所有方法将不能再使用 HttpServletResponse 控制响应|
|@RequestMapping|类|声明所有方法请求路径和请求方式|
|-|方法|声明请求路径和请求方式，请求路径会拼接 @RequestMapping 声明的路径作为前缀|
|@GetMapping|方法|声明 GET 请求及请求路径，请求路径会拼接 @RequestMapping 声明的路径作为前缀|
|@ResponseBody|方法|声明由 spring 构造响应结果|
|@PathVariable|参数|声明 ULR 路径参数|
|@RequestParam|参数|声明接收 URL 查询参数|
|-|-|value="参数名"；映射参数名|
|-|-|required={true,false}；参数是否必须|
|-|-|defaultValue=""；参数默认值|
|@PostMapping|方法|声明 POST 请及及请求路径，请求路径会拼接 @RequestMapping 声明的路径作为前缀|
|@PutMapping|方法|声明 PUT 请及及请求路径，请求路径会拼接 @RequestMapping 声明的路径作为前缀|
|@PatchMapping|方法|声明 PATCH 请及及请求路径，请求路径会拼接 @RequestMapping 声明的路径作为前缀|
|@DeleteMapping|方法|声明 DELETE 请及及请求路径，请求路径会拼接 @RequestMapping 声明的路径作为前缀|
|@RequestBody|参数|声明方法参数来自于 http body|
|@Service|类|声明服务类|

Spring Security 注解

|注解|位置|说明|
|---|:---:|---|
|@EnableWebSecurity|类|Security 开关|
|@EnableGlobalMethodSecurity|类|控制 @Secured,@PreAuthorize 是否生效的开关|
|-|-|securedEnabled = true；启用注解：@Secured；[@Secured("ROLE_USER"), @Secured("IS_AUTHENTICATED_ANONYMOUSLY")]|
|-|-|prePostEnabled = true；启用注解：@PreAuthorize；[@PreAuthorize("hasAuthority('ROLE_USER')"), @PreAuthorize("isAnonymous()"), @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")]|
|@PostAuthorize|类|声明该类所有方法需要经过自定义权限校验|
|-|方法|声明该方法需要经过自定义权限校验，@PostAuthorize("returnObject.username == principal.username or hasRole('ADMIN')")|
|@AuthenticationPrincipal|参数|获取当前上下文中监权成功的用户信息|
|@Secured|方法|方法权限认证规则，必须带 ROLE_ 前缀，且 GrantedAuthority 中的角色也必须带 ROLE_ 前缀，IS_AUTHENTICATED_ANONYMOUSLY 属于特殊权限，表示游客，没有回话的用户都会被赋予游客角色；@Secured("ROLE_USER")|
|@PreAuthorize|方法|方法权限认证规则，可以不带 ROLE_ 前缀，可以通过该注解实现权限指令，作为权限指令使用时，称谓不适合叫角色|
|-|-|@PreAuthorize("isAnonymous()")；满足游客|
|-|-|@PreAuthorize("hasAuthority('ROLE_USER')")；必须满足该角色|
|-|-|@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")；满足其中一个角色|

Spring Cache 注解

|注解|位置|说明|
|---|:---:|---|
|@EnableCaching|类|缓存开关|
|@CacheEvict|方法|删除缓存；@CacheEvict(cacheNames = "", key = "#id")|
|-|-|cacheNames="缓存模块名"|
|-|-|key="#id"，缓存 key 生成规则|
|@Cacheable|方法|缓存返回数据；@Cacheable(cacheNames = "", key = "#id")|
|-|-|cacheNames="缓存模块名"|
|-|-|key="#id"，缓存 key 生成规则|

Swagger 注解

|注解|位置|说明|
|---|:---:|---|
|@EnableSwagger2|类|Swagger 开关|
|@EnableKnife4j|类|Swagger 增强开关|
|@ApiIgnore|方法|声明 Swagger 接描时忽略该接口|
|-|参数|声明 Swagger 扫描时忽略该参数|
|@ApiModel|类|声明 Swagger 扫描实体；@ApiModel(description = "用户")|
|@ApiModelProperty|属性|声明 Swagger 扫描实体属性；@ApiModelProperty(position = 1, value = "", example = "")|
|-|-|position = 序号|
|-|-|value = "字段说明"|
|-|-|example = "参考值"|
|-|-|hidden = {true,false}，字段是否隐藏|
|@Api|类|声明 Swagger 扫描接口，@Api(tags = "用户")|
|@ApiSort|类|声明接口显示顺序|
|@ApiOperationSupport|方法|声明接口方法扩展；@ApiOperationSupport(order = 1, ignoreParameters = {""})|
|-|-|order = 1；方法显示顺序|
|-|-|ignoreParameters = {"忽略属性"}；指定调用接口忽略参数|
|-|-|params = @DynamicParameters；动态参数声明，一般用于 Map 类型参数接收|
|@ApiOperation|方法|声明接口信息；@ApiOperation(value = "说明", tags = {"标签"})|
|@ApiParam|参数|声明接口参数说明，@ApiParam(required = true, value = "", example = "") |
|-|-|required = {true,false}；是否必填|
|-|-|value = ""；参数说明|
|-|-|example = ""；参考值|
|@ApiImplicitParam|方法|声明参数信息；@ApiImplicitParam(name = "body", dataType = "TabUser", dataTypeClass = TabUser.class, required = true)|
|-|-|name = "body"；参数名|
|-|-|dataType = "TabUser"；参数数据类型|
|-|-|dataTypeClass = TabUser.class；参数数据类型实体|
|-|-|required = {true,false}；是否必填|
|@DynamicParameters|类|声明参数对象；@DynamicParameters(name = "NameVO", properties = {@DynamicParameter(name = "参数名", value = "属性说明", example = "参考值", required = true, dataTypeClass = String.class)})|
|-|-|name = "NameVO",对象显示类名|
|-|-|properties={@DynamicParameter}，对象属性集合|
|@DynamicParameter|注解|声明对象参数属性；@DynamicParameter(name = "", value = "", example = "", required = true, dataTypeClass = String.class),|
|-|-|name = " 参数名"；|
|-|-|value = "参数说明"；|
|-|-|example = "参考值"；|
|-|-|required = {true,false}；是否必填|
|-|-|dataTypeClass = String.class；参数数据类型|

自定义注解

|注解|位置|说明|
|---|:---:|---|
|@Master|类|强制方法使用主库|
|@ServiceAspect|类|声明该服务实现类将会拦截 save,saveAll,update 方法|
|-|-|id={true,false}；save,saveAll 方法是否自动清空ID，update 方法是否自动设置参数中的 ID 到更新对象中|
|-|-|uid={true,false}；save,saveAll 方法是否自动设置 uid|
|-|-|user={true,false}；save,saveAll 方法是否自动设置用户 id 到 [insertUserId,updateUserId] 字段，update 方法是否自动设置用户 id 到 updateUserId 字段|
|-|-|sync={true,false}；该 service 是否为同步镜像服务；比如将 mysql 数据以镜像形式存储到其他数据库|

其他注解

|注解|位置|说明|
|---|:---:|---|
|@Deprecated|类|标记该类已废弃不再使用|
|-|属性|标记该属性已废弃不再使用|
|-|方法|标记该方法已废弃不再使用|
|@Documented|注解|声明注解|
|@Target|注解|声明注解支持位置|
|@Retention|注解|声明注解生命周期|
