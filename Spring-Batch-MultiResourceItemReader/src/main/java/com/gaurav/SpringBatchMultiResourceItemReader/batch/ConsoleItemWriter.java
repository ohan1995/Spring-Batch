package com.gaurav.SpringBatchMultiResourceItemReader.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.gaurav.SpringBatchMultiResourceItemReader.model.User;

@SuppressWarnings("hiding")
public class ConsoleItemWriter<User> implements ItemWriter<User> {

	@Override
	public void write(List<? extends User> items) throws Exception {
		for(User item : items) {
			System.out.println(item);
		}
	}

}
