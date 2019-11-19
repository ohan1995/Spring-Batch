package com.gaurav.SpringBatchMultiResourceItemReader.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.gaurav.SpringBatchMultiResourceItemReader.batch.ConsoleItemWriter;
import com.gaurav.SpringBatchMultiResourceItemReader.model.User;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired 
	private StepBuilderFactory stepBuilderFactory;
	
	@Value("classpath:/inputData*.csv")
	private Resource[] inputResource;
	
	@Bean
	public FlatFileItemReader<User> reader() {
		FlatFileItemReader<User> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setLineMapper(new DefaultLineMapper<User>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames(new String[] {"id", "firstName", "lastName"});
			}});
			
			setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
				setTargetType(User.class);
			}});
		}});
		return reader;
	}
	
	@Bean
	public MultiResourceItemReader<User> multiResourceItemReader() {
		
		MultiResourceItemReader<User> multiResourceItemReader = new MultiResourceItemReader<>();
		multiResourceItemReader.setResources(inputResource);
		multiResourceItemReader.setDelegate(reader());
		return multiResourceItemReader;
	}
	
	@Bean
	public ConsoleItemWriter<User> writer() {
		return new ConsoleItemWriter<>();
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<User, User>chunk(10)
				.reader(reader())
				.writer(writer())
				.build();
	}
	
	@Bean
	public Job job() {
		return jobBuilderFactory.get("readMultipleCSVFiles")
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.build();
	}
}
