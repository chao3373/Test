package com.pachong;

public class ProName {
	String name;
	String star;
	
	public ProName(String name, String star) {
		super();
		this.name = name;
		this.star = star;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}
	
	@Override
	public String toString() {
		return "项目名：" + this.name + "\t" + "星级：" + this.star;
	}

}
