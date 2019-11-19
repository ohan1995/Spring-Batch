package com.gaurav.springbatchcsvtodatabase.batch;

import org.springframework.batch.item.ItemProcessor;

import com.gaurav.springbatchcsvtodatabase.model.Employee;
import com.gaurav.springbatchcsvtodatabase.model.EmployeeDTO;


public class EmployeeProcessor implements ItemProcessor<Employee, EmployeeDTO> {

	@Override
	public EmployeeDTO process(Employee employee) throws Exception {
		System.out.println("Transforming Employee to EmployeeDTO");
		final EmployeeDTO EMPLOYEEDTO = new EmployeeDTO(employee.getFirstName().toUpperCase(),
														employee.getLastName().toUpperCase(),
														employee.getCompanyName().toUpperCase(),
														employee.getCity().toUpperCase(),
														employee.getState().toUpperCase());
		
		return EMPLOYEEDTO;
	}

}
