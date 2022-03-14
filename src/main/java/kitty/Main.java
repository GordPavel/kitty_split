package kitty;

import kitty.arguments.Arguments;
import kitty.arguments.ArgumentsParser;
import kitty.arguments.ArgumentsParserImpl;
import kitty.exceptions.UserInputException;
import kitty.io.DataReader;
import kitty.io.DataWriter;
import kitty.io.IoDataReader;
import kitty.io.IoDataWriter;
import kitty.split.BillSplitter;
import kitty.split.InputData;
import kitty.split.OutputData;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

public class Main {

    private static final ArgumentsParser argumentsParser = new ArgumentsParserImpl();
    private static final PrintWriter errorsWriter = new PrintWriter(System.err);

    private static final DataReader dataReader = new IoDataReader();
    private static final BillSplitter billSplitter = new BillSplitter();
    private static final DataWriter dataWriter = new IoDataWriter();

    public static void main(String[] args) {
        Arguments arguments = argumentsParser.parseArguments(args);
        try (Reader reader = arguments.getInput().get()) {
            final InputData inputData = dataReader.readInput(reader);
            final OutputData outputData = billSplitter.splitBill(inputData);
            try (Writer writer = arguments.getOutput().get()) {
                dataWriter.writeData(outputData, writer);
            }
        } catch (UserInputException e) {
            System.out.println("Вы ввели неверные данные");
            System.out.println(e.getMessage());
            e.printStackTrace(errorsWriter);
        } catch (Exception e) {
            System.out.println("Извините, произошла неизвестная ошибка");
            e.printStackTrace(errorsWriter);
        }
    }

}
