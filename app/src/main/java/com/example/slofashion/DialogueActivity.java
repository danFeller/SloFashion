package com.example.slofashion;

import static java.util.UUID.randomUUID;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slofashion.datamodels.Author;
import com.example.slofashion.datamodels.DialogueAction;
import com.example.slofashion.datamodels.Message;
import com.example.slofashion.datamodels.UsePrefs;
import com.example.slofashion.datamodels.entities.Budget;
import com.google.android.material.slider.RangeSlider;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Arrays;
import java.util.Date;

public class DialogueActivity extends AppCompatActivity implements View.OnClickListener {

    /** Invoke different conversations based on DialogueType */
    public enum DialogueType {
        ENTER_STORE,
        LEAVE_STORE
    }

    // View fields
    MessagesList messagesList;
    FrameLayout inputContainer;
    Button dialogueDoneBtn;

    // View inputs
    RangeSlider moneyInputSlider;
    Button moneySubmitBtn;
    Button confirmYesBtn;
    Button confirmNoBtn;

    // Adapter to update list
    MessagesListAdapter<Message> adapter;

    // Dialogue action fields
    final DialogueAction[] enterDialogue = new DialogueAction[] {
            DialogueAction.GenChatbotAction("You have ${{cost}} and {{clothes}} clothes left in your budget"),
            DialogueAction.GenChatbotAction("How much are you spending today?"),
            new DialogueAction(true, null, 0, DialogueAction.InputType.MONEY, DialogueAction.InputTarget.COST),
            DialogueAction.GenChatbotAction("You will be left with ${{res_cost}} in the budget after this trip"),
            new DialogueAction(true, null, -4, DialogueAction.InputType.CONFIRM, null),
            DialogueAction.GenChatbotAction("How many clothes are you purchasing today?"),
            new DialogueAction(true, null, 0, DialogueAction.InputType.MONEY, DialogueAction.InputTarget.CLOTHES),
            DialogueAction.GenChatbotAction("You will be left with {{res_clothes}} clothes left in the budget after this trip"),
            new DialogueAction(true, null, -4, DialogueAction.InputType.CONFIRM, null),
    };
    final DialogueAction[] exitDialogue = new DialogueAction[] {
            DialogueAction.GenChatbotAction("You have ${{cost}} and {{clothes}} clothes left in your budget"),
            DialogueAction.GenChatbotAction("How much did you spend?"),
            new DialogueAction(true, null, 0, DialogueAction.InputType.MONEY, DialogueAction.InputTarget.COST),
            DialogueAction.GenChatbotAction("You will be left with ${{res_cost}} in the budget after this trip"),
            new DialogueAction(true, null, -4, DialogueAction.InputType.CONFIRM, null),
            DialogueAction.GenChatbotAction("How many clothes did you purchase?"),
            new DialogueAction(true, null, 0, DialogueAction.InputType.MONEY, DialogueAction.InputTarget.CLOTHES),
            DialogueAction.GenChatbotAction("You will be left with {{res_clothes}} clothes left in the budget after this trip"),
            new DialogueAction(true, null, -4, DialogueAction.InputType.CONFIRM, null),
    };

    final Author chatbotAuthor = new Author("chatbot", "chatbot");
    final Author userAuthor = new Author("user", "user");

    DialogueAction[] targetConversation;
    DialogueType dialogueType;
    int dialogueStep;
    int moneyInputValue;
    int inputTargetCost;
    int inputTargetClothes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String strDialogueType = intent.getStringExtra(Intent.EXTRA_TEXT);
        try {
            dialogueType = DialogueType.valueOf(strDialogueType);
        } catch (IllegalArgumentException | NullPointerException e) {
            // TODO: shouldn't need this here, intent should be passed to get to this activity
            // but this is an alright default
            dialogueType = DialogueType.ENTER_STORE;
        }

        dialogueStep = 0;
        moneyInputValue = 0;
        inputTargetCost = 0;
        inputTargetClothes = 0;

        setContentView(R.layout.activity_dialogue);
        messagesList = findViewById(R.id.messagesList);
        inputContainer = findViewById(R.id.input_container);

        dialogueDoneBtn = findViewById(R.id.dialogue_done_view_budget);

        moneyInputSlider = findViewById(R.id.money_input_slider);
        moneySubmitBtn = findViewById(R.id.money_submit);
        confirmYesBtn = findViewById(R.id.confirm_yes);
        confirmNoBtn = findViewById(R.id.confirm_no);

        dialogueDoneBtn.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            // only persist if we are actually done shopping
            if (dialogueType == DialogueType.LEAVE_STORE) {
                i.putExtra("money_spent", ""+inputTargetCost);
                i.putExtra("item_bought", ""+inputTargetClothes);
            }
            startActivity(i);
        });

        moneyInputSlider.addOnChangeListener((slider, value, fromUser) -> moneyInputValue = Math.round(value));
        moneySubmitBtn.setOnClickListener(this);
        confirmYesBtn.setOnClickListener(this);
        confirmNoBtn.setOnClickListener(this);

        adapter = new MessagesListAdapter<>("user", null);
        messagesList.setAdapter(adapter);

        switch (dialogueType) {
            case ENTER_STORE:
                targetConversation = enterDialogue;
                break;
            case LEAVE_STORE:
                targetConversation = exitDialogue;
                break;
            default:
                targetConversation = null;
                break;
        }

        goToNextUserDialogue();
    }

    protected void goToNextUserDialogue() {
        int totalCost = UsePrefs.getAllExpendituresForCurrentMonth(getApplicationContext()).stream().mapToInt(e -> e.cost).sum();
        int totalClothes = UsePrefs.getAllExpendituresForCurrentMonth(getApplicationContext()).stream().mapToInt(e -> e.clothes).sum();
        Budget budget = UsePrefs.getBudgetForCurrentMonth(getApplicationContext()).get();

        DialogueAction[] conversation = targetConversation;
        for (; dialogueStep < conversation.length && !conversation[dialogueStep].userAction; dialogueStep++) {
            DialogueAction da = conversation[dialogueStep];

            int cost_left = budget.costBudget - totalCost;
            int clothes_left = budget.clothesBudget - totalClothes;
            int res_cost = cost_left - inputTargetCost;
            int res_clothes = clothes_left - inputTargetClothes;

            String fmtPromptText = da.promptText;

            fmtPromptText = fmtPromptText.replaceAll("\\{\\{cost\\}\\}", ""+cost_left);
            fmtPromptText = fmtPromptText.replaceAll("\\{\\{clothes\\}\\}", ""+clothes_left);
            fmtPromptText = fmtPromptText.replaceAll("\\{\\{res_cost\\}\\}", ""+res_cost);
            fmtPromptText = fmtPromptText.replaceAll("\\{\\{res_clothes\\}\\}", ""+res_clothes);
            adapter.addToStart(new Message(randomUUID().toString(), fmtPromptText, chatbotAuthor, new Date()), true);
        }

        if (dialogueStep >= conversation.length) {
            // if no more user dialogue
            String finalConvoMsg = dialogueType == DialogueType.ENTER_STORE
                    ? "No changes were made to the budget, but you can view your progress"
                    : "View the changes in your budget";
            adapter.addToStart(new Message(randomUUID().toString(), finalConvoMsg, chatbotAuthor, new Date()), true);
            findViewById(R.id.money_input_layout).setVisibility(View.GONE);
            findViewById(R.id.confirm_input_layout).setVisibility(View.GONE);
            findViewById(R.id.dialogue_done_view_budget).setVisibility(View.VISIBLE);
        } else {
            // else setup input methods
            DialogueAction curStep = conversation[dialogueStep];
            switch (curStep.inputType) {
                case MONEY:
                    findViewById(R.id.money_input_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.confirm_input_layout).setVisibility(View.GONE);
                    break;
                case CONFIRM:
                    findViewById(R.id.money_input_layout).setVisibility(View.GONE);
                    findViewById(R.id.confirm_input_layout).setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        int[] validSubmitBtns = new int[] { moneySubmitBtn.getId(), confirmYesBtn.getId(), confirmNoBtn.getId() };
        boolean validSubmit = Arrays.stream(validSubmitBtns).anyMatch(b -> b == view.getId());

        if (!validSubmit) return;

        DialogueAction[] conversation = targetConversation;
        if (dialogueStep < conversation.length && conversation[dialogueStep].userAction) {
            DialogueAction curStep = conversation[dialogueStep];
            boolean isNoBtn = view.getId() == confirmNoBtn.getId();

            String msgText = "";
            switch (curStep.inputType) {
                case MONEY:
                    msgText = "" + moneyInputValue;
                    switch (curStep.inputTarget) {
                        case COST:
                            inputTargetCost = moneyInputValue;
                            break;
                        case CLOTHES:
                            inputTargetClothes = moneyInputValue;
                            break;
                        default:
                            break;
                    }
                    break;
                case CONFIRM:
                    msgText = "" + (isNoBtn ? confirmNoBtn.getText() : confirmYesBtn.getText());
                    break;
            }

            Message userMsg = new Message(randomUUID().toString(), msgText, userAuthor, new Date());
            adapter.addToStart(userMsg, true);

            if (isNoBtn)
                dialogueStep += curStep.dialogueSkip;
            dialogueStep++;
        }

        goToNextUserDialogue();
    }
}