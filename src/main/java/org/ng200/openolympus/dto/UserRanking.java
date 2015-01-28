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
package org.ng200.openolympus.dto;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ng200.openolympus.model.User;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.fasterxml.jackson.annotation.JsonView;

public class UserRanking extends User {

	/**
	 *
	 */
	private static final long serialVersionUID = -1305111374276439473L;

	private BigDecimal score;

	public UserRanking(User user, BigDecimal score) {
		this.score = score;

		final BeanWrapper src = new BeanWrapperImpl(user);
		final BeanWrapper trg = new BeanWrapperImpl(this);

		for (final String propertyName : Stream
				.of(src.getPropertyDescriptors()).map(pd -> pd.getName())
				.collect(Collectors.toList())) {
			if (trg.getPropertyDescriptor(propertyName).getWriteMethod() == null) {
				continue;
			}

			trg.setPropertyValue(propertyName,
					src.getPropertyValue(propertyName));
		}
	}

	@JsonView(UnprivilegedUserView.class)
	public BigDecimal getScore() {
		return this.score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

}
