package utils;
import java.util.Random;

public class Utils {

    public static String randomString(int length) {
        Random random = new Random();
        int leftLimit = 97;
        int rightLimit = 122;
        StringBuilder buffer = new StringBuilder(length);

        for(int i = 0; i < length; ++i) {
            int randomLimitedInt = leftLimit + (int)(random.nextFloat() * (float)(rightLimit - leftLimit + 1));
            buffer.append(Character.toChars(randomLimitedInt));
        }

        return buffer.toString();
    }

    public static String randomEmail(int length) {
        String randomString = randomString(length);
        return randomString.toLowerCase() + "@ussr.ru";
    }

    public static int randomNumber(int number) {
        Random rand = new Random();
        return rand.nextInt(number);
    }
}
