package io.fluentqa.codegen.database.enums;

/**
 * 关系类型
 *
 *
 */
public enum Relational {
	OneToOne("OneToOne"), OneToMany("OneToMany"), ManyToOne("ManyToOne"), ManyToMany("ManyToMany");
	/** 关系的值 */
	public String val;

	private Relational(String val) {
		this.val = val;
	}

}
