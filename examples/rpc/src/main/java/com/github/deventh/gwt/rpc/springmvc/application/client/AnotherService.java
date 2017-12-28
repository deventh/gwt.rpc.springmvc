package com.github.deventh.gwt.rpc.springmvc.application.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("another/logic.gwt")
public interface AnotherService extends RemoteService {
	String another();
}
