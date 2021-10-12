package Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class SimpleCCBotService {

     static List<UsersTg> usersTgList=new ArrayList<>();

    public static SendMessage start(Update update) {
        SendMessage sendMessage = new SendMessage();
        UsersTg users=findUser(update);
        String firstName = update.getMessage().getFrom().getFirstName();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setText("\uD83C\uDDFA\uD83C\uDDFF Salom *" + firstName + "* ! Valyuta konverter Botga xush kelibsiz!\n" +
                        "\uD83C\uDDFA\uD83C\uDDF8 Hello *" + firstName + "* ! Welcome to Currency Converter Bot!\n" +
                        "\uD83C\uDDF7\uD83C\uDDFA Привет *" + firstName + "* ! Добро пожаловать в бот-конвертер валют!\n" +
                        "\n" +
                        "\uD83C\uDDFA\uD83C\uDDFF Konverterni tanlang!  \uD83C\uDDFA\uD83C\uDDF8 Choose a converter!\n\uD83C\uDDF7\uD83C\uDDFA Выбери конвертер!  \uD83D\uDC47");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        List<List<InlineKeyboardButton>> inlineRowlist = new ArrayList<>();
        List<InlineKeyboardButton> inlineRow = new ArrayList<>();

        InlineKeyboardButton buton1 = new InlineKeyboardButton("\uD83C\uDDFA\uD83C\uDDF8 USD-SUM \uD83C\uDDFA\uD83C\uDDFF");
        buton1.setCallbackData("USD/SUM");
        InlineKeyboardButton buton2 = new InlineKeyboardButton("\uD83C\uDDFA\uD83C\uDDFF SUM-USD \uD83C\uDDFA\uD83C\uDDF8");
        buton2.setCallbackData("SUM/USD");

        inlineRow.add(buton1);
        inlineRow.add(buton2);

        List<InlineKeyboardButton> inlineRow2 = new ArrayList<>();
        InlineKeyboardButton but = new InlineKeyboardButton("\uD83C\uDDEA\uD83C\uDDFA EUR-SUM \uD83C\uDDFA\uD83C\uDDFF");
        but.setCallbackData("EUR/SUM");
        InlineKeyboardButton but2 = new InlineKeyboardButton("\uD83C\uDDFA\uD83C\uDDFF SUM-EUR \uD83C\uDDEA\uD83C\uDDFA");
        but2.setCallbackData("SUM/EUR");

        inlineRow2.add(but);
        inlineRow2.add(but2);

        List<InlineKeyboardButton> inlineRow3 = new ArrayList<>();
        InlineKeyboardButton but3 = new InlineKeyboardButton("\uD83C\uDDE8\uD83C\uDDF3 CNY-SUM \uD83C\uDDFA\uD83C\uDDFF");
        but3.setCallbackData("CNY/SUM");
        InlineKeyboardButton but4 = new InlineKeyboardButton("\uD83C\uDDFA\uD83C\uDDFF SUM-CNY \uD83C\uDDE8\uD83C\uDDF3");
        but4.setCallbackData("SUM/CNY");

        inlineRow3.add(but3);
        inlineRow3.add(but4);

        inlineRowlist.add(inlineRow);
        inlineRowlist.add(inlineRow2);
        inlineRowlist.add(inlineRow3);
        inlineKeyboardMarkup.setKeyboard(inlineRowlist);

        return sendMessage;
    }

    public static UsersTg findUser(Update update) {
        long chatId = update.getCallbackQuery() != null ? update.getCallbackQuery().getMessage().getChatId() : update.getMessage().getChatId();

        for (UsersTg user : usersTgList) {
            if (user.getChatId() == chatId) {

                return user;
            }
        }

        UsersTg tgUser = new UsersTg(chatId, State.CHOOSE_CURRENCY, null, null);
        usersTgList.add(tgUser);
        return tgUser;


    }
    public static SendMessage getCurrency(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String currency = update.getCallbackQuery().getData();
        String fromCurrency = currency.substring(0, currency.indexOf("/"));
        String toCurrency = currency.substring(currency.indexOf("/") + 1);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        UsersTg user = findUser(update);
        user.setFromCurrency(fromCurrency);
        user.setToCurrency(toCurrency);
        user.setBotState(State.ENTER_AMOUNT);
        updateUser(user);
        if (fromCurrency.equals("USD")) {
            sendMessage.setText("\uD83C\uDDFA\uD83C\uDDFF Miqdorni kiriting *dollarda!* \uD83C\uDDFA\uD83C\uDDF8 Enter the amount in *dollars!*\n\uD83C\uDDF7\uD83C\uDDFA Введите сумму в *долларах!*   ↙  ️");
            sendMessage.setChatId(chatId);
        } else if (fromCurrency.equals("EUR")) {
            sendMessage.setText("\uD83C\uDDFA\uD83C\uDDFF Miqdorni kiriting *yevroda!* \uD83C\uDDFA\uD83C\uDDF8 Enter the amount in *euros!*\n\uD83C\uDDF7\uD83C\uDDFA Введите сумму в *евро!*   ↙ ️️");
            sendMessage.setChatId(chatId);
        } else if (fromCurrency.equals("CNY")) {
            sendMessage.setText("\uD83C\uDDFA\uD83C\uDDFF Miqdorni kiriting *yuanda!* \uD83C\uDDFA\uD83C\uDDF8 Enter the amount in *yuan!*\n\uD83C\uDDF7\uD83C\uDDFA Введите сумму в *юанях!*   ↙ ️");
            sendMessage.setChatId(chatId);
        } else if (toCurrency.equals("USD") || toCurrency.equals("EUR") || toCurrency.equals("CNY")) {
            sendMessage.setText("\uD83C\uDDFA\uD83C\uDDFF Miqdorni kiriting *so`mda!* \uD83C\uDDFA\uD83C\uDDF8Enter the amount in *soums!*\n\uD83C\uDDF7\uD83C\uDDFA Введите сумму в *сумах!*  ↙ ️");
            sendMessage.setChatId(chatId);
        }

        return sendMessage;
    }
    public static void updateUser(UsersTg user) {
        for (UsersTg tgUser : usersTgList) {
            if (user.getChatId() == tgUser.getChatId()) {
                tgUser = user;
                break;
            }
        }
    }
    public static SendMessage getAmount(Update update) throws IOException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(update.getMessage().getChatId());

        double amount = Double.parseDouble(update.getMessage().getText());
        UsersTg user = findUser(update);

        String fromCurrency = user.getFromCurrency();
        String toCurrency = user.getToCurrency();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/"+(fromCurrency.equals("SUM")?toCurrency:fromCurrency)+"/");
        URLConnection urlConnection = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        Currensy[]  currency=gson.fromJson(reader,Currensy[].class);
        double result=0;
        if (fromCurrency.equals("SUM")){
            result =  (amount/(Double.parseDouble(currency[0].getRate())));
        }else {
            result =  (amount*(Double.parseDouble(currency[0].getRate())));
        }
        sendMessage.setText("*Result* : "+(long)amount+" "+fromCurrency+" = "+result+" "+toCurrency+"\n" +
                "\uD83C\uDDFA\uD83C\uDDFF Yana konverterni tanlang!  \uD83C\uDDFA\uD83C\uDDF8 Choose another converter!\n\uD83C\uDDF7\uD83C\uDDFA Выбери другой конвертер!  \uD83D\uDC47" );


        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();


        List<List<InlineKeyboardButton>> inlineRowlist = new ArrayList<>();
        List<InlineKeyboardButton> inlineRow = new ArrayList<>();

        InlineKeyboardButton buton1 = new InlineKeyboardButton("\uD83C\uDDFA\uD83C\uDDF8 USD-SUM \uD83C\uDDFA\uD83C\uDDFF");
        buton1.setCallbackData("USD/SUM");
        InlineKeyboardButton buton2 = new InlineKeyboardButton("\uD83C\uDDFA\uD83C\uDDFF SUM-USD \uD83C\uDDFA\uD83C\uDDF8");
        buton2.setCallbackData("SUM/USD");

        inlineRow.add(buton1);
        inlineRow.add(buton2);

        List<InlineKeyboardButton> inlineRow2 = new ArrayList<>();
        InlineKeyboardButton but = new InlineKeyboardButton("\uD83C\uDDEA\uD83C\uDDFA EUR-SUM \uD83C\uDDFA\uD83C\uDDFF");
        but.setCallbackData("EUR/SUM");
        InlineKeyboardButton but2 = new InlineKeyboardButton("\uD83C\uDDFA\uD83C\uDDFF SUM-EUR \uD83C\uDDEA\uD83C\uDDFA");
        but2.setCallbackData("SUM/EUR");

        inlineRow2.add(but);
        inlineRow2.add(but2);

        List<InlineKeyboardButton> inlineRow3 = new ArrayList<>();
        InlineKeyboardButton but3 = new InlineKeyboardButton("\uD83C\uDDE8\uD83C\uDDF3 CNY-SUM \uD83C\uDDFA\uD83C\uDDFF");
        but3.setCallbackData("CNY/SUM");
        InlineKeyboardButton but4 = new InlineKeyboardButton("\uD83C\uDDFA\uD83C\uDDFF SUM-CNY \uD83C\uDDE8\uD83C\uDDF3");
        but4.setCallbackData("SUM/CNY");


        inlineRow3.add(but3);
        inlineRow3.add(but4);

        inlineRowlist.add(inlineRow);
        inlineRowlist.add(inlineRow2);
        inlineRowlist.add(inlineRow3);
        inlineKeyboardMarkup.setKeyboard(inlineRowlist);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }


}
