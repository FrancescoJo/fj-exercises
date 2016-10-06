/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.annotation;

import com.example.hwan.myapplication.inject.component.PersistOnConfigChangeComponent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * A scoping annotation to permit dependencies conform to the life of the
 * {@link PersistOnConfigChangeComponent}.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Oct - 2016
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PersistOnConfigChange {
}
