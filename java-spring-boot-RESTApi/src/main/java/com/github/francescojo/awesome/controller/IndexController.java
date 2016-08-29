package com.github.francescojo.awesome.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.francescojo.awesome.bo.CounterBo;
import com.github.francescojo.awesome.dto.CounterDto;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 */
@RestController
public class IndexController {
	@Autowired
	private CounterBo counterBo;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public CounterDto root(Model model) {
		CounterDto counter = counterBo.getCounter();

		counter.setCounter(counter.getCounter() + 1)
			.setLastAccessed(new Date(System.currentTimeMillis()));
		counterBo.updateCounter(counter);
		return counter;
	}

	public CounterBo getAppBo() {
		return counterBo;
	}

	public void setAppBo(CounterBo appBo) {
		this.counterBo = appBo;
	}
}