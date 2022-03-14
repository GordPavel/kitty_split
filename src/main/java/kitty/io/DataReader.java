package kitty.io;

import kitty.split.InputData;

import java.io.Reader;

public interface DataReader {
    InputData readInput(Reader reader);
}
