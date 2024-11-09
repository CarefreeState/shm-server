package com.macaron.homeschool;

import com.macaron.homeschool.common.constants.DateTimeConstants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
@MapperScan({"com.macaron.homeschool.model.dao.mapper"})
public class HomeSchoolManagementApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone(DateTimeConstants.TIME_ZONE));
		SpringApplication.run(HomeSchoolManagementApplication.class, args);
	}

}
