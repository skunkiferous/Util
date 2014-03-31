/**
 *
 */
package com.blockwithme.util.server.base40;

/**
 * Handler interface for instances of Enum40. It takes and returns an Object.
 *
 * It should be extended for every Enum40 type, to provide one method for
 * every Enum40 value. And each value should delegate to the appropriate
 * method of the handler from the Enum40.handle() method.
 *
 * To keep the specific handler interface generic, all handler methods
 * should have the form: OUTPUT valueName(INPUT input);
 *
 * @author monster
 */
public interface Enum40ObjectHandler<E extends Enum40<E>, INPUT, OUTPUT> {
    /** Handles an unknown instance of Enum40. */
    OUTPUT unknown(final E unknow, final INPUT input);
}
