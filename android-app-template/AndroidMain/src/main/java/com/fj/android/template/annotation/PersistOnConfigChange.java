/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.annotation;

import com.fj.android.template.inject.component.PersistOnConfigChangeComponent;

import javax.inject.Scope;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A scoping annotation to permit dependencies conform to the life of the
 * {@link PersistOnConfigChangeComponent}.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Oct - 2016
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PersistOnConfigChange {}
