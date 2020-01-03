![Version 0.1-alpha](https://img.shields.io/github/tag-pre/Frodez/BlogManagePlatform.svg)
[![License](https://img.shields.io/badge/license-apache-blue.svg)](https://github.com/Frodez/BlogManagePlatform/blob/master/LICENSE)
[![Build Status](https://travis-ci.com/Frodez/BlogManagePlatform.svg)](https://travis-ci.org/Frodez/BlogManagePlatform)
[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
# BlogManagePlatform  
## 这是一个springboot项目的脚手架,追求简洁高速可扩展。  

## 项目要求:
1. JDK11+
2. Mysql8+

## 功能实现:
已完成了一些常用功能，包括:
1. https和http2支持。
2. 较简单的请求限流。
3. 较简单的日志配置。
4. 多功能的参数验证。
5. 较完备的spring security配置,使用jwt,无session。
6. 使用mybatisORM工具，采用通用mapper和pageHelper作为辅助。
7. 高性能的常用工具类，如对象复制，反射，json处理，日期处理，正则表达式处理，判空。
8. 具备良好API的通用Result。
9. 较全面的mybatis代码自动生成工具。
10. swagger支持(这里使用了自定义插件,根据约定简化了部分代码编写,详情参见swagger包下package-info.java)。
11. 异常兜底处理。  
12. 加载期代码检查。  
13. mybatis插件。  

**对可能有不同应用场景的功能，均保证了尽可能良好的扩展性，方便接入不同实现。**  

##功能特色:
1. 简单的业务逻辑编写。  
和常见的复制粘贴一把梭，或者稍微高级些的代码生成器总揽全局不同，本项目使用代码生成器对基本的model,mapper(和对应xml),param进行了生成，但另外还基于一系列最基础的约定，使用注解进行额外的配置，尽可能简化了代码编写。
这种方式的最大好处在于，首先严格约束了类型，使得编码错误尽量少；同时由于动态解析，如果代码变动，需要做的修改非常小，不至于需要重新生成。
	1.1. 比如日常使用时需要用到hibernate-validator验证，经常需要写验证用的代码逻辑。在本项目中，可以使用注解，一键搞定验证逻辑，而且错误信息人性化。  
	另外，项目中还扩展了一些验证注解，增强了对枚举——基本数据类型转换的支持，增强了正则表达式验证，增强了日期验证。  
	最后，还提供了对项目代码本身的校验，依据于常见的使用规则，尽可能避免漏写注解，错写注解等情况的发生。  
	1.2. 比如日常使用常见的请求限流和日志功能，只需一个注解，便可搞定。  
	1.3. 比如经常用到的swagger，在swagger自定义插件的加持下，可以用已有的一些必要注解来辅助补充swagger信息。对于不需要深入使用swagger各类功能的人而言，可以减少很多swagger注解的使用——  
	举例来说，一个实体类如果有了@ApiModel注解，那么插件会将该注解的信息自动补充到接口上，不再需要在接口的方法参数上增加@ApiParam注解；又如常用的@ApiOperation注解，  
	如果在@RequestMapping,@GetMapping此类注解中配置了name属性，即可替代@ApiOperation注解最基本的功能。当然，这些swagger自定义插件都可以选择性开启或者关闭，一键配置。  
	1.4. 比如对mybatis插件tk-mybatis的再度增强，以及自定义返回数据处理，可以在orm的过程中，把数据从list映射为map，pair等等类型，摆脱繁琐的遍历赋值创建对象。    
2. 强大的工具类  
在业务中常见的一种需求是将一个类型的实体转换成另一个类型的实体。如果每个字段都需要自己去编写，那真的费时费力而且也容易出错。但是本项目提供了不同实体转换的工具类——而且比常见工具类更快更高效。  
如果你遇到了json和实体或者数据类型的转换，也不用担心。本项目基于jackson的工具类实现，涵盖了大部分常见功能，且速度飞快。  
如果你遇到了判空，日期，字符串，BigDecimal计算之类的问题，项目中也有对应的工具类可以帮助你。  
如果你需要使用反射或者是spring的一些基础功能，项目中也有反射工具类和对spring功能的封装。  
在返回信息时，良好包装的Result可以用简单且安全的方式，以较少的代码来实现返回。如果要取出返回信息也很方便，而且安全。  
当然，工具类并不是越多越好。项目中专门封装的工具类，基本是在项目依赖中无法找到的功能集合。另外有很多功能，都可以在项目依赖中找到——比如guava。  

## 项目结构:
1. 这个项目只包括了服务端API部分,前端页面部分可自行接入各种实现。
2. src文件夹为项目文件夹。src/test文件夹为测试文件存放处。src/main/resources文件夹为配置文件存放处，其中application.yml位于主目录，others文件夹存放了sql，settings文件夹存放了自定义的配置文件。
3. src/main/generator文件夹为代码生成器的存放处。这里存放了自行实现的mybatis-generator插件和基于eclipde.jdt的代码生成插件，可以简化开发中的重复操作。
4. src/main/java/frodez文件夹为项目代码存放处。  
	4.1. config文件夹存放了项目的配置，以及一些功能(AOP, spring security, mapper, swagger等)的实现。  
	4.2. controller文件夹存放了项目的controller。   
	4.3. dao文件夹存放了mapper,mapper.xml,数据库orm映射实体,以及其他与数据库相关的bean。  
	4.4. service文件夹存放了项目的主体业务逻辑实现。  
	4.5. util文件夹存放了工具类和常用常量。  

**由于使用缓存的缘故，建议不要使用springboot热部署工具,否则可能带来一些奇怪的问题。** 
