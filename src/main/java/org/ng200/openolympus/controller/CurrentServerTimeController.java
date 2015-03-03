package org.ng200.openolympus.controller;

import java.time.Instant;
import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrentServerTimeController {

	@RequestMapping(value = "/api/time")
	public Date currentTime() {
		return Date.from(Instant.now());
	}
}
