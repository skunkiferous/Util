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
package java.io;

/**
 * @author monster
 *
 */
public class FileOutputStream extends OutputStream {

    private final RandomAccessFile file;

    public FileOutputStream(File file) throws FileNotFoundException {
        this.file = new RandomAccessFile(file, "rw");
    }

    public FileOutputStream(String name) throws FileNotFoundException {
        this(new File(name));
    }

    public void close() throws IOException {
        file.close();
    }

    public void flush() throws IOException {
        file.flush();
    }

    public void write(byte[] ba) throws IOException {
        file.write(ba);
    }

    public void write(int b) throws IOException {
        file.write(b);
    }

    public void write(byte[] ba, int start, int len) throws IOException {
        file.write(ba, start, len);
    }
}