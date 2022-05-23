package jrain.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author poltergeist0
 *
 * This class extends the functionality of {@link FilterInputStream} by providing 
 * a count of the number of bytes read.
 */
public class InputStreamProgress extends FilterInputStream {

    /**
     * Byte count
     */
    private long totalBytes = 0;

    /**
     * Constructor
     * 
     * @param in is the input stream
     */
    public InputStreamProgress(InputStream in) {
       super(in);
    }

    @Override
    public int read() throws IOException {
       int bt = super.read();
       if(bt>=0)totalBytes++;
       return bt;
    }

    @Override
    public int read(byte[] b) throws IOException {
       int count = super.read(b);
       if(count>=0)totalBytes += count;
       return count;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
       int count = super.read(b,off,len);
       if(count>=0)totalBytes += count;
       return count;
    }

    @Override
    public long skip(long n) throws IOException {
       long count = super.skip(n);
       if(count>=0)totalBytes += count;
       return count;
    }

    public byte[] readAllBytes() throws IOException{
    	byte[] count = super.readAllBytes();
    	if(count.length>=0)totalBytes += count.length;
    	return count;
    }
    
    @Override
    public void close() throws IOException {
    	if(super.in!=System.in) super.close();
    }
    
    /**
     * @return the number of bytes read
     */
    public long byteCount() {return totalBytes;}
}
