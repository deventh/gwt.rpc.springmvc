package com.github.deventh.gwt.rpc.springmvc.example.sessionexpired.application.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.github.deventh.gwt.rpc.springmvc.example.sessionexpired.application.shared.security.AppSecurityException;

@RemoteServiceRelativePath("greet/greetingService.gwt")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws AppSecurityException;
}
