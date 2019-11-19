package com.gaurav.springbatchcsvtodatabase.config;


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

import com.gaurav.springbatchcsvtodatabase.batch.EmployeeProcessor;
import com.gaurav.springbatchcsvtodatabase.listner.JobListener;
import com.gaurav.springbatchcsvtodatabase.model.Employee;
import com.gaurav.springbatchcsvtodatabase.model.EmployeeDTO;

@Configuration
@EnableBatchProcessing
public class SpringConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;
	
	//reader
	@Bean
	public FlatFileItemReader<Employee> reader() {
		
		FlatFileItemReader<Employee> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("employee.csv"));
		reader.setLinesToSkip(1);
		reader.setLineMapper(new DefaultLineMapper<Employee>() {
			{
//				setLineTokenizer(new DelimitedLineTokenizer() {
//					{
//						setNames(new String[] {"first_name","last_name","company_name","city","state"});
//					}
//				});
				
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {
					{
						setTargetType(Employee.class);
					}
				});
			}
		});
		return reader;
	}
	
	//processor
	@Bean
	public EmployeeProcessor processor() {
		return new EmployeeProcessor();
	}
	
	
	//writer
	@Bean
	public JdbcBatchItemWriter<EmployeeDTO> writer() {
		JdbcBatchItemWriter<EmployeeDTO> writer = new JdbcBatchItemWriter<>();
		
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		
		writer.setSql("INSERT INTO employeedto(first_name,last_name,company_name,city,state)"+
						"VALUES(:firstName,:lastName,:companyName,:city,:state)");
		
		writer.setDataSource(dataSource);
		return writer;
	}
	
	//job
	@Bean
	public Job importUserJob(JobListener listener) {
		return jobBuilderFactory.get("importUserJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1())
				.end()
				.build();
	}
	
	
	//step
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<Employee, EmployeeDTO> chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}
}
