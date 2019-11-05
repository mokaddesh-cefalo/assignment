package com.cefalo.assignment;

import org.assertj.core.error.ShouldBeAfterYear;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class AssignmentApplicationTests {

	@Test
	void contextLoads() {
		String dateInString = "05 january 2018";
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMMM yyyy");
		Date date = DateTime.parse(dateInString, formatter).toDate();
		System.out.println();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(format.format(date));
	}

}
