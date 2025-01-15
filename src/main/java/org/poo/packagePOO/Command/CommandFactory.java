package org.poo.packagePOO.Command;

import org.poo.fileio.CommandInput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class CommandFactory {
    private final Map<String, Function<CommandInput, Command>> commandCreators;

    public CommandFactory() {
        this.commandCreators = new HashMap<>();
        registerCommands();
    }

    private void registerCommands() {
        commandCreators.put("addAccount", input -> new AddAccount(
                input.getEmail(),
                input.getCurrency(),
                input.getAccountType(),
                input.getTimestamp(),
                input.getInterestRate()
        ));

        commandCreators.put("createCard", input -> new CreateCard(
                input.getEmail(),
                input.getAccount(),
                input.getTimestamp()
        ));

        commandCreators.put("printUsers", input -> new PrintUsers(
                input.getTimestamp()
        ));

        commandCreators.put("addFunds", input -> new AddFunds(
                input.getAccount(),
                input.getAmount(),
                input.getTimestamp()
        ));

        commandCreators.put("deleteAccount", input -> new DeleteAccount(
                input.getEmail(),
                input.getAccount(),
                input.getTimestamp()
        ));

        commandCreators.put("createOneTimeCard", input -> new CreateOneTimeCard(
                input.getEmail(),
                input.getAccount(),
                input.getTimestamp()
        ));

        commandCreators.put("deleteCard", input -> new DeleteCard(
                input.getCardNumber(),
                input.getEmail(),
                input.getTimestamp()
        ));

        commandCreators.put("setAlias", input -> new SetAlias(
                input.getEmail(),
                input.getAlias(),
                input.getAccount(),
                input.getTimestamp()
        ));

        commandCreators.put("printTransactions", input -> new PrintTransactions(
                input.getEmail(),
                input.getTimestamp()
        ));

        commandCreators.put("payOnline", input -> new PayOnline(
                input.getCardNumber(),
                input.getAmount(),
                input.getCurrency(),
                input.getDescription(),
                input.getCommerciant(),
                input.getEmail(),
                input.getTimestamp()
        ));

        commandCreators.put("sendMoney", input -> new SendMoney(
                input.getAccount(),
                input.getAmount(),
                input.getReceiver(),
                input.getDescription(),
                input.getTimestamp()
        ));

        commandCreators.put("checkCardStatus", input -> new CheckCardStatus(
                input.getCardNumber(),
                input.getTimestamp()
        ));

        commandCreators.put("setMinimumBalance", input -> new SetMinimumBalance(
                input.getAccount(),
                input.getAmount(),
                input.getTimestamp()
        ));

        commandCreators.put("splitPayment", input -> new SplitPayments(
                new ArrayList<>(input.getAccounts()),
                input.getTimestamp(),
                input.getCurrency(),
                input.getAmount(),
                input.getAmountForUsers() != null ?
                        new ArrayList<>(input.getAmountForUsers()) : null,
                input.getSplitPaymentType()
        ));

        commandCreators.put("report", input -> new Report(
                input.getStartTimestamp(),
                input.getEndTimestamp(),
                input.getAccount(),
                input.getTimestamp()
        ));

        commandCreators.put("spendingsReport", input -> new SpendingsReport(
                input.getStartTimestamp(),
                input.getEndTimestamp(),
                input.getAccount(),
                input.getTimestamp()
        ));

        commandCreators.put("addInterest", input -> new AddInterest(
                input.getAccount(),
                input.getTimestamp()
        ));

        commandCreators.put("changeInterestRate", input -> new ChangeInterestRate(
                input.getAccount(),
                input.getInterestRate(),
                input.getTimestamp()
        ));

        commandCreators.put("withdrawSavings", input -> new WithdrawSavings(
                input.getAccount(),
                input.getAmount(),
                input.getCurrency(),
                input.getTimestamp()
        ));

        commandCreators.put("upgradePlan", input -> new UpgradePlan(
                input.getAccount(),
                input.getNewPlanType(),
                input.getTimestamp()
        ));

        commandCreators.put("cashWithdrawal", input -> new CashWithdrawal(
                input.getCardNumber(),
                input.getAmount(),
                input.getEmail(),
                input.getLocation(),
                input.getTimestamp()
        ));

        commandCreators.put("acceptSplitPayment", input -> new AcceptSplitPayment(
                input.getEmail(),
                input.getTimestamp()
        ));

        commandCreators.put("rejectSplitPayment", input -> new RejectSplitPayment(
                input.getEmail(),
                input.getTimestamp()
        ));
    }

    /**
     *
     * @param input
     * @return
     */
    public Command createCommand(final CommandInput input) {
        Function<CommandInput, Command> creator = commandCreators.get(input.getCommand());
        if (creator == null) {
            return null;
        }
        return creator.apply(input);
    }
}
