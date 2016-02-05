/**
 * The MIT License
 * Copyright (c) 2014-2016 Nick Guletskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
			final Parameter[] parameters = this.methodInvocation.getMethod()
					.getParameters();

			final String name = method.getName();
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

			return this.methodInvocation.getArguments()[ind];
		}
	}

	private final MethodInvocation methodInvocation;
	private final T parameterProxy;

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

	public MethodInvocation getMethodInvocation() {
		return this.methodInvocation;
	}

	public T getParameterProxy() {
		return this.parameterProxy;
	}

}
