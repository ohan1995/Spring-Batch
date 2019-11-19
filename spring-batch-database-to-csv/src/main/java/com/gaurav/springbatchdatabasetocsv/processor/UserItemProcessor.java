package com.gaurav.springbatchdatabasetocsv.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.gaurav.springbatchdatabasetocsv.model.User;

@Component
public class UserItemProcessor implements ItemProcessor<User, User>{

	@Override
	public User process(User user) throws Exception {
		return user;
	}

}
