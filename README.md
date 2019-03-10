# BlogManagePlatform  
## 这是一个springboot项目的脚手架,追求简洁高速可扩展。  
已完成了一些常用功能，包括:
1. 较简单的请求限流。
2. 较简单的日志配置。
3. 多功能的参数验证。
4. 较完备的spring security配置,使用jwt,无session。
5. 使用mybatisORM工具，采用通用mapper和pageHelper作为辅助。
5. 高性能的常用工具类，如对象复制，反射，json处理，日期处理，正则表达式处理，判空。
6. 具备良好API的通用Result。
7. 较全面的mybatis代码自动生成工具。
8. swagger支持。
9. 异常兜底处理。  
**对可能有不同应用场景的功能，均保证了尽可能良好的扩展性，方便接入不同实现。**  
## 项目结构:
1. 这个项目只包括了服务端API部分,前端页面部分可自行接入各种实现。
2. src文件夹为项目文件夹。src/test文件夹为测试文件存放处。src/main/resources文件夹为配置文件存放处，其中application.yml位于主目录，others文件夹存放了sql，settings文件夹存放了自定义的配置文件。
3. src/main/generator文件夹为代码生成器的存放处。这里存放了自行实现的mybatis-generator插件和基于eclipde.jdt的代码生成插件，可以简化开发中的重复操作。
4. src/main/java/frodez文件夹为项目代码存放处。 
    1. config文件夹存放了项目的配置，以及一些功能(AOP, spring security, mapper, swagger等)的实现。 
    2. controller文件夹存放了项目的controller。 
    3. dao文件夹存放了mapper,mapper.xml,数据库orm映射实体,以及其他与数据库相关的bean。 
    4. service文件夹存放了项目的主体业务逻辑实现。 
    5. util文件夹存放了工具类和常用常量。 
