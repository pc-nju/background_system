package com.bms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class BmsApplicationTests {

	class Animal {
		private String type;
		private int age;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public Animal(String type, int age) {
			this.type = type;
			this.age = age;
		}

		@Override
		public String toString() {
			return "Animal{" +
					"type='" + type + '\'' +
					", age=" + age +
					'}';
		}
	}

	@Test
	public void contextLoads() {
		Animal animal1 = new Animal("dog", 1);
		Animal animal2 = new Animal("cat", 1);
		Animal animal3 = new Animal("bird", 1);
		List<Animal> animals = new ArrayList<>();
		animals.add(animal1);
		animals.add(animal2);
		animals.add(animal3);
		animals.forEach(animal -> {
			animal.setAge(2);
		});
		System.out.println(animals);
	}

}

