package org.ng200.openolympus.security;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;

import javax.mail.MethodNotSupportedException;

import org.aopalliance.intercept.MethodInvocation;

public class OpenOlympusMethodInvocation<T> {

	private final class ParameterGetterInvocationHandler
			implements InvocationHandler {
		private final MethodInvocation methodInvocation;

		public ParameterGetterInvocationHandler(
				MethodInvocation methodInvocation) {
			this.methodInvocation = methodInvocation;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			Parameter[] parameters = methodInvocation.getMethod()
					.getParameters();

			String name = method.getName();
			int ind;

			searchForParameter: {
				for (int i = 0; i < parameters.length; i++) {
					if (parameters[i].getName().equals(name)) {
						ind = i;
						break searchForParameter; // Found the required
													// parameter
					}
				}
				throw new MethodNotSupportedException(
						"This method can't be supplied by method invocation information access proxy.");
			}

			return methodInvocation.getArguments()[ind];
		}
	}

	private MethodInvocation methodInvocation;
	private T parameterProxy;

	@SuppressWarnings("unchecked")
	public OpenOlympusMethodInvocation(MethodInvocation methodInvocation,
			Class<T> clazz) {
		this.methodInvocation = methodInvocation;
		this.parameterProxy = (T) Proxy.newProxyInstance(
				clazz.getClassLoader(),
				new Class[] {
								clazz
		}, new ParameterGetterInvocationHandler(methodInvocation));
	}

	public T getParameterProxy() {
		return parameterProxy;
	}

	public MethodInvocation getMethodInvocation() {
		return methodInvocation;
	}

}
