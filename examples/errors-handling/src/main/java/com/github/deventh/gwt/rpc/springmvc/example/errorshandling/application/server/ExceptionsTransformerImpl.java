package com.github.deventh.gwt.rpc.springmvc.example.errorshandling.application.server;

import com.github.deventh.gwt.rpc.springmvc.ExceptionsTransformer;
import com.github.deventh.gwt.rpc.springmvc.example.errorshandling.application.shared.EmailNotUniqueException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Component
public class ExceptionsTransformerImpl implements ExceptionsTransformer {
  @Override
  public RuntimeException transform(Exception serverSideError) {
    if(serverSideError instanceof DuplicateKeyException) {
      // if server side exception does not contain all need info
      // use thread local if needed
      return new EmailNotUniqueException();
    }

    return null;
  }
}
