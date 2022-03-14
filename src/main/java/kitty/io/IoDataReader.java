package kitty.io;

import kitty.io.exceptions.IllegalInputData;
import kitty.split.InputData;
import kitty.split.Spending;
import lombok.RequiredArgsConstructor;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class IoDataReader implements DataReader {
    private static final String CSV_DELIMITER = "\\s*,\\s*";

    @Override
    public InputData readInput(Reader reader) {
        final var scanner = new Scanner(reader);
        final var header = scanner.nextLine().split(CSV_DELIMITER);
        final var participants = asList(header).subList(2, header.length);
        String csvLinePatternChecker = format("^.+\\s*,\\s*.+(\\s*,\\s*.+){%d}$", participants.size());
        final var spendings = new SpendingsIterable(scanner, csvLinePatternChecker);
        return new InputData(participants, spendings);
    }

    @RequiredArgsConstructor
    private static class SpendingsIterable implements Iterable<Spending> {
        private final Scanner scanner;
        private final String csvLinePatternChecker;

        @Override
        public Iterator<Spending> iterator() {
            return new Iterator<>() {

                String nextLine;

                @Override
                public boolean hasNext() {
                    while (scanner.hasNextLine()) {
                        final var l = scanner.nextLine();
                        if (l.isBlank()) continue;
                        if (!l.matches(csvLinePatternChecker))
                            throw new IllegalInputData(format("Ошибка в строке %s", l));
                        nextLine = l;
                        return true;
                    }
                    nextLine = null;
                    return false;
                }

                @Override
                public Spending next() {
                    if (isNull(nextLine)) throw new NoSuchElementException();
                    final var spending = nextLine.split(CSV_DELIMITER);
                    final String payer;
                    final String good;
                    final List<String> parts;
                    try {
                        payer = spending[0];
                        good = spending[1];
                        parts = asList(spending).subList(2, spending.length);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        throw new IllegalInputData("Ошибка обработки строки " + nextLine, e);
                    }
                    return new Spending(
                            payer,
                            good,
                            parts.stream()
                                    .map(val -> {
                                        try {
                                            return new BigDecimal(val);
                                        } catch (NumberFormatException e) {
                                            throw new IllegalInputData(format(
                                                    "Ошибка обработки значения %s в строке %s",
                                                    val, nextLine
                                            ), e);
                                        }
                                    })
                                    .collect(toList())
                    );
                }
            };
        }
    }

}
