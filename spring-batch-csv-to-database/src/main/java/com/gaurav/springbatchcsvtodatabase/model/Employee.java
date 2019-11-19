package com.gaurav.springbatchcsvtodatabase.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Employee implements Serializable{

	private static final long serialVersionUID = 8078578076650845559L;
	
	private String firstName;
	private String lastName;
	private String companyName;
	private String city;
	private String state;
	
}
