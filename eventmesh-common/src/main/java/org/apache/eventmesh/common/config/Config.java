package org.apache.eventmesh.common.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface Config {

	String field() ;
	
	String path() default "";
	
	String prefix();
	
	String hump() default ".";
	
	boolean removePrefix() default true;
	
	boolean monitor() default false;
}



