package com.valtech.azubi.bankanwendung.server.filter;

import com.valtech.azubi.bankanwendung.model.core.Rolle;

import javax.ws.rs.NameBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@NameBinding
public @interface RolesAuthorization {

     Rolle[] value() default {};

}