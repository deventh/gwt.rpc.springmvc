package com.github.deventh.gwt.rpc.springmvc.application.server;

import com.github.deventh.gwt.rpc.springmvc.application.client.AnotherService;
import com.github.deventh.gwt.rpc.springmvc.application.client.GreetingService;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service("greetingService")
public class GreetingServiceImpl implements GreetingService, AnotherService {
	@Override
	public String greetServer(String name) throws IllegalArgumentException {
		return MessageFormat.format("Hello, {0}!", name);
	}

	@Override
	public String another() {
		return "Another Logic";
	}
}
