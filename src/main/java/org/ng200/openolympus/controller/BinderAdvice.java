/**
 * The MIT License
 * Copyright (c) 2014-2015 Nick Guletskii
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
package org.ng200.openolympus.controller;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.joor.Reflect;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class BinderAdvice implements ApplicationContextAware {
	private final class DurationEditor extends PropertyEditorSupport {

		@Override
		public String getAsText() {
			return Long.toString(((Duration) this.getValue()).toMillis());
		}

		@Override
		public void setAsText(String text) throws IllegalArgumentException {

			this.setValue(Duration.ofMillis(Long.valueOf(text)));
		}

	}

	private final class ModelTypeEditor extends PropertyEditorSupport {

		private Reflect dao;

		public ModelTypeEditor(Class<?> clazz,
				ApplicationContext applicationContext)
				throws NoSuchMethodException, SecurityException,
				ClassNotFoundException {
			Class<?> daoClass = clazz.getClassLoader().loadClass(
					clazz.getName().replaceAll("pojos", "daos") + "Dao");
			dao = Reflect.on(applicationContext.getBean(daoClass));
		}

		@Override
		public String getAsText() {
			return Reflect.on(this.getValue()).call("getId").call("toString")
					.get();
		}

		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			this.setValue(dao.call("fetchOneById", text).get());
		}

	}

	private static final CustomDateEditor dateEditor = new CustomDateEditor(
			new SimpleDateFormat("YYYY-MM-DD"), true);
	private ApplicationContext applicationContext;

	@InitBinder
	protected void initBinder(final HttpServletRequest request,
			final ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(LocalDate.class, BinderAdvice.dateEditor);
		binder.registerCustomEditor(Duration.class, new DurationEditor());

		registerModelType(binder, User.class);
		registerModelType(binder, Task.class);
		registerModelType(binder, Contest.class);
	}

	private void registerModelType(ServletRequestDataBinder binder,
			Class<?> clazz) throws NoSuchMethodException, SecurityException,
			ClassNotFoundException {
		binder.registerCustomEditor(clazz, new ModelTypeEditor(clazz,
				applicationContext));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
