package com.polarbookshop.catalogservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// 이 클래스는 "polar"로 시작하는 설정 속성에 대한 소스임을 표시함.
@ConfigurationProperties(prefix = "polar")
public class PolarProperties {
	/**
	 *  A message to welcome users. 
	 */
	private String greeting;
	
	public String getGreeting() {
		return greeting;
	}
	
	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}
}
