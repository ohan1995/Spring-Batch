package com.gaurav.springbootbatchcsvtodatabase.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import com.gaurav.springbootbatchcsvtodatabase.model.User;
import com.gaurav.springbootbatchcsvtodatabase.processor.UserItemProcessor;

@Configuration
@EnableBatchProcessing
public class SpringBootBatchConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public FlatFileItemReader<User> reader() {
		
		FlatFileItemReader<User> reader = new FlatFileItemReader<>();
		
		reader.setResource(new ClassPathResource("user.csv"));
		reader.setLinesToSkip(1);
		
		reader.setLineMapper(new DefaultLineMapper<User>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] {"first_name", "last_name", "city" ,"state", "country"});
					}
				});
				
				setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {
					{
						setTargetType(User.class);
					}
				});
			}
		});
		return reader;
	}
	
	
	@Bean
	public UserItemProcessor processor() {
		return new UserItemProcessor();
	}
	
	@Bean
	public JdbcBatchItemWriter<User> writer() {
		JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriter<>();
		
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		System.out.println("Writing data =====================================================");
		writer.setSql("INSERT INTO USER(first_name,last_name,city,state,country)"+
					"VALUES(:firstName,:lastName,:city,:state,:country)");
		writer.setDataSource(dataSource);
		return writer;
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<User, User> chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}
	
	@Bean
	public Job job() {
		return jobBuilderFactory.get("userJob")
				.incrementer(new RunIdIncrementer())
				.flow(step1())
				.end()
				.build();
	}
}
