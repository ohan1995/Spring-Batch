package com.gaurav.springbatchcsvtodatabase.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeeDTO {
	
	
	@Id
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "company_name")
	private String companyName;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "state")
	private String state;
}
