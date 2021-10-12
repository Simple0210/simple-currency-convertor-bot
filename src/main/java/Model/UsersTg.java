package Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersTg {

    private long chatId;
    private State botState;
    private String fromCurrency;
    private String toCurrency;
}
