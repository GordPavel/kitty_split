package kitty.split;

import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
public class Spending {
    String payer;
    String good;
    List<BigDecimal> parts;
}
