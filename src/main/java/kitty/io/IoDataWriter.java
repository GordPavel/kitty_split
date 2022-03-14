package kitty.io;

import kitty.split.OutputData;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class IoDataWriter implements DataWriter {
    @Override
    public void writeData(OutputData data, Writer writer) {
        final var printWriter = new PrintWriter(new BufferedWriter(writer));
        try {
            printWriter.println(data.getParticipants().stream().map(participant -> "," + participant).collect(joining()));
            for (String participant : data.getParticipants()) {
                printWriter.print(participant);
                final Map<String, BigDecimal> participantDebts = data.getTransactions().get(participant);
                printWriter.println(
                        data.getParticipants().stream()
                                .map(participantDebts::get)
                                .map(amount -> "," + amount.toString())
                                .collect(joining())
                );
            }
        } finally {
            printWriter.flush();
        }
    }
}
