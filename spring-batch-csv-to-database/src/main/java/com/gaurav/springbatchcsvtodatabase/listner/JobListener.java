package com.gaurav.springbatchcsvtodatabase.listner;

import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.gaurav.springbatchcsvtodatabase.model.EmployeeDTO;

@Component
public class JobListener extends JobExecutionListenerSupport {
	
	private final JdbcTemplate JDBCTEMPLATE;
	
	@Autowired
	public JobListener(JdbcTemplate jdbcTemplate) {
		this.JDBCTEMPLATE = jdbcTemplate;
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			System.out.println("In completion Listener");
			
			List<EmployeeDTO> results = JDBCTEMPLATE.query("SELECT first_name,last_name,company_name,city,state FROM employeedto",
					(rs,rowNum)->{
                        return new EmployeeDTO(rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4),
                                rs.getString(5));
                    	}
					);
			results.forEach(System.out::println);
		}
	}
}
