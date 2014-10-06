/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package java.io;

/**
 * Wraps an existing {@link OutputStream} and provides convenience methods for
 * writing common data types in a human readable format. This is not to be
 * confused with DataOutputStream which is used for encoding common data types
 * so that they can be read back in. No {@code IOException} is thrown by this
 * class. Instead, callers should use {@link #checkError()} to see if a problem
 * has occurred in this stream.
 */
public class PrintStream extends FilterOutputStream implements Appendable,
        Closeable {
    /** "null" as bytes. */
    private static final byte[] NULL = "null".getBytes();

    /**
     * Constructs a new {@code PrintStream} with {@code out} as its target
     * stream. By default, the new print stream does not automatically flush its
     * contents to the target stream when a newline is encountered.
     *
     * @param out
     *            the target output stream.
     * @throws NullPointerException
     *             if {@code out} is {@code null}.
     */
    public PrintStream(OutputStream out) {
        super(out);
    }

    /**
     * Closes this print stream. Flushes this stream and then closes the target
     * stream. If an I/O error occurs, this stream's error state is set to
     * {@code true}.
     */
    @Override
    public void close() {
        try {
            super.close();
        } catch (IOException e) {
            // NOP
        }
    }

    /**
     * Ensures that all pending data is sent out to the target stream. It also
     * flushes the target stream. If an I/O error occurs, this stream's error
     * state is set to {@code true}.
     */
    @Override
    public void flush() {
        try {
            super.flush();
        } catch (IOException e) {
            // NOP
        }
    }

    /**
     * Prints a string to the target stream. The string is converted to an array
     * of bytes using the encoding chosen during the construction of this
     * stream. The bytes are then written to the target stream with
     * {@code write(int)}.
     * <p>
     * If an I/O error occurs, this stream's error state is set to {@code true}.
     *
     * @param str
     *            the string to print to the target stream.
     * @see #write(int)
     */
    public void print(String str) {
        try {
            if (str == null) {
                write(NULL);
            } else {
                write(str.getBytes());
            }
        } catch (IOException e) {
            // NOP
        }
    }

    /**
     * Prints the string representation of the specified character array
     * to the target stream.
     *
     * @param charArray
     *            the character array to print to the target stream.
     * @see #print(String)
     */
    public void print(char[] charArray) {
        print(new String(charArray, 0, charArray.length));
    }

    /**
     * Prints the string representation of the specified character to the target
     * stream.
     *
     * @param ch
     *            the character to print to the target stream.
     * @see #print(String)
     */
    public void print(char ch) {
        print(String.valueOf(ch));
    }

    /**
     * Prints the string representation of the specified double to the target
     * stream.
     *
     * @param dnum
     *            the double value to print to the target stream.
     * @see #print(String)
     */
    public void print(double dnum) {
        print(String.valueOf(dnum));
    }

    /**
     * Prints the string representation of the specified float to the target
     * stream.
     *
     * @param fnum
     *            the float value to print to the target stream.
     * @see #print(String)
     */
    public void print(float fnum) {
        print(String.valueOf(fnum));
    }

    /**
     * Prints the string representation of the specified integer to the target
     * stream.
     *
     * @param inum
     *            the integer value to print to the target stream.
     * @see #print(String)
     */
    public void print(int inum) {
        print(String.valueOf(inum));
    }

    /**
     * Prints the string representation of the specified long to the target
     * stream.
     *
     * @param lnum
     *            the long value to print to the target stream.
     * @see #print(String)
     */
    public void print(long lnum) {
        print(String.valueOf(lnum));
    }

    /**
     * Prints the string representation of the specified object to the target
     * stream.
     *
     * @param obj
     *            the object to print to the target stream.
     * @see #print(String)
     */
    public void print(Object obj) {
        print(String.valueOf(obj));
    }

    /**
     * Prints the string representation of the specified boolean to the target
     * stream.
     *
     * @param bool
     *            the boolean value to print the target stream.
     * @see #print(String)
     */
    public void print(boolean bool) {
        print(String.valueOf(bool));
    }

    /**
     * Prints the string representation of the system property
     * {@code "line.separator"} to the target stream.
     */
    public void println() {
        print('\n');
    }

    /**
     * Prints the string representation of the specified character array
     * followed by the system property {@code "line.separator"} to the target
     * stream.
     *
     * @param charArray
     *            the character array to print to the target stream.
     * @see #print(String)
     */
    public void println(char[] charArray) {
        println(new String(charArray, 0, charArray.length));
    }

    /**
     * Prints the string representation of the specified character followed by
     * the system property {@code "line.separator"} to the target stream.
     *
     * @param ch
     *            the character to print to the target stream.
     * @see #print(String)
     */
    public void println(char ch) {
        println(String.valueOf(ch));
    }

    /**
     * Prints the string representation of the specified double followed by the
     * system property {@code "line.separator"} to the target stream.
     *
     * @param dnum
     *            the double value to print to the target stream.
     * @see #print(String)
     */
    public void println(double dnum) {
        println(String.valueOf(dnum));
    }

    /**
     * Prints the string representation of the specified float followed by the
     * system property {@code "line.separator"} to the target stream.
     *
     * @param fnum
     *            the float value to print to the target stream.
     * @see #print(String)
     */
   public void println(float fnum) {
        println(String.valueOf(fnum));
    }

   /**
     * Prints the string representation of the specified integer followed by the
     * system property {@code "line.separator"} to the target stream.
     *
     * @param inum
     *            the integer value to print to the target stream.
     * @see #print(String)
     */
    public void println(int inum) {
        println(String.valueOf(inum));
    }

    /**
     * Prints the string representation of the specified long followed by the
     * system property {@code "line.separator"} to the target stream.
     *
     * @param lnum
     *            the long value to print to the target stream.
     * @see #print(String)
     */
    public void println(long lnum) {
        println(String.valueOf(lnum));
    }

    /**
     * Prints the string representation of the specified object followed by the
     * system property {@code "line.separator"} to the target stream.
     *
     * @param obj
     *            the object to print to the target stream.
     * @see #print(String)
     */
    public void println(Object obj) {
        println(String.valueOf(obj));
    }

    /**
     * Prints a string followed by the system property {@code "line.separator"}
     * to the target stream. The string is converted to an array of bytes using
     * the encoding chosen during the construction of this stream. The bytes are
     * then written to the target stream with {@code write(int)}.
     * <p>
     * If an I/O error occurs, this stream's error state is set to {@code true}.
     *
     * @param str
     *            the string to print to the target stream.
     * @see #write(int)
     */
    public void println(String str) {
        print(str);
        println();
    }

    /**
     * Prints the string representation of the specified boolean followed by the
     * system property {@code "line.separator"} to the target stream.
     *
     * @param bool
     *            the boolean value to print to the target stream.
     * @see #print(String)
     */
    public void println(boolean bool) {
        println(String.valueOf(bool));
    }

    /**
     * Appends the character {@code c} to the target stream. This method works
     * the same way as {@link #print(char)}.
     *
     * @param c
     *            the character to append to the target stream.
     * @return this stream.
     */
    public PrintStream append(char c) {
        print(c);
        return this;
    }

    /**
     * Appends the character sequence {@code csq} to the target stream. This
     * method works the same way as {@code PrintStream.print(csq.toString())}.
     * If {@code csq} is {@code null}, then the string "null" is written to the
     * target stream.
     *
     * @param csq
     *            the character sequence appended to the target stream.
     * @return this stream.
     */
    public PrintStream append(CharSequence csq) {
        if (null == csq) {
            print("null");
        } else {
            print(csq.toString());
        }
        return this;
    }

    /**
     * Appends a subsequence of the character sequence {@code csq} to the target
     * stream. This method works the same way as {@code
     * PrintStream.print(csq.subsequence(start, end).toString())}. If {@code
     * csq} is {@code null}, then the specified subsequence of the string "null"
     * will be written to the target stream.
     *
     * @param csq
     *            the character sequence appended to the target stream.
     * @param start
     *            the index of the first char in the character sequence appended
     *            to the target stream.
     * @param end
     *            the index of the character following the last character of the
     *            subsequence appended to the target stream.
     * @return this stream.
     * @throws IndexOutOfBoundsException
     *             if {@code start > end}, {@code start < 0}, {@code end < 0} or
     *             either {@code start} or {@code end} are greater or equal than
     *             the length of {@code csq}.
     */
    public PrintStream append(CharSequence csq, int start, int end) {
        if (null == csq) {
            print("null");
        } else {
            print(csq.subSequence(start, end).toString());
        }
        return this;
    }
}
