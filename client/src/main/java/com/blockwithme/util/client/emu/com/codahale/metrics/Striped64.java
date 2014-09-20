/*
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 *
 * http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/jsr166e/Striped64.java?revision=1.8&view=markup
 */

package com.codahale.metrics;

// CHECKSTYLE:OFF
/**
 * A package-local class holding common representation and mechanics for classes supporting dynamic
 * striping on 64bit values. The class extends Number so that concrete subclasses must publicly do
 * so.
 */
@SuppressWarnings("all")
abstract class Striped64 extends Number {
    protected long base;
}
// CHECKSTYLE:ON
