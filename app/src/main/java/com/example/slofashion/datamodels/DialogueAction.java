package com.example.slofashion.datamodels;

public class DialogueAction {
    public boolean userAction;
    public String promptText;
    public int dialogueSkip;
    public String inputType;

    public DialogueAction(boolean userAction, String promptText, int dialogueSkip, String inputType) {
        this.userAction = userAction;
        this.promptText = promptText;
        this.dialogueSkip = dialogueSkip;
        this.inputType = inputType;
    }

    public static DialogueAction GenChatbotAction(String promptText) {
        return new DialogueAction(false, promptText, 0, null);
    }
}
