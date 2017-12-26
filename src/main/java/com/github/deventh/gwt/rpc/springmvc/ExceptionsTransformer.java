package com.github.deventh.gwt.rpc.springmvc;

/**
 * Transformer between server side exceptions and client side aware exceptions.
 */
public interface ExceptionsTransformer {
  RuntimeException transform(Exception serverSideError);
}
