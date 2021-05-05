package com.skillstorm.helloWorld.beans;

public class HelloWorld {

	public int id;
	public String message;
	
	public HelloWorld(int id, String message) {
		this.id = id;
		this.message = message;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "HelloWorldBeans [id=" + id + ", message=" + message + "]";
	}

}
