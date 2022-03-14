package kitty.arguments;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.io.Reader;
import java.io.Writer;
import java.util.function.Supplier;

@Value
@EqualsAndHashCode(exclude = {
        "input",
        "output",
})
@ToString(exclude = {
        "input",
        "output",
})
public class Arguments {
    Supplier<Reader> input;
    Supplier<Writer> output;
}
