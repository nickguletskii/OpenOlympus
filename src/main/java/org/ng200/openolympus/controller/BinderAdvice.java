package org.ng200.openolympus.controller;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class BinderAdvice {
	private final class DurationEditor extends PropertyEditorSupport {

		@Override
		public String getAsText() {
			return Long.toString(((Duration) getValue()).getSeconds());
		}

		@Override
		public void setAsText(String text) throws IllegalArgumentException {

			setValue(Duration.ofSeconds(Long.valueOf(text)));
		}

	}

	private static final CustomDateEditor dateEditor = new CustomDateEditor(
			new SimpleDateFormat("YYYY-MM-DD"), true);

	@InitBinder
	protected void initBinder(final HttpServletRequest request,
			final ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(LocalDate.class, BinderAdvice.dateEditor);
		binder.registerCustomEditor(Duration.class, new DurationEditor());
	}
}
