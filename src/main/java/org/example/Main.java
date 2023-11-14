import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class  Main extends TelegramLongPollingBot {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(new Main());
    }
    @Override
    public String getBotUsername() {
        return "Gidrologichna_sutyatsia_bot";
    }
    @Override
    public String getBotToken() {
        return "code from FatherBot";
    }
    @Override
    public void onUpdateReceived(Update update) {
        Long chatId=getChatId(update);
        if(update.hasMessage()&&update.getMessage().getText().equals("/start"))
        {
            SendMessage message = createMessage("Push the button");
            message.setChatId(chatId);
            attachButtons(message, Map.of("BUTTON", "button1"));
            sendApiMethodAsync(message);
        }
        if (update.hasCallbackQuery())
        {
            if(update.getCallbackQuery().getData().equals("button1"))
            {
                sendImage("src/images/test.gif", chatId);
            }
        }
    }
    public Long getChatId(Update update)
    {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getId();

        }
        if (update.hasCallbackQuery())
        {
            return update.getCallbackQuery().getFrom().getId();
        }

        return null;
    }
    public SendMessage createMessage (String text)
    {
        SendMessage message = new SendMessage();
        message.setText(new String(text.getBytes(), StandardCharsets.UTF_8));
        message.setText(text);
        message.setParseMode("markdown");
        return message;
    }
    public void attachButtons(SendMessage message, Map<String,String> buttons)
    {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (String buttonName : buttons.keySet())
        {
            String buttonValue = buttons.get(buttonName);
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonName);
            button.setCallbackData(buttonValue);

            keyboard.add (Arrays.asList(button));
        }
        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);
    }
    /// Send image to recipient
    public void sendImage(String path, Long chatId)
    {
        SendAnimation animation = new SendAnimation();

        InputFile inputFile = new InputFile();
        inputFile.setMedia(new File(path));
        animation.setAnimation(inputFile);
        animation.setChatId(chatId);

        executeAsync(animation);

    }

}