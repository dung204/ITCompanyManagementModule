package com.exercise;

import java.util.ArrayList;
import java.util.HashMap;

class A {
	public void sayHello() {
		System.out.println("Hello from A");
	}
}

class B extends A {
	@Override
	public void sayHello() {
		System.out.println("Hello from B");
	}
}

public class Test {
	public static void main(String[] args) {
		HashMap<Integer, Integer> map = new HashMap<>();
		map.put(3, -8);
		map.put(0, 6);
		map.put(9, 12);
		map.put(1, 100);
		map.forEach((integer, integer2) -> System.out.println(integer + " " + integer2));
	}
}
