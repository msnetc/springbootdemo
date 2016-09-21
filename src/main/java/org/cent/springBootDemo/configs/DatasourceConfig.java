package org.cent.springBootDemo.configs;

import com.alibaba.druid.pool.DruidDataSource;
import org.cent.springBootDemo.properties.DataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 数据源配置
 * Created by cent on 2016/9/19.
 */
@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class DatasourceConfig {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    /**
     * 数据源
     * @return
     */
    @Bean
    public DruidDataSource dataSource() {
        DruidDataSource dataSource=new DruidDataSource();
        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());
        dataSource.setInitialSize(dataSourceProperties.getInitialSize());
        dataSource.setMinIdle(dataSourceProperties.getMinIdle());
        dataSource.setMaxActive(dataSourceProperties.getMaxActive());
        dataSource.setMaxWait(dataSourceProperties.getMaxWait());
        dataSource.setTimeBetweenEvictionRunsMillis(dataSourceProperties.getTimeBetweenEvictionRunsMillis());
        return dataSource;
    }
}
