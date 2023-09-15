package dev.galasa.framework.api.cps.internal.mocks;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class MockServletInputStream extends ServletInputStream {

    String content;

    public MockServletInputStream(String content) {
        this.content = content;
    }
    @Override
    public boolean isFinished() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isFinished'");
    }

    @Override
    public boolean isReady() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isReady'");
    }

    @Override
    public void setReadListener(ReadListener arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setReadListener'");
    }

    @Override
    public int read() throws IOException {
        return this.content.length();
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        return this.content.getBytes(StandardCharsets.UTF_8);
    }
    
}
