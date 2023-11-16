package com.example.slofashion.datamodels;

public class DialogueAction {
    public enum InputType {
        MONEY,
        CONFIRM,
    }

    public enum InputTarget {
        COST,
        CLOTHES,
    }

    /**
     * Whether or not this is a user action or a chatbot action
     */
    public boolean userAction;
    /**
     * The actual message text to display
     */
    public String promptText;
    /**
     * If inputType is CONFIRM, selecting the no button will change dialog step by this much
     */
    public int dialogueSkip;
    /**
     * The type of input the user needs to give
     */
    public InputType inputType;
    /**
     * Where the input will be redirected to
     */
    public InputTarget inputTarget;

    public DialogueAction(boolean userAction, String promptText, int dialogueSkip, InputType inputType, InputTarget inputTarget) {
        this.userAction = userAction;
        this.promptText = promptText;
        this.dialogueSkip = dialogueSkip;
        this.inputType = inputType;
        this.inputTarget = inputTarget;
    }

    public static DialogueAction GenChatbotAction(String promptText) {
        return new DialogueAction(false, promptText, 0, null, null);
    }
}
