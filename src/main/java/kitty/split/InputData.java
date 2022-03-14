package kitty.split;

import lombok.Value;

import java.util.List;

@Value
public class InputData {
    List<String> participants;
    Iterable<Spending> spendings;
}
