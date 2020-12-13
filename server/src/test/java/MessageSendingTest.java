import Models.ServerModel;
import Models.UserConnectionModel;
import Repositories.MessagesRep;
import Services.ServerService;
import Services.UserConnectionService;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class MessageSendingTest {

    /**
     * В задании реализован "обратный поток" сообщений.
     *  Т.е. когда кто то создает сообщение в чате, то
     *      остальные пользователи при вводе /update или
     *      своего сообщения получат и все новые сообщения
     *      из чата.
     *  Данный тест позволяет проверить работоспособность механизма.
     */
    @org.junit.Test
    public void testSending() {
        ServerModel sm = new ServerModel(-1, false);
        MessagesRep chart = new MessagesRep("test");
        UserConnectionModel ucm1 = prepareUserConnection("test1", sm),
                ucm2 = prepareUserConnection("test2", sm);

        Assertions.assertTrue(sm.getUserConns().asList().size() == 2);

        ucm1.getUser().setChart(chart);
        ucm2.getUser().setChart(chart);

        String msg = "test message";
        UserConnectionService.get().sendMessage(msg, ucm1);
        List<String> msgs = ucm2.getNewMessages();

        Assertions.assertTrue(msgs.size() == 1);
        Assertions.assertEquals(msgs.get(0), msg);
    }

    private UserConnectionModel prepareUserConnection(String login, ServerModel sm) {
        UserConnectionModel ucm = UserConnectionService.get().connection(null, sm);
        ucm.setUser(login);
        ServerService.get().addUserConnection(sm, ucm);
        return ucm;
    }
}
