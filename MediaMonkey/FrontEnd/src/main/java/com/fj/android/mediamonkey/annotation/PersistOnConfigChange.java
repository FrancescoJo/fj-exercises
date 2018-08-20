/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.annotation;

import com.fj.android.mediamonkey.inject.component.PersistOnConfigChangeComponent;

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
public @interface PersistOnConfigChange {}
