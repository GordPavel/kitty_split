package kitty.split;

import kitty.split.exceptions.ErrorProcessingData;
import kitty.utils.DefaultHashMap;
import lombok.Value;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class BillSplitter {
    public OutputData splitBill(InputData inputData) {
        final Map<Boolean, List<ParticipantBalance>> balances = calculateBalances(inputData);
        final Map<String, Map<String, BigDecimal>> transactions = calculateTransactions(
                balances,
                inputData.getParticipants()
        );
        return new OutputData(inputData.getParticipants(), transactions);
    }

    private Map<Boolean, List<ParticipantBalance>> calculateBalances(InputData inputData) {
        final Map<String, BigDecimal> incomes = new DefaultHashMap<>(
                BigDecimal.ZERO,
                inputData.getParticipants().size()
        );
        final Map<String, BigDecimal> outcomes = new DefaultHashMap<>(
                BigDecimal.ZERO,
                inputData.getParticipants().size()
        );
        int participantIndex;
        for (Spending spending : inputData.getSpendings()) {
            try {
                final String payer = spending.getPayer();

                if (!inputData.getParticipants().contains(payer)) {
                    throw new ErrorProcessingData(format(
                            "За товар %s расплатился неизвестный участник %s",
                            spending.getGood(), payer
                    ));
                }

                participantIndex = 0;
                for (BigDecimal amount : spending.getParts()) {
                    try {
                        final var incomer = inputData.getParticipants().get(participantIndex);
                        incomes.put(incomer, incomes.get(incomer).add(amount));
                        participantIndex++;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        throw new ErrorProcessingData(
                                format("Ошибка в трате %s участником %s", spending.getGood(), payer),
                                e
                        );
                    }
                }

                final BigDecimal spendingSum = spending.getParts().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                final BigDecimal newPayerOutcomeValue = outcomes.get(payer).add(spendingSum);
                outcomes.put(payer, newPayerOutcomeValue);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ErrorProcessingData(format("За товар %s указаны некорректные данные", spending.getGood()));
            }
        }

        incomes.forEach((participant, incomeSum) -> outcomes.putIfAbsent(participant, BigDecimal.ZERO));

        return outcomes.entrySet().stream()
                .map(participantOutcomeEntry -> {
                    final BigDecimal income = incomes.get(participantOutcomeEntry.getKey());
                    final BigDecimal outcome = participantOutcomeEntry.getValue();
                    final BigDecimal delta = outcome.subtract(income);
                    return new ParticipantBalance(participantOutcomeEntry.getKey(), delta);
                })
                .filter(balance -> !balance.getBalance().equals(BigDecimal.ZERO))
                .sorted(comparing(balance -> balance.getBalance().compareTo(BigDecimal.ZERO) < 0 ?
                        balance.getBalance() :
                        balance.getBalance().negate())
                )
                .collect(partitioningBy(
                        balance -> balance.getBalance().compareTo(BigDecimal.ZERO) < 0,
                        mapping(
                                balance -> balance.getBalance().compareTo(BigDecimal.ZERO) < 0 ?
                                        new ParticipantBalance(
                                                balance.getParticipant(),
                                                balance.getBalance().negate()
                                        ) :
                                        new ParticipantBalance(
                                                balance.getParticipant(), balance
                                                .getBalance()
                                        ),
                                toList()
                        )
                ));
    }

    private Map<String, Map<String, BigDecimal>> calculateTransactions(Map<Boolean, List<ParticipantBalance>> balances, List<String> participants) {
        final List<ParticipantBalance> sources = balances.getOrDefault(true, emptyList());
        final List<ParticipantBalance> destinations = balances.getOrDefault(false, emptyList());

        int sourcesPointer = 0;
        int sourcesSize = sources.size();
        int destinationsPointer = 0;
        int destinationsSize = destinations.size();

        final Map<String, Map<String, BigDecimal>> transactions = new HashMap<>();
        for (String participant : participants) {
            transactions.put(participant, participants.stream().collect(toMap(identity(), p -> BigDecimal.ZERO)));
        }
        while (sourcesPointer < sourcesSize && destinationsPointer < destinationsSize) {
            final ParticipantBalance source = sources.get(sourcesPointer);
            final ParticipantBalance destination = destinations.get(destinationsPointer);

            final BigDecimal amountDelta = source.getBalance().subtract(destination.getBalance());
            if (amountDelta.compareTo(BigDecimal.ZERO) < 0) {
                sourcesPointer++;
            } else {
                destinationsPointer++;
            }
            transactions.get(source.getParticipant()).put(destination.getParticipant(), amountDelta.abs());
        }

        return transactions;
    }

    @Value
    private static class ParticipantBalance {
        String participant;
        BigDecimal balance;
    }
}
