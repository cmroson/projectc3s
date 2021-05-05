package com.skillstorm.helloWorld.controller;

//Hello World API
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.skillstorm.helloWorld.beans.HelloWorld;

@RestController

public class HelloWorldController {

	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<HelloWorld> helloWorld() {
		List<HelloWorld> hello = new ArrayList<>();
		hello.add(new HelloWorld(1, "Hello World"));
		return hello;
	}
}
