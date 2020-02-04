package org.yidan.springbootdemo.codegen;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yidan.coco.settings.JdbcProperty;

@Configuration
public class JdbcPropertyConfig {
    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${db.master.url}")
    private String url;
    @Value("${db.master.username}")
    private String username;
    @Value("${db.master.password}")
    private String password;

    @Bean
    public JdbcProperty jdbcProperty(){
        return new JdbcProperty(driver, url, username, password);
    }
}
