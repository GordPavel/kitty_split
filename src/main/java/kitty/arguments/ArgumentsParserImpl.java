package kitty.arguments;

import kitty.arguments.exceptions.IllegalArgumentsException;
import kitty.arguments.exceptions.IllegalInputFilePathException;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ArgumentsParserImpl implements ArgumentsParser {
    @Override
    public Arguments parseArguments(String... arguments) {
        final var argList = Arrays.asList(arguments);

        final Supplier<Reader> input = () -> getFileInputReader(argList, "-i")
                .or(() -> getFileInputReader(argList, "--input"))
                .orElseGet(() -> new InputStreamReader(System.in));

        final Supplier<Writer> output = () -> getFileOutputWriter(argList, "-o")
                .or(() -> getFileOutputWriter(argList, "--output"))
                .orElseGet(() -> new OutputStreamWriter(System.out));

        return new Arguments(input, output);
    }

    private Optional<Reader> getFileInputReader(List<String> arguments, String paramName) {
        final var paramPos = arguments.indexOf(paramName);
        if (paramPos == -1) {
            return Optional.empty();
        }

        final String inputFilePath;
        try {
            inputFilePath = arguments.get(paramPos + 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentsException(String.format("Param %s specified without path", paramName), e);
        }
        final FileReader fileReader;
        try {
            fileReader = new FileReader(inputFilePath);
        } catch (FileNotFoundException e) {
            throw new IllegalInputFilePathException(String.format("Path %s not found", inputFilePath), e);
        }

        return Optional.of(fileReader);
    }

    private Optional<Writer> getFileOutputWriter(List<String> arguments, String paramName) {
        final var paramPos = arguments.indexOf(paramName);
        if (paramPos == -1) {
            return Optional.empty();
        }

        final String outputFilePath;
        try {
            outputFilePath = arguments.get(paramPos + 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentsException(String.format("Param %s specified without path", paramName), e);
        }
        final FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(outputFilePath);
        } catch (IOException e) {
            throw new IllegalInputFilePathException(String.format("Error writing to file %s", outputFilePath), e);
        }

        return Optional.of(fileWriter);
    }
}
