/*******************************************************************************
 * Copyright (C) 2026 poltergeist0
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Any libraries this program depends on have their own Licenses.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * LICENSE file for more details.
 ******************************************************************************/
package jrain.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author poltergeist0
 *
 * This class extends the functionality of {@link FilterOutputStream} by providing 
 * a count of the number of bytes written.
 */
public class OutputStreamProgress extends FilterOutputStream {

    /**
     * Byte count
     */
    private long totalBytes = 0;

    /**
     * Constructor
     * 
     * @param out is the output stream
     */
    public OutputStreamProgress(OutputStream out) {
       super(out);
    }

    @Override
    public void write(int b) throws IOException {
       super.write(b);
       totalBytes++;
    }

    @Override
    public void write(byte[] b) throws IOException {
       super.write(b);
       totalBytes += b.length;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
       super.write(b,off,len);
       totalBytes += len-off;
    }
    
    @Override
    public void close() throws IOException {
    	if(super.out!=System.out) super.close();
    }

    /**
     * @return the number of bytes written
     */
    public long byteCount() {return totalBytes;}
}
