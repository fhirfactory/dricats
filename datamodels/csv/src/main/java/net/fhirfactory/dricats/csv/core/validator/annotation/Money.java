package net.fhirfactory.dricats.csv.core.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.fhirfactory.dricats.csv.core.validator.MoneyTypeEnum;

/**
 * An annotation to use when CSV money validation is required.
 *
 * @author Brendan Douglas
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Money {
    public MoneyTypeEnum type() default MoneyTypeEnum.ANY;
}
