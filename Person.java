package Ignite.mysql;

import java.io.Serializable;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Person implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@QuerySqlField(index = true)
	private long id;
	@QuerySqlField(index = true)
    private long orgId;
	@QuerySqlField(index = true)
    private String name;
	@QuerySqlField(index = true)
    private int salary;
	 public Person(long long1, long long2, String string, int int1) {
		// TODO Auto-generated constructor stub
		 this.id = long1;
		 this.orgId = long2;
		 this.name = string;
		 this.salary = int1;
	}
	public Long getId() {
		// TODO Auto-generated method stub
		return this.id;
	}
	public Long getorgId() {
		// TODO Auto-generated method stub
		return this.orgId;
	}
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}
	public int getsal() {
		// TODO Auto-generated method stub
		return this.salary;
	}
	public String toString(){
	    return "id: "+ this.id
	       + " name:"+ this.name
	       + " salary:"+ this.salary; 
	} 
	    
	}