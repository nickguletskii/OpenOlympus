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
package org.ng200.openolympus.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BindingResponse {
	public static class FieldErrorDescription {
		private String field;
		private String defaultMessage;

		public FieldErrorDescription(String field, String defaultMessage) {
			super();
			this.field = field;
			this.defaultMessage = defaultMessage;
		}

		public String getDefaultMessage() {
			return this.defaultMessage;
		}

		public String getField() {
			return this.field;
		}

		public void setDefaultMessage(String defaultMessage) {
			this.defaultMessage = defaultMessage;
		}

		public void setField(String field) {
			this.field = field;
		}
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	public static enum Status {
		OK, BINDING_ERROR
	}

	public static final BindingResponse OK = new BindingResponse(Status.OK,
			null);

	private BindingResponse.Status status;
	private List<FieldErrorDescription> fieldErrors;
	private Map<String, Object> data;

	public BindingResponse() {

	}

	public BindingResponse(BindingResponse.Status status,
			List<FieldError> fieldErrors) {
		this.status = status;
		this.setFieldErrorsWithSpring(fieldErrors);
	}

	public BindingResponse(BindingResponse.Status status,
			List<FieldError> fieldErrors, Map<String, Object> data) {
		this.status = status;
		this.setFieldErrorsWithSpring(fieldErrors);
		this.data = data;
	}

	public Map<String, Object> getData() {
		return this.data;
	}

	public List<FieldErrorDescription> getFieldErrors() {
		return this.fieldErrors;
	}

	public BindingResponse.Status getStatus() {
		return this.status;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public void setFieldErrors(List<FieldErrorDescription> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	public void setFieldErrorsWithSpring(List<FieldError> fieldErrors) {
		if (fieldErrors == null) {
			return;
		}
		this.setFieldErrors(fieldErrors
				.stream()
				.map((x) -> new FieldErrorDescription(x.getField(), x
						.getDefaultMessage()))
				.collect(Collectors.toList()));
	}

	public void setStatus(BindingResponse.Status status) {
		this.status = status;
	}

}