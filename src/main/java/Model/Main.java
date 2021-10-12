package Model;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;

public class Main extends TelegramLongPollingBot {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi tgApi=new TelegramBotsApi();

        try {
            tgApi.registerBot(new Main());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            String text = update.getMessage().getText();
            if (text.equals("/start")) {
                try {
                    execute(SimpleCCBotService.start(update));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else {
                UsersTg user = SimpleCCBotService.findUser(update);
                if (user.getBotState().equals(State.ENTER_AMOUNT)){
                    try {
                        execute(SimpleCCBotService.getAmount(update));
                    } catch (TelegramApiException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        }else if (update.getCallbackQuery()!=null){
            String callbackData = update.getCallbackQuery().getData();
            if (callbackData.contains("/")){
                try {
                    execute(SimpleCCBotService.getCurrency(update));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }



    }

    @Override
    public String getBotUsername() {
        return "SimpleCCBot";
    }

    @Override
    public String getBotToken() {
        return "1774730675:AAHzosCeByGJX10q5qBQbeBGLz15n6P4X0Y";
    }
}
