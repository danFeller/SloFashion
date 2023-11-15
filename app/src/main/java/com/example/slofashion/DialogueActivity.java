package com.example.slofashion;

import static java.util.UUID.randomUUID;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slofashion.datamodels.Author;
import com.example.slofashion.datamodels.DialogueAction;
import com.example.slofashion.datamodels.Message;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Date;

public class DialogueActivity extends AppCompatActivity {
    enum DialogueType {
        ENTER_STORE,
        LEAVE_STORE
    }

    MessagesList messagesList;
    MessagesListAdapter<Message> adapter;
    DialogueType dialogueType;
    int dialogueStep;

    final DialogueAction[] enterDialogue = new DialogueAction[] {
            DialogueAction.GenChatbotAction("You have $x in your budget today"),
            DialogueAction.GenChatbotAction("How much are you spending today?"),
            new DialogueAction(true, null, 0, "money"),
            DialogueAction.GenChatbotAction("Filler 1"),
            DialogueAction.GenChatbotAction("You will be left with $y in the budget after this trip"),
            DialogueAction.GenChatbotAction("Filler 2"),
    };
    final DialogueAction[] exitDialogue = new DialogueAction[] {
            DialogueAction.GenChatbotAction("You have $x in your budget today"),
            DialogueAction.GenChatbotAction("How much did you spend?"),
            new DialogueAction(true, null, 0, "money"),
            DialogueAction.GenChatbotAction("Filler 3"),
            DialogueAction.GenChatbotAction("Done. You will be left with $y in the budget after this trip"),
            DialogueAction.GenChatbotAction("Filler 4"),
    };

    final Author chatbotAuthor = new Author("chatbot", "chatbot");
    final Author userAuthor = new Author("user", "user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String strDialogueType = intent.getStringExtra(Intent.EXTRA_REFERRER_NAME);
        try {
            dialogueType = DialogueType.valueOf(strDialogueType);
        } catch (IllegalArgumentException | NullPointerException e) {
            // TODO: shouldn't need this here, intent should be passed to get to this activity
            dialogueType = DialogueType.ENTER_STORE;
        }

        dialogueStep = 0;

        setContentView(R.layout.activity_dialogue);
        messagesList = findViewById(R.id.messagesList);

        adapter = new MessagesListAdapter<>("user", null);
        messagesList.setAdapter(adapter);

        DialogueAction[] targetConversation = dialogueType.equals(DialogueType.ENTER_STORE)
                ? enterDialogue
                : exitDialogue;
        goToNextUserDialogue(targetConversation);
    }

    protected void goToNextUserDialogue(DialogueAction[] conversation) {
        for (; dialogueStep < conversation.length && !conversation[dialogueStep].userAction; dialogueStep++) {
            DialogueAction da = conversation[dialogueStep];
            adapter.addToStart(new Message(randomUUID().toString(), da.promptText, chatbotAuthor, new Date()), true);
        }
        if (dialogueStep >= conversation.length)
            adapter.addToStart(new Message(randomUUID().toString(), "View your budget now", chatbotAuthor, new Date()), true);

        // TODO: currently simulate user interaction, should just create input element and pick up logic at onSubmit handler
        if (dialogueStep < conversation.length && conversation[dialogueStep].userAction) {
            adapter.addToStart(new Message(randomUUID().toString(), "$25", userAuthor, new Date()), true);
            dialogueStep++;
        }
        if (dialogueStep < conversation.length)
            goToNextUserDialogue(conversation);
    }
}