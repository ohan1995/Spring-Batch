package com.gaurav.springbootbatchcsvtodatabase.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.gaurav.springbootbatchcsvtodatabase.model.User;

@Component
public class UserItemProcessor implements ItemProcessor<User, User>{

	@Override
	public User process(User user) throws Exception {
		return new User(user.getFirstName().toUpperCase(),
						user.getLastName().toUpperCase(),
						user.getCity().toLowerCase(),
						user.getState().toUpperCase(),
						user.getCountry().toUpperCase());
	}
	
	
}
