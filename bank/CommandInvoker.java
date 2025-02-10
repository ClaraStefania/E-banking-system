package org.poo.bank;

class CommandInvoker {
    private Command command;

    /**
     * sets the command
     * @param command the new value of command
     */
    public void setCommand(final Command command) {
        this.command = command;
    }

    /**
     * executes the command
     */
    public void executeCommand() {
        if (command != null) {
            command.execute();
        }
    }
}
