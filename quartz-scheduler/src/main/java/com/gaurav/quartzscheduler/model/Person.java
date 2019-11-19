package com.gaurav.quartzscheduler.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

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
public class Person {
	
	@XmlAttribute(name = "id")
	private int id;
	
	@XmlElement(name = "first_name")
	private String firstName;
	
	@XmlElement(name = "lastName")
	private String lastName;
}
