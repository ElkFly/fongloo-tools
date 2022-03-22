## tools-xss
模块定位为防跨站脚本攻击（XSS），通过对用户在页面输入的 HTML / CSS / JavaScript 等内容进行检验和清理，确保输入内容符合应用规范，保障系统的安全。

### XSS介绍

XSS：跨站脚本攻击(Cross Site Scripting)，为不和 CSS混淆，故将跨站脚本攻击缩写为XSS。XSS是指恶意攻击者往Web页面里插入恶意Script代码，当用户浏览该页时，嵌入其中Web里面的Script代码会被执行，从而达到恶意攻击用户的目的。有点类似于sql注入。

XSS攻击原理：

HTML是一种超文本标记语言，通过将一些字符特殊地对待来区别文本和标记，例如，小于符号（<）被看作是HTML标签的开始，<title>与</title>之间的字符是页面的标题等等。当动态页面中插入的内容含有这些特殊字符时，用户浏览器会将其误认为是插入了HTML标签，当这些HTML标签引入了一段JavaScript脚本时，这些脚本程序就将会在用户浏览器中执行。所以，当这些特殊字符不能被动态页面检查或检查出现失误时，就将会产生XSS漏洞。

### AntiSamy介绍

AntiSamy是OWASP的一个开源项目，通过对用户输入的 HTML / CSS / JavaScript 等内容进行检验和清理，确保输入符合应用规范。AntiSamy被广泛应用于Web服务对存储型和反射型XSS的防御中。

### 如何使用
1. 引入依赖
    ```xml
        <dependency>
            <groupId>com.fongloo</groupId>
            <artifactId>fongloo-tools</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    ```
   或者按需引入
    ```xml
        <dependency>
            <groupId>com.fongloo</groupId>
            <artifactId>tools-xss</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    ```
2. 直接开始使用，在所有参数进行传递时会自动过滤XSS。


## tools-validator

pd-tools-validator模块定位为后端表单数据校验，其他模块可以直接引入tools-validator的maven坐标就可以使用其提供的表单校验功能。pd-tools-validator底层基于hibernate-validator实现。

### hibernate-validator介绍

早期的网站，用户输入一个邮箱地址，需要将邮箱地址发送到服务端，服务端进行校验，校验成功后，给前端一个响应。

有了JavaScript后，校验工作可以放在前端去执行。那么为什么还需要服务端校验呢？ 因为前端传来的数据不可信。前端很容易获取到后端的接口，如果有人直接调用接口，就可能会出现非法数据，所以服务端也要数据校验。

总的来说：

- [ ] 前端校验：主要是提高用户体验
- [ ] 后端校验：主要是保证数据安全可靠

校验参数基本上是一个体力活，而且冗余代码繁多，也影响代码的可读性，我们需要一个比较优雅的方式来解决这个问题。Hibernate Validator 框架刚好解决了这个问题，可以以很优雅的方式实现参数的校验，让业务代码和校验逻辑分开,不再编写重复的校验逻辑。

hibernate-validator优势：

- [ ] 验证逻辑与业务逻辑之间进行了分离，降低了程序耦合度
- [ ] 统一且规范的验证方式，无需你再次编写重复的验证代码
- [ ] 你将更专注于你的业务，将这些繁琐的事情统统丢在一边

hibernate-validator的maven坐标：

~~~xml
<dependency>
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-validator</artifactId>
      <version>6.2.2.Final</version>
</dependency>
~~~
`6.2.1.Final 后版本解决了log4j的问题`

### hibernate-validator常用注解

hibernate-validator提供的校验方式为在类的属性上加入相应的注解来达到校验的目的。hibernate-validator提供的用于校验的注解如下：

| 注解                      | 说明                                                         |
| ------------------------- | ------------------------------------------------------------ |
| @AssertTrue               | 用于boolean字段，该字段只能为true                            |
| @AssertFalse              | 用于boolean字段，该字段只能为false                           |
| @CreditCardNumber         | 对信用卡号进行一个大致的验证                                 |
| @DecimalMax               | 只能小于或等于该值                                           |
| @DecimalMin               | 只能大于或等于该值                                           |
| @Email                    | 检查是否是一个有效的email地址                                |
| @Future                   | 检查该字段的日期是否是属于将来的日期                         |
| @Length(min=,max=)        | 检查所属的字段的长度是否在min和max之间,只能用于字符串        |
| @Max                      | 该字段的值只能小于或等于该值                                 |
| @Min                      | 该字段的值只能大于或等于该值                                 |
| @NotNull                  | 不能为null                                                   |
| @NotBlank                 | 不能为空，检查时会将空格忽略                                 |
| @NotEmpty                 | 不能为空，这里的空是指空字符串                               |
| @Pattern(regex=)          | 被注释的元素必须符合指定的正则表达式                         |
| @URL(protocol=,host,port) | 检查是否是一个有效的URL，如果提供了protocol，host等，则该URL还需满足提供的条件 |

### 如何使用
1. 引入依赖
    ```xml
        <dependency>
            <groupId>com.fongloo</groupId>
            <artifactId>fongloo-tools</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    ```
   或者按需引入
    ```xml
        <dependency>
            <groupId>com.fongloo</groupId>
            <artifactId>tools-validator</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    ```
2. 在类上添加`EnableValidatorFastFail`注解，可以"快速失败模式"
   > 默认是某个参数校验不通过的情况下，还会继续校验后面的参数。
   > 开启后"快速失败模式"后，第一个参数验证不通过后直接异常。

3. 全局异常配置, 需要监听 `ConstraintViolationException`和`BindException`
   示例代码:
~~~java
/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class ExceptionConfiguration {
    @ExceptionHandler({ConstraintViolationException.class,BindException.class})
    public String validateException(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        String msg = null;
        if(ex instanceof ConstraintViolationException){
            ConstraintViolationException constraintViolationException = 
                (ConstraintViolationException)ex;
            Set<ConstraintViolation<?>> violations = 
                constraintViolationException.getConstraintViolations();
            ConstraintViolation<?> next = violations.iterator().next();
            msg = next.getMessage();
        }else if(ex instanceof BindException){
            BindException bindException = (BindException)ex;
            msg = bindException.getBindingResult().getFieldError().getDefaultMessage();
        }
        return msg;
    }
}
~~~

------

## tools-log

tools-log模块定位为日志模块，本质也是一个starter。提供的日志功能主要有两个方面：

1、通过logback框架可以在控制台或者日志文件记录日志信息

2、拦截用户请求，将操作日志进行处理，列如：你可以保存到数据库，也可以进行用户行为分析。

### 如何使用
1. 在resources下logback配置文件logback-base.xml和logback-spring.xml

`logback-base.xml`
~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<included>
    <contextName>logback</contextName>
    <!--
		name的值是变量的名称，value的值时变量定义的值
		定义变量后，可以使“${}”来使用变量
	-->
    <property name="log.path" value="d:\\logs" />

    <!-- 彩色日志 -->
    <!-- 彩色日志依赖的渲染类 -->
    <conversionRule
            conversionWord="clr"
            converterClass="org.springframework.boot.logging.logback.ColorConverter" />
    <conversionRule
            conversionWord="wex" converterClass="org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter" />
    <conversionRule conversionWord="wEx" converterClass="org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter" />
    <!-- 彩色日志格式 -->
    <property name="CONSOLE_LOG_PATTERN" value="${CONSOLE_LOG_PATTERN:-%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>

    <!--输出到控制台-->
    <appender name="LOG_CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <Pattern>${CONSOLE_LOG_PATTERN}</Pattern>
            <!-- 设置字符集 -->
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!--输出到文件-->
    <appender name="LOG_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 正在记录的日志文件的路径及文件名 -->
        <file>${log.path}/logback.log</file>
        <!--日志文件输出格式-->
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
        <!-- 日志记录器的滚动策略，按日期，按大小记录 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 每天日志归档路径以及格式 -->
            <fileNamePattern>${log.path}/info/log-info-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>100MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <!--日志文件保留天数-->
            <maxHistory>15</maxHistory>
        </rollingPolicy>
    </appender>
</included>
~~~

`logback-spring.xml`

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!--引入其他配置文件-->
    <include resource="logback-base.xml" />

    <!--开发环境-->
    <springProfile name="dev">
        <logger name="com.fongloo" additivity="false" level="debug">
            <appender-ref ref="LOG_CONSOLE"/>
        </logger>
    </springProfile>
    <!--生产环境-->
    <springProfile name="pro">
        <logger name="com.fongloo" additivity="false" level="info">
            <appender-ref ref="LOG_FILE"/>
        </logger>
    </springProfile>

    <root level="info">
        <appender-ref ref="LOG_CONSOLE" />
        <appender-ref ref="LOG_FILE" />
    </root>
</configuration>
~~~

2. 在 `application.yml`中加入如下配置
~~~yaml
log:
    enabled: true
logging:
  #在Spring Boot项目中默认加载类路径下的logback-spring.xml文件
  config: classpath:logback-spring.xml
spring:
  profiles:
    # 生产环境
    active: dev 
~~~

3. 编写Controller时加上`@SysLog`注解

~~~java
@RestController
@RequestMapping("/test")
@Api(tags = "测试")
public class TestController {
    
    @SysLog("分页查询用户")//记录操作日志
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码",
                    required = true, type = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数",
                    required = true, type = "Integer"),
    })
    @ApiOperation(value = "分页查询用户信息")
    @GetMapping(value = "page/{pageNum}/{pageSize}")
    public String findByPage(@PathVariable Integer pageNum,
                             @PathVariable Integer pageSize) {
        return "OK";
    }
}
~~~

4. 获取到日志实体类
```java
   @Configuration
   public class TestListener {
       @Bean
       @ConditionalOnMissingBean
       public SysLogListener sysLogListener() {
           SysLogListener sysLogListener = new SysLogListener(optLogDTO -> {
               System.out.println(optLogDTO);
               
              // TODO: 处理日志 optLogDTO
              
           });
           return sysLogListener;
       }
   }
```
或者
```java
   @Component
   public class TestListener implements ApplicationListener<SysLogEvent> {
       @Override
       public void onApplicationEvent(SysLogEvent event) {
           OptLogDTO optLogDTO= (OptLogDTO)event.getSource();
           System.out.println(optLogDTO);
           
           // TODO: 处理日志 optLogDTO
       }
   }
```

---

## tools-jwt

tools-jwt模块的定位是对于jwt令牌相关操作进行封装，为认证、鉴权提供支撑。

提供的功能：生成jwt token、解析jwt token

### JWT介绍

JWT全称为JSON Web Token，是目前最流行的跨域身份验证解决方案。JWT是为了在网络应用环境间传递声明而制定的一种基于JSON的开放标准。

JWT特别适用于分布式站点的单点登录（SSO）场景。JWT的声明一般被用来在身份提供者和服务提供者间传递被认证的用户身份信息，以便于从资源服务器获取资源，也可被加密。

###的数据结构

JWT其实就是一个很长的字符串，字符之间通过"."分隔符分为三个子串，各字串之间没有换行符。每一个子串表示了一个功能块，总共有三个部分：**JWT头(header)**、**有效载荷(payload)**、**签名(signature)**，如下图所示：


#### JWT头

JWT头是一个描述JWT元数据的JSON对象，通常如下所示：

~~~json
{"alg": "HS256","typ": "JWT"}
~~~

alg：表示签名使用的算法，默认为HMAC SHA256（写为HS256）

typ：表示令牌的类型，JWT令牌统一写为JWT

最后，使用Base64 URL算法将上述JSON对象转换为字符串

#### 有效载荷

有效载荷，是JWT的主体内容部分，也是一个JSON对象，包含需要传递的数据。

有效载荷部分规定有如下七个默认字段供选择：

~~~makefile
iss：发行人
exp：到期时间
sub：主题
aud：用户
nbf：在此之前不可用
iat：发布时间
jti：JWT ID用于标识该JWT
~~~

除以上默认字段外，还可以自定义私有字段。

最后，同样使用Base64 URL算法将有效载荷部分JSON对象转换为字符串

#### 签名

签名实际上是一个加密的过程，是对上面两部分数据通过指定的算法生成哈希，以确保数据不会被篡改。

首先需要指定一个密码（secret），该密码仅仅保存在服务器中，并且不能向用户公开。然后使用JWT头中指定的签名算法（默认情况下为HMAC SHA256），根据以下公式生成签名哈希：

~~~dockerfile
HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload),secret)
~~~

在计算出签名哈希后，JWT头，有效载荷和签名哈希的三个部分组合成一个字符串，每个部分用"."分隔，就构成整个JWT对象。

### JWT签名算法

JWT签名算法中，一般有两个选择：HS256和RS256。

HS256 (带有 SHA-256 的 HMAC )是一种对称加密算法, 双方之间仅共享一个密钥。由于使用相同的密钥生成签名和验证签名, 因此必须注意确保密钥不被泄密。

RS256 (采用SHA-256 的 RSA 签名) 是一种非对称加密算法, 它使用公共/私钥对: JWT的提供方采用私钥生成签名, JWT 的使用方获取公钥以验证签名。

### tools-jwt使用

tools-jwt底层是基于`jjwt`进行`jwt令牌`的生成和解析的。为了方便使用，在tools-jwt模块中封装了两个工具类：`JwtTokenServerUtils`和`JwtTokenClientUtils`。

`JwtTokenServerUtils`主要是提供给权限服务的，类中包含`生成jwt`和`解析jwt`两个方法

`JwtTokenClientUtils`主要是提供给网关服务的，类中只有一个`解析jwt`的方法

需要注意的是tools-jwt并不是starter，所以如果只是在项目中引入他的maven坐标并不能直接使用其提供的工具类。需要在启动类上加入tools-jwt模块中定义的注解`@EnableAuthServer`或者`@EnableAuthClient`。

tools-jwt使用的签名算法为`RS256`，需要我们自己的应用来提供一对公钥和私钥，然后在`application.yml`中进行配置即可。

1. `tools-jwt/src/test/java/RsaKeyHelperTest.java` 生成自己的`pri.key`和`pub.key`
2. `tools-jwt/src/test/java/JwtHelperTest.java` 测试是否正常
3. 复制到自己的项目的`resources`目录下
4. 在自己的项目`application.yml`配置文件中添加
   ```yml
    server:
      port: 8080
   # JWT相关配置
    authentication:
      user:
        expire: 3600 #令牌失效时间
        priKey: keys/pri.key #私钥路径
        pubKey: keys/pub.key #公钥路径
   ```
5. 为客户端生成和解析`jwt令牌`
   ```java
   @RestController
   @RequestMapping("/test")
   public class UserController {
   
   @Autowired
   private JwtTokenServerUtils jwtTokenServerUtils;
   
       //用户登录功能，如果登录成功则签发jwt令牌给客户端
       @GetMapping("/login")
       public Token login(){
           String userName = "admin";
           String password = "CUAdmin";
           //查询数据库进行用户名密码校验...
   
           //如果校验通过，则为客户端生成jwt令牌
           JwtUserInfo jwtUserInfo = new JwtUserInfo();
           jwtUserInfo.setName(userName);
           jwtUserInfo.setOrgId(10L);
           jwtUserInfo.setUserId(1L);
           jwtUserInfo.setAccount(userName);
           jwtUserInfo.setStationId(20L);
           Token token = jwtTokenServerUtils.generateUserToken(jwtUserInfo, null);
   
           //实际应该是在过滤器中进行jwt令牌的解析
           JwtUserInfo userInfo = jwtTokenServerUtils.getUserInfo(token.getToken());
           System.out.println(userInfo);
           return token;
       }
   }
   ```
6. 为需要生成`jwt令牌`的服务启动类上添加`@EnableAuthServer`注解
   ```java
   @SpringBootApplication
   @EnableAuthServer //启用jwt服务端认证功能
   public class MyJwtApplication {
       public static void main(String[] args) {
           SpringApplication.run(MyJwtApplication.class,args);
       }
   }
   ```
   >`@EnableAuthServer`注解包含生成和解析jwt的方法