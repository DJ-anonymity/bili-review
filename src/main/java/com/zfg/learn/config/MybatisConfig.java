package com.zfg.learn.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(value ="com.zfg.learn.dao")
public class MybatisConfig {
}
