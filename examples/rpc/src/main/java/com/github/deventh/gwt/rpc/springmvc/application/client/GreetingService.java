package com.github.deventh.gwt.rpc.springmvc.application.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greet/greetingService.gwt")
public interface GreetingService extends RemoteService {
	String greetServer(String name);
}
