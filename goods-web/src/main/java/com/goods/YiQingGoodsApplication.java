package com.goods;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;


@SpringBootApplication
@EnableTransactionManagement  //开启事务管理
@MapperScan("com.goods.*.mapper") //扫描mapper
@Import(FdfsClientConfig.class)
public class YiQingGoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(YiQingGoodsApplication.class,args);
    }
}
