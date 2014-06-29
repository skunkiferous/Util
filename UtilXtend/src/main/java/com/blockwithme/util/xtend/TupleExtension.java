/*
 * Copyright (C) 2014 Sebastien Diot.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blockwithme.util.xtend;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Xtend Extension related to tuples values.
 *
 * The extension methods must be defined in Java, to be able to use @Inline,
 * but the @Data must be declared in Xtend, therefore it is split in two.
 *
 * @author monster
 */
public class TupleExtension extends MathExtension {
    @Pure
    @Inline("new com.blockwithme.util.xtend.TB($1)")
    public static TB $(final boolean p0) {
        return new TB(p0);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TD($1)")
    public static TD $(final double p0) {
        return new TD(p0);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TO<E0>($1)")
    public static <E0> TO<E0> $(final E0 p0) {
        return new TO<E0>(p0);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TBB($1,$2)")
    public static TBB $(final boolean p0, final boolean p1) {
        return new TBB(p0, p1);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TDB($1,$2)")
    public static TDB $(final double p0, final boolean p1) {
        return new TDB(p0, p1);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TOB<E0>($1,$2)")
    public static <E0> TOB<E0> $(final E0 p0, final boolean p1) {
        return new TOB<E0>(p0, p1);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TBD($1,$2)")
    public static TBD $(final boolean p0, final double p1) {
        return new TBD(p0, p1);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TDD($1,$2)")
    public static TDD $(final double p0, final double p1) {
        return new TDD(p0, p1);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TOD<E0>($1,$2)")
    public static <E0> TOD<E0> $(final E0 p0, final double p1) {
        return new TOD<E0>(p0, p1);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TBO($1,$2)")
    public static <E1> TBO<E1> $(final boolean p0, final E1 p1) {
        return new TBO<E1>(p0, p1);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TDO($1,$2)")
    public static <E1> TDO<E1> $(final double p0, final E1 p1) {
        return new TDO<E1>(p0, p1);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TOO<E0,E1>($1,$2)")
    public static <E0, E1> TOO<E0, E1> $(final E0 p0, final E1 p1) {
        return new TOO<E0, E1>(p0, p1);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TBBB($1,$2,$3)")
    public static TBBB $(final boolean p0, final boolean p1, final boolean p2) {
        return new TBBB(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TDBB($1,$2,$3)")
    public static TDBB $(final double p0, final boolean p1, final boolean p2) {
        return new TDBB(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TOBB<E0>($1,$2,$3)")
    public static <E0> TOBB<E0> $(final E0 p0, final boolean p1,
            final boolean p2) {
        return new TOBB<E0>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TBDB($1,$2,$3)")
    public static TBDB $(final boolean p0, final double p1, final boolean p2) {
        return new TBDB(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TDDB($1,$2,$3)")
    public static TDDB $(final double p0, final double p1, final boolean p2) {
        return new TDDB(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TODB<E0>($1,$2,$3)")
    public static <E0> TODB<E0> $(final E0 p0, final double p1, final boolean p2) {
        return new TODB<E0>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TBOB($1,$2,$3)")
    public static <E1> TBOB<E1> $(final boolean p0, final E1 p1,
            final boolean p2) {
        return new TBOB<E1>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TDOB($1,$2,$3)")
    public static <E1> TDOB<E1> $(final double p0, final E1 p1, final boolean p2) {
        return new TDOB<E1>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TOOB<E0,E1>($1,$2,$3)")
    public static <E0, E1> TOOB<E0, E1> $(final E0 p0, final E1 p1,
            final boolean p2) {
        return new TOOB<E0, E1>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TBBD($1,$2,$3)")
    public static TBBD $(final boolean p0, final boolean p1, final double p2) {
        return new TBBD(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TDBD($1,$2,$3)")
    public static TDBD $(final double p0, final boolean p1, final double p2) {
        return new TDBD(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TOBD<E0>($1,$2,$3)")
    public static <E0> TOBD<E0> $(final E0 p0, final boolean p1, final double p2) {
        return new TOBD<E0>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TBDD($1,$2,$3)")
    public static TBDD $(final boolean p0, final double p1, final double p2) {
        return new TBDD(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TDDD($1,$2,$3)")
    public static TDDD $(final double p0, final double p1, final double p2) {
        return new TDDD(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TODD<E0>($1,$2,$3)")
    public static <E0> TODD<E0> $(final E0 p0, final double p1, final double p2) {
        return new TODD<E0>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TBOD($1,$2,$3)")
    public static <E1> TBOD<E1> $(final boolean p0, final E1 p1, final double p2) {
        return new TBOD<E1>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TDOD($1,$2,$3)")
    public static <E1> TDOD<E1> $(final double p0, final E1 p1, final double p2) {
        return new TDOD<E1>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TOOD<E0,E1>($1,$2,$3)")
    public static <E0, E1> TOOD<E0, E1> $(final E0 p0, final E1 p1,
            final double p2) {
        return new TOOD<E0, E1>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TBBO<E2>($1,$2,$3)")
    public static <E2> TBBO<E2> $(final boolean p0, final boolean p1,
            final E2 p2) {
        return new TBBO<E2>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TDBO<E2>($1,$2,$3)")
    public static <E2> TDBO<E2> $(final double p0, final boolean p1, final E2 p2) {
        return new TDBO<E2>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TOBO<E0,E2>($1,$2,$3)")
    public static <E0, E2> TOBO<E0, E2> $(final E0 p0, final boolean p1,
            final E2 p2) {
        return new TOBO<E0, E2>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TBDO<E2>($1,$2,$3)")
    public static <E2> TBDO<E2> $(final boolean p0, final double p1, final E2 p2) {
        return new TBDO<E2>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TDDO<E2>($1,$2,$3)")
    public static <E2> TDDO<E2> $(final double p0, final double p1, final E2 p2) {
        return new TDDO<E2>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TODO<E0,E2>($1,$2,$3)")
    public static <E0, E2> TODO<E0, E2> $(final E0 p0, final double p1,
            final E2 p2) {
        return new TODO<E0, E2>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TBOO<E1,E2>($1,$2,$3)")
    public static <E1, E2> TBOO<E1, E2> $(final boolean p0, final E1 p1,
            final E2 p2) {
        return new TBOO<E1, E2>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TDOO<E1,E2>($1,$2,$3)")
    public static <E1, E2> TDOO<E1, E2> $(final double p0, final E1 p1,
            final E2 p2) {
        return new TDOO<E1, E2>(p0, p1, p2);
    }

    @Pure
    @Inline("new com.blockwithme.util.xtend.TOOO<E0,E1,E2>($1,$2,$3)")
    public static <E0, E1, E2> TOOO<E0, E1, E2> $(final E0 p0, final E1 p1,
            final E2 p2) {
        return new TOOO<E0, E1, E2>(p0, p1, p2);
    }
}
