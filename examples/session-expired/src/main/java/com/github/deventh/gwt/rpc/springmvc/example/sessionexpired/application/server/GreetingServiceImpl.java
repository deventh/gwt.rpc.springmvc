package com.github.deventh.gwt.rpc.springmvc.example.sessionexpired.application.server;

import com.github.deventh.gwt.rpc.springmvc.example.sessionexpired.application.client.GreetingService;
import com.github.deventh.gwt.rpc.springmvc.example.sessionexpired.application.shared.security.AppSecurityException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service("greetingService")
public class GreetingServiceImpl implements GreetingService {
	@Override
	public String greetServer(String name) throws AppSecurityException {
		return MessageFormat.format("Hello, {0}!", name);
	}
}
