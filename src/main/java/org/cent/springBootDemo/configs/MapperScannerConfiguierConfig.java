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
