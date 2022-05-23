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
