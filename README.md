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