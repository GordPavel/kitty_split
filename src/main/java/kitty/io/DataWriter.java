package kitty.io;

import kitty.split.OutputData;

import java.io.Writer;

public interface DataWriter {
    void writeData(OutputData data, Writer writer);
}
