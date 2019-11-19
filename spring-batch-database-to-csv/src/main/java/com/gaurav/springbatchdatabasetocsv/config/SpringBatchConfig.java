package com.gaurav.springbatchdatabasetocsv.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import com.gaurav.springbatchdatabasetocsv.model.User;
import com.gaurav.springbatchdatabasetocsv.processor.UserItemProcessor;
import com.gaurav.springbatchdatabasetocsv.rowmapper.UserRowMapper;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;
	
	//reader
	//read from the database
	@Bean
	public JdbcCursorItemReader<User> reader() {
		System.out.println("reader============================");
		JdbcCursorItemReader<User> reader = new JdbcCursorItemReader<>();
		reader.setDataSource(dataSource);
		reader.setSql("SELECT id,name from user");
		reader.setRowMapper(new UserRowMapper());
		
		return reader;
	}
	
	//processor
	
	@Bean
	public UserItemProcessor processor() {
		return new UserItemProcessor();
	}
	
	//writer
	//write to the CSV file
	@Bean
	public FlatFileItemWriter<User> writer(){
		FlatFileItemWriter<User> writer = new FlatFileItemWriter<>();
		System.out.println("Writer Method()==============================================");
		writer.setResource(new ClassPathResource("user.csv"));
		writer.setLineAggregator(new DelimitedLineAggregator<User>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<User>() {
					{
						setNames(new String[] {"id","name"});
					}
				});
			}
		});
		return writer;
	}
	
	//Step
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<User, User> chunk(100)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}
	
	//Job
	@Bean
	public Job job() {
		return jobBuilderFactory.get("exportUserJob")
				.incrementer(new RunIdIncrementer())
				.flow(step1())
				.end()
				.build();
	}
}
