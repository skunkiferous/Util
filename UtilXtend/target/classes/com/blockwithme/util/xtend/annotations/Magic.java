package com.blockwithme.util.xtend.annotations;

import com.blockwithme.util.xtend.annotations.MagicAnnotationProcessor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.xtend.lib.macro.Active;

/**
 * Marks that *all types in this file* should be processed.
 * 
 * @author monster
 */
@Active(MagicAnnotationProcessor.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Magic {
}
