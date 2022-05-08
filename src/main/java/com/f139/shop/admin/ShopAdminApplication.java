package com.f139.shop.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.f139.shop.admin.mapper")
public class ShopAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopAdminApplication.class, args);
	}

}
