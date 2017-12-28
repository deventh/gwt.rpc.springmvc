package com.github.deventh.gwt.rpc.springmvc.example.errorshandling.application.client;

import com.github.deventh.gwt.rpc.springmvc.example.errorshandling.application.shared.ApplicationException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("greet/greetingService.gwt")
public interface PostEmailService extends RemoteService {
	List<String> post(String email) throws ApplicationException;
}
