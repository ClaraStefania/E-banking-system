package org.poo.bank.commands.userCommands;

import org.poo.bank.Command;
import org.poo.fileio.CommandInput;

import java.util.Map;

public class SetAlias implements Command {
    private final CommandInput command;
    private final Map<String, String> aliases;

    public SetAlias(final CommandInput command, final Map<String, String> aliases) {
        this.command = command;
        this.aliases = aliases;
    }

    /**
     * sets the alias for the account
     */
    @Override
    public void execute() {
        String iban = command.getAccount();
        String alias = command.getAlias();
        aliases.put(alias, iban);
    }
}
