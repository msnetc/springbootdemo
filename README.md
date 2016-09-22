## SpringBoot整合Mybatis示例项目

#### 前言
Spring Boot是最近几年火起来的一个开源框架，通过使用特定方式的配置快速搭建应用开发项目（Spring原来强大而且恶心的xml配置，相信不少人配置过一次就不想搞第二次吧）。

开发人员通过starter（启动器）引入所需模块，无需过分关注其中的依赖处理，Spring Boot会自动发现和组织其中的bean以及自动获取相关配置。

本示例项目介绍如何在Spring Boot项目中整合**Mybatis**模块（使用阿里 druid数据源开源项目），另外在项目中**log4j2**替换Spring Boot默认日志模块、使用MyBatis Generator插件生成Mybatis相关代码。

***

#### 进入正题
##### >>创建项目

创建一个普通的Maven webapp项目，很简单，所以...过程略...

##### >>添加Maven依赖

在pom.xml文件中添加对应的依赖，需注意的是本人使用jetty替换tomcat容器、使用log4j2替换默认的logback，所以引入spring-boot-starter-web的时候排除累tomcat、logging的starter，不需要可自行调整。具体见以下代码及注释。

在build标签内增加mybatis-generator-maven-plugin插件，用于生成Mybatis的Model、mapper等文件，程序猿偷懒专用。

*pom.xml*

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.cent</groupId>
    <artifactId>springBootDemo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.4.0.RELEASE</version>
    </parent>

    <properties>
        <org.mybatis.spring.boot.version>1.1.1</org.mybatis.spring.boot.version>
        <org.aspectj.version>1.6.11</org.aspectj.version>
        <com.alibaba.druid.version>1.0.5</com.alibaba.druid.version>
        <mysql.connector.version>5.1.38</mysql.connector.version>
        <org.mybatis.generator.version>1.3.5</org.mybatis.generator.version>
        <org.spring.framework.orm.version>3.1.0.RELEASE</org.spring.framework.orm.version>
        <com.github.pagehelper.version>4.1.6</com.github.pagehelper.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <!-- 去除tomcat starter -->
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-tomcat</artifactId>
                </exclusion>
                <!-- 去除logback starter -->
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-logging</artifactId>
                </exclusion>
                <!-- 去除jdbc starter -->
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-jdbc</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <!-- 使用jetty starter -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jetty</artifactId>
        </dependency>

        <!-- 使用actuator starter（监控和管理模块） -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- 引入mybatis -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>${org.mybatis.spring.boot.version}</version>
        </dependency>

        <!-- mybatis分页插件 -->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
            <version>${com.github.pagehelper.version}</version>
        </dependency>
        <!-- sql解析（mybatis分页插件需依赖） -->
        <dependency>
            <groupId>com.github.jsqlparser</groupId>
            <artifactId>jsqlparser</artifactId>
            <version>0.9.6</version>
        </dependency>

        <!-- log4j2 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-log4j2</artifactId>
        </dependency>

        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>${org.aspectj.version}</version>
        </dependency>

        <!-- mysql 驱动 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql.connector.version}</version>
        </dependency>

        <!-- 数据库连接池（阿里druid开源项目） -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>${com.alibaba.druid.version}</version>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <!-- mybatis代码生成器 -->
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>${org.mybatis.generator.version}</version>
                <configuration>
                    <verbose>true</verbose>
                    <overwrite>true</overwrite>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```


##### >>数据源配置

1. 在src/main/resource路径下添加jdbc.properties文件，保存数据源相关配置。

*jdbc.properties*

```
demo.jdbc.url=jdbc:mysql://localhost:3306/spring_boot_demo
demo.jdbc.username=root
demo.jdbc.password=123456

demo.jdbc.initialSize=1
demo.jdbc.minIdle=1
demo.jdbc.maxActive=20

demo.jdbc.maxWait=60000
demo.jdbc.timeBetweenEvictionRunsMillis=60000
```

2. 增加对应的DataSourceProperties类

*DataSourceProperties.class*

```
package org.cent.springBootDemo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据源配置类
 * Created by cent on 2016/9/2.
 */
@ConfigurationProperties(locations = "classpath:jdbc.properties", prefix = "demo.jdbc")
public class DataSourceProperties {

    private String url;

    private String username;

    private String password;

    private Integer initialSize;

    private Integer minIdle;

    private Integer maxActive;

    private Long maxWait;

    private Long timeBetweenEvictionRunsMillis;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public Long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(Long maxWait) {
        this.maxWait = maxWait;
    }

    public Long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(Long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }
}


```


##### >>Mybatis配置

1. 新建MybatisConfig类，用于配置Mybatis相关Bean（SqlSessionFactory、SqlSessionTemplate等）。

*MybatisConfig.class*

```
package org.cent.springBootDemo.configs;


import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import java.util.Properties;

/**
 * Mybatis主配置
 * Created by cent on 2016/9/20.
 */
@Configuration
@EnableTransactionManagement//启用事务控制
@AutoConfigureAfter(DatasourceConfig.class)
@Import(DatasourceConfig.class)//设置为在数据源之后初始化，防止数据源为null导致启动报错
public class MybatisConfig implements TransactionManagementConfigurer {

    /**
     * 数据源对象
     */
    @Autowired
    private DruidDataSource dataSource;

    /**
     * 数据库会话工厂bean
     * @return
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("org.cent.springBootDemo.entity");
        org.apache.ibatis.session.Configuration configuration=new org.apache.ibatis.session.Configuration();
        configuration.setLogImpl(Log4j2Impl.class);
        bean.setConfiguration(configuration);

        //配置mybatis 分页插件
        PageHelper pageHelper=new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("dialect","mysql");
        properties.setProperty("params","pageNum=start;pageSize=limit;pageSizeZero=zero;reasonable=heli;count=contsql");
        pageHelper.setProperties(properties);

        try {
            bean.setPlugins(new Interceptor[]{pageHelper});
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 数据库会话模板bean
     * @param sqlSessionFactory
     * @return
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 事务管理器配置
     * @return
     */
    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

}

```

2. 新建MapperScannerConfiguierConfig.class 文件，用于配置Mybatis的Mapper扫描器。

```
package org.cent.springBootDemo.configs;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Mybatis Mapper扫描配置
 * Created by cent on 2016/9/20.
 */
@Configuration
@AutoConfigureAfter(MybatisConfig.class)
public class MapperScannerConfiguierConfig {

    /**
     * Mybatis mapper扫描器
     * @return
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("org.cent.springBootDemo.mapper");
        return mapperScannerConfigurer;
    }
}

```

##### >>Spring Boot启动入口

创建Spring Boot启动入口Application类，这里只有启动的main方法，所以的配置都在其他的config类配置。

```
package org.cent.springBootDemo.entrance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Spring Boot启动入口
 * Created by cent on 2016/9/2.
 */
@EnableWebMvc
@Configuration
@SpringBootApplication(scanBasePackages = {"org.cent.springBootDemo"})
public class Application extends WebMvcConfigurerAdapter {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}

```


##### >>MyBatis generator插件配置

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">


<generatorConfiguration>
    <!--数据库驱动jar -->
    <classPathEntry location="C:\Users\user\.m2\repository\mysql\mysql-connector-java\5.1.38\mysql-connector-java-5.1.38.jar" />

    <context id="DB2Tables" targetRuntime="MyBatis3">
        <!--去除注释  -->
        <commentGenerator>
            <property name="suppressAllComments" value="true" />
        </commentGenerator>

        <!--数据库连接 -->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/spring_boot_demo"
                        userId="root"
                        password="123456">
        </jdbcConnection>
        <!--默认false
           Java type resolver will always use java.math.BigDecimal if the database column is of type DECIMAL or NUMERIC.
         -->
        <javaTypeResolver >
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>

        <!--生成实体类 指定包名 以及生成的地址 （可以自定义地址，但是路径不存在不会自动创建  使用Maven生成在target目录下，会自动创建） -->
        <javaModelGenerator targetPackage="org.cent.springBootDemo.entity" targetProject="src\main\java">
            <property name="enableSubPackages" value="false" />
            <property name="trimStrings" value="true" />
        </javaModelGenerator>
        <!--生成SQLMAP文件 -->
        <sqlMapGenerator targetPackage="mapper"  targetProject="src\main\resources">
            <property name="enableSubPackages" value="false" />
        </sqlMapGenerator>

        <!--生成Dao文件 可以配置 type="XMLMAPPER"生成xml的dao实现  context id="DB2Tables" 修改targetRuntime="MyBatis3"  -->
        <javaClientGenerator type="ANNOTATEDMAPPER" targetPackage="org.cent.springBootDemo.mapper"  targetProject="src\main\java">
            <property name="enableSubPackages" value="false" />
        </javaClientGenerator>

        <!--对应数据库表 mysql可以加入主键自增 字段命名 忽略某字段等-->
        <table tableName="sys_user" domainObjectName="User" >
        </table>

    </context>
</generatorConfiguration>
```

##### >>生成实体类及Mapper接口

我用的是IDEA IntelliJ，使用Mybatis generator插件的方式如下图，其他开发工具类似。

![image](http://note.youdao.com/yws/public/resource/65bd991a23087de0045eec6b5eacbdf3/xmlnote/8BECB4FD79DB48C18169D3C8AA62A3F9/28639)


##### >>开发Service
1. 定义UserService接口

*UserService.class*

```
package org.cent.springBootDemo.service;

import com.github.pagehelper.PageInfo;
import org.cent.springBootDemo.entity.User;

/**
 * User服务接口
 * Created by cent on 2016/9/20.
 */
public interface UserService {
    /**
     * 根据ID查找
     * @return
     * @throws Exception
     */
    public User queryById(Integer id) throws Exception;

    /**
     * 获取分页数据
     * @param start
     * @param limit
     * @return
     * @throws Exception
     */
    public PageInfo<User> selectPage(int start,int limit) throws Exception;
}

```

2. UserService实现类

*UserServiceImpl.class*

```
package org.cent.springBootDemo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.cent.springBootDemo.entity.User;
import org.cent.springBootDemo.entity.UserExample;
import org.cent.springBootDemo.mapper.UserMapper;
import org.cent.springBootDemo.service.UserService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * User服务接口实现类
 * Created by cent on 2016/9/20.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public User queryById(Integer id) throws Exception {
        UserMapper userMapper = sqlSessionTemplate.getMapper(UserMapper.class);
        return userMapper.selectByPrimaryKey(id);
    }

    public PageInfo<User> selectPage(int start,int limit) throws Exception {
        UserMapper userMapper = sqlSessionTemplate.getMapper(UserMapper.class);
        PageHelper.startPage(start,limit);
        UserExample ue = new UserExample();
        PageInfo<User> users = new PageInfo<User>(userMapper.selectByExample(ue));
        return users;
    }
}

```

##### >>开发Controller

定义UserController类。

*UserController.class*

```
package org.cent.springBootDemo.controller;


import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cent.springBootDemo.constants.PageConstants;
import org.cent.springBootDemo.entity.User;
import org.cent.springBootDemo.properties.DataSourceProperties;
import org.cent.springBootDemo.service.UserService;
import org.cent.springBootDemo.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * User Demo Controller
 * Created by cent on 2016/9/2.
 */
@RestController
@RequestMapping(value="/user")
public class UserController extends BaseController {

    Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private UserService userService;

    /**
     * 根据用户ID获取用户信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public User user(@PathVariable("id") Integer id) {
        try {
            User user = userService.queryById(id);
            return user;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new User();
        }
    }

    /**
     * 获取用户列表（分页）
     * 请求参数：start-页数、limit-每页记录数
     * @param request
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public PageInfo<User> user(HttpServletRequest request) {
        try {
            Map<String, String> params = getRequestParams(request);
            Integer start = StringUtil.isBlank(params.get("start")) ? PageConstants.DEFAULT_START_PAGE : Integer.valueOf(params.get("start"));
            Integer limit = StringUtil.isBlank(params.get("limit")) ? PageConstants.DEFAULT_PAGE_LIMIT : Integer.valueOf(params.get("limit"));

            PageInfo<User> users = userService.selectPage(start, limit);
            return users;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new PageInfo<User>();
        }

    }
}

```

##### >>Log4j2配置（使用Spring Boot默认日志模块可忽略）

1. 新建log4j2.properties文件，配置appender和logger，以下配置增加单独输出Mybatis Mapper的Debug日志，由于查看Mybatis的语句执行记录。

*log4j2.properties*

```
appender.console.type = Console
appender.console.name = STDOUT
appender.console.target = SYSTEM_OUT
appender.console.layout.type = PatternLayout
appender.console.layout.pattern = %-d{yyyy-MM-dd HH:mm:ss} [ %p ] [ %c ] %m%n

appender.rollingFileAppender.type = RollingFile
appender.rollingFileAppender.name = rollingFile
appender.rollingFileAppender.fileName = /spider/logs/all.log
appender.rollingFileAppender.filePattern = /spider/logs/info_%d{yyyy-MM-dd}_%i.log
appender.rollingFileAppender.layout.type = PatternLayout
appender.rollingFileAppender.layout.pattern = %-d{yyyy-MM-dd HH:mm:ss} [ %p ] [ %c ] %m%n
appender.rollingFileAppender.policies.type = Policies
appender.rollingFileAppender.policies.time.type = TimeBasedTriggeringPolicy
appender.rollingFileAppender.policies.time.interval = 1
appender.rollingFileAppender.policies.time.modulate = true
appender.rollingFileAppender.policies.size.type = SizeBasedTriggeringPolicy
appender.rollingFileAppender.policies.size.size=20M
appender.rollingFileAppender.strategy.type = DefaultRolloverStrategy
appender.rollingFileAppender.strategy.max = 100

appenders =console,rollingFileAppender

#主logger
rootLogger.level = info
#rootLogger.appenderRefs = console,I,E
rootLogger.appenderRefs =${appenders}
rootLogger.appenderRef.console.ref = STDOUT
rootLogger.appenderRef.rollingFileAppender.ref = rollingFile

#输出mybatis的SQL语句执行日志
logger.sql.name=org.cent.springBootDemo.mapper
logger.sql.level = debug
logger.sql.additivity=false
logger.sql.appenderRefs = console
logger.sql.appenderRef.console.ref = STDOUT
```

2. 更改Spring Boot的日志配置路径，创建application.properties文件，在里面更改日志配置。

*application.properties*

```
#使用log4j2的配置
logging.config=classpath:log4j2.properties
```

##### >>整体项目结构

![image](http://note.youdao.com/yws/public/resource/65bd991a23087de0045eec6b5eacbdf3/xmlnote/529C76B632AF4CACA04D314824356968/28702)

##### >>启动&测试
1. 运行入口main方法，启动Spring Boot。

![image](http://note.youdao.com/yws/public/resource/65bd991a23087de0045eec6b5eacbdf3/xmlnote/714BF433954D4EBF8107E606735DD56C/28668)

2. http访问测试

![image](http://note.youdao.com/yws/public/resource/65bd991a23087de0045eec6b5eacbdf3/xmlnote/A83673C90F92485EAA734C2F69FFB1F8/28670)

***

#### 附录
- 示例数据库初始化脚本
 

```
CREATE DATABASE `spring_boot_demo` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

CREATE TABLE `sys_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_CODE` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `USER_NAME` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `REMARK` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
```

***


#### 示例源码
示例项目源码地址：[https://code.aliyun.com/cent/spring-boot-demo.git](https://code.aliyun.com/cent/spring-boot-demo.git)