/**
 *
 */
package com.blockwithme.util.proto.base40;

/**
 * Handler interface for instances of Enum40. It takes and returns a long.
 *
 * It should be extended for every Enum40 type, to provide one method for
 * every Enum40 value. And each value should delegate to the appropriate
 * method of the handler from the Enum40.handle() method.
 *
 * To keep the specific handler interface generic, all handler methods
 * should have the form: long valueName(long input);
 *
 * @author monster
 */
public interface Enum40LongHandler<E extends Enum40<E>> {
    /** Handles an unknown instance of Enum40. */
    long unknown(final E unknow, final long input);
}
