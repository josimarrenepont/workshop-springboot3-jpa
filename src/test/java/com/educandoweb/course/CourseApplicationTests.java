package com.educandoweb.course;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CourseApplication.class)
@ActiveProfiles("test")
class CourseApplicationTests {

	@Test
	void contextLoads() {
	}

}
