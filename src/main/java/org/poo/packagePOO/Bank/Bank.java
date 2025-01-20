package org.poo.packagePOO.Bank;

import org.poo.fileio.*;
import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Command.Command;
import org.poo.packagePOO.Command.CommandFactory;
import org.poo.packagePOO.GlobalManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public final class Bank {
    private final CommandFactory commandFactory = new CommandFactory();
    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<ExchangeRate> exchangeRates = new ArrayList<>();
    private final ArrayList<BankAccount> accounts = new ArrayList<>();
    private final Map<String, Map<String, String>> aliases = new HashMap<>();
    private final Map<String, Commerciant> commerciants = new HashMap<>();
    private final Map<String, Queue<SplitPaymentRequest>> pendingSplitPayments
            = new HashMap<>();

    /**
     *
     * @param request
     */
    public void addPendingSplitPayment(final SplitPaymentRequest request) {
        for (String account : request.getAccounts()) {
            BankAccount bankAccount = getAccountIBAN(account);
            if (bankAccount != null) {
                String email = bankAccount.getEmail();
                pendingSplitPayments.computeIfAbsent(email,
                        k -> new LinkedList<>()).add(request);
            }
        }
    }

    /**
     *
     * @param email
     * @return
     */
    public SplitPaymentRequest getNextPendingSplitPayment(final String email) {
        Queue<SplitPaymentRequest> requests = pendingSplitPayments.get(email);
        if (requests == null || requests.isEmpty()) {
            return null;
        }
        return requests.peek();
    }

    /**
     *
     * @param email
     */
    public void removePendingSplitPayment(final String email) {
        Queue<SplitPaymentRequest> requests = pendingSplitPayments.get(email);
        if (requests != null && !requests.isEmpty()) {
            requests.poll();
        }
    }

    /**
     *
     * @return
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     *
     * @return
     */
    public ArrayList<ExchangeRate> getExchangeRates() {
        return exchangeRates;
    }

    /**
     *
     * @return
     */
    public ArrayList<BankAccount> getAccounts() {
        return accounts;
    }

    /**
     *
     * @param inputs
     */
    public void addUsersFromInput(final UserInput[] inputs) {
        for (UserInput input : inputs) {
            users.add(new User(input.getFirstName(),
                    input.getLastName(),
                    input.getEmail(),
                    input.getBirthDate(),
                    input.getOccupation()));
        }
    }

    /**
     *
     * @param rates
     */
    public void addExchangeRatesFromInput(final ExchangeInput[] rates) {
        for (ExchangeInput rate : rates) {
            exchangeRates.add(new ExchangeRate(rate.getFrom(),
                    rate.getTo(),
                    rate.getRate()));
        }
    }

    /**
     *
     * @param inputData
     */
    public void initializeBank(final ObjectInput inputData) {
        GlobalManager.getGlobal().getBank().addUsersFromInput(inputData.getUsers());
        GlobalManager.getGlobal().getBank()
                .addExchangeRatesFromInput(inputData.getExchangeRates());
        GlobalManager.getGlobal().getBank()
                .addCommerciantsFromInput(inputData.getCommerciants());
    }

    /**
     *
     * @param account
     */
    public void addAccount(final BankAccount account) {
        accounts.add(account);
    }

    /**
     *
     * @param account
     */
    public void removeAccount(final BankAccount account) {
        accounts.remove(account);
    }

    /**
     *
     * @param iban
     * @return
     */
    public BankAccount getAccountIBAN(final String iban) {
        for (BankAccount account : accounts) {
            if (iban.equals(account.getIBAN())) {
                return account;
            }
        }
        return null;
    }

    /**
     *
     * @param cardNumber
     * @return
     */
    public BankAccount getAccountCard(final String cardNumber) {
        for (BankAccount account : accounts) {
            if (account.searchCard(cardNumber) != null) {
                return account;
            }
        }
        return null;
    }

    /**
     *
     * @param email
     * @return
     */
    public User getUserEmail(final String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    /**
     *
     * @param email
     * @param alias
     * @param iban
     */
    public void addAlias(final String email,
                         final String alias,
                         final String iban) {
        aliases.computeIfAbsent(email, k -> new HashMap<>()).put(alias, iban);
    }

    /**
     *
     * @param commandInput
     */
    public void executeCommand(final CommandInput commandInput) {
        Command command = commandFactory.createCommand(commandInput);
        if (command != null) {
            command.execute();
        }
    }

    /**
     *
     * @param email
     * @param currency
     * @return
     */
    public BankAccount getClassicAccountEmailCurrency(final String email,
                                                      final String currency) {
        for (BankAccount account : accounts) {
            if (account.getEmail().equals(email)
                    && account.getAccountType().equals("classic")
                    && account.getCurrency().equals(currency)) {
                return account;
            }
        }
        return null;
    }

    /**
     *
     * @param commerciant
     */
    public void addCommerciant(final Commerciant commerciant) {
        commerciants.put(commerciant.getName(), commerciant);
    }

    /**
     *
     * @param name
     * @return
     */
    public Commerciant getCommerciant(final String name) {
        return commerciants.get(name);
    }

    /**
     *
     * @param inputs
     */
    public void addCommerciantsFromInput(final CommerciantInput[] inputs) {
        for (CommerciantInput input : inputs) {
            commerciants.put(input.getCommerciant(),
                    new Commerciant(
                            input.getCommerciant(),
                            input.getId(),
                            input.getAccount(),
                            input.getType(),
                            input.getCashbackStrategy()
                    ));
        }
    }
}
