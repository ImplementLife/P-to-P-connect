package com.implementLife.connectingService.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class LogProxy implements InvocationHandler {
    private static final Logger LOG = LoggerFactory.getLogger(LogProxy.class);

    private final Object origin;

    public LogProxy(Object origin) {
        this.origin = origin;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        LOG.info("Invoked method: {}({})", method.getName(), Arrays.toString(args));
        Object invokeResult = method.invoke(origin, args);
        LOG.info("After invoke the method: {}", method.getName());
        return invokeResult;
    }
}
