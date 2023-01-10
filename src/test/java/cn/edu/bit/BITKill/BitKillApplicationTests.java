package cn.edu.bit.BITKill;

import cn.edu.bit.BITKill.model.Drug;
import cn.edu.bit.BITKill.model.Game;
import cn.edu.bit.BITKill.model.GameState;
import cn.edu.bit.BITKill.model.params.UserParam;
import cn.edu.bit.BITKill.repo.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class BitKillApplicationTests {

	@Autowired
	private UserRepository user_repository;

	@Test
	void repoTest()
	{
		List<UserParam> test1 = user_repository.selectAll();
		boolean test2_1 = user_repository.insert(new UserParam("aaa", "bbb"));
		boolean test2_2 = user_repository.insert(new UserParam("ddd", "eee"));
		Assertions.assertTrue(test2_1);
		Assertions.assertTrue(test2_2);
		UserParam test3 = user_repository.selectName("aaa");
		Assertions.assertEquals("username: aaa, password: bbb, sex: male, age: 5555", test3.toString());
		boolean test4 = user_repository.updateUser(new UserParam("ddd", "fff"));
		Assertions.assertTrue(test4);
		test3 = user_repository.selectName("ddd");
		Assertions.assertEquals("username: ddd, password: fff, sex: female, age: 4444", test3.toString());
		boolean test5_1 = user_repository.deleteUser("aaa");
		boolean test5_2 = user_repository.deleteUser("ddd");
		Assertions.assertTrue(test5_1);
		List<UserParam> test6 = user_repository.selectAll();
		return;
	}



	@Test
	void contextLoads() {

	}

}
