package client;
import static utils.Utils.randomString;
import static utils.Utils.randomEmail;
import models.UserData;

    public class UserGenerator {
        static int emailLength = 10;
        static int passwordLength = 10;
        static int nameLength = 20;
        public static UserData randomUser() {
            return new UserData()
                    .withEmail(randomEmail(emailLength))
                    .withPassword(randomString(passwordLength))
                    .withName(randomString(nameLength));
        }

        public static UserData noEmailUser() {
            return new UserData()
                    .withPassword(randomString(passwordLength))
                    .withName(randomString(nameLength));
        }

        public static UserData noPasswordUser() {
            return new UserData()
                    .withEmail(randomEmail(emailLength))
                    .withName(randomString(nameLength));
        }

        public static UserData noNameUser() {
            return new UserData()
                    .withEmail(randomString(emailLength))
                    .withPassword(randomString(passwordLength));
        }
    }
