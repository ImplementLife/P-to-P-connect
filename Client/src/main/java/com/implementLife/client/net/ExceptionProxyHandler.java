package com.implementLife.client.net;

import com.implementLife.client.UI.UIProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ExceptionProxyHandler implements InvocationHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionProxyHandler.class);

    private final Object origin;

    public ExceptionProxyHandler(Object origin) {
        this.origin = origin;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            LOG.info("Invoked method: {}({})", method.getName(), Arrays.toString(args));
            Object invokeResult = method.invoke(origin, args);
            LOG.info("After invoke the method: {}", method.getName());
            return invokeResult;
        } catch (Throwable throwable) {
            UIProvider.showErrDialog(throwable);
            throw throwable;
        }
    }
}
