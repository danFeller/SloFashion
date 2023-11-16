package com.example.slofashion.datamodels;

public class DialogueAction {
    public enum InputType {
        MONEY,
        CONFIRM,
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

    public DialogueAction(boolean userAction, String promptText, int dialogueSkip, InputType inputType) {
        this.userAction = userAction;
        this.promptText = promptText;
        this.dialogueSkip = dialogueSkip;
        this.inputType = inputType;
    }

    public static DialogueAction GenChatbotAction(String promptText) {
        return new DialogueAction(false, promptText, 0, null);
    }
}
