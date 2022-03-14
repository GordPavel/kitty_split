package kitty.split;

import lombok.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Value
public class OutputData {
    List<String> participants;
    Map<String, Map<String, BigDecimal>> transactions;
}
