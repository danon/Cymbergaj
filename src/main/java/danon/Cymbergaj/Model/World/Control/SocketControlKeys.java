package danon.Cymbergaj.Model.World.Control;

import danon.Chat.KeyMessage;

import java.awt.event.KeyEvent;

public class SocketControlKeys extends ControlKeys {
    @Override
    public boolean verifyUp(KeyEvent event) {
        return false;
    }

    @Override
    public boolean verifyDown(KeyEvent event) {
        return false;
    }

    @Override
    public boolean verifyLeft(KeyEvent event) {
        return false;
    }

    @Override
    public boolean verifyRight(KeyEvent event) {
        return false;
    }

    public void acceptKeyChange(KeyMessage message) {
        keys.setUp(message.isKeyUp());
        keys.setDown(message.isKeyDown());
        keys.setLeft(message.isKeyLeft());
        keys.setRight(message.isKeyRight());
    }
}