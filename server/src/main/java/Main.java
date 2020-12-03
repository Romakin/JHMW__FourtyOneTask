import Controllers.ServerController;

/**
 * Задание выполнено в 3ех разных модулях:
 *  - Server - это реализация много поточного сервера в DDD-стиле
 *  - Client(1|2) - это реализация простейших клиентов в функциональном стиле.
 *      Клиенты не содержат никакой логики, кроме чтения настроек и записи логов.
 *
 *  Фактическое написание тестов было опущено по причине отсутствия области их применения.
 *  Все тестирование заключалось именно в интеграционных тестах, которые производились вручную.
 */
public class Main {

    public static void main(String[] args) {
        Runnable control = new ServerController(System.out, System.in);
        new Thread(control, "ControllerThread").start();
    }
}
