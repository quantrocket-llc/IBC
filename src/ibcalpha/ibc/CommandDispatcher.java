// This file is part of IBC.
// Copyright (C) 2004 Steven M. Kearns (skearns23@yahoo.com )
// Copyright (C) 2004 - 2018 Richard L King (rlking@aultan.com)
// For conditions of distribution and use, see copyright notice in COPYING.txt

// IBC is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// IBC is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with IBC.  If not, see <http://www.gnu.org/licenses/>.

package ibcalpha.ibc;

import java.awt.event.KeyEvent;
import java.awt.Window;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JTextField;


class CommandDispatcher
        implements Runnable {

    private final CommandChannel mChannel;

    CommandDispatcher(CommandChannel channel) {
        this.mChannel = channel;
    }

    @Override public void run() {
        String cmd = mChannel.getCommand();
        while (cmd != null) {
            String upper = cmd.toUpperCase();
            if (upper.equals("EXIT")) {
                mChannel.writeAck("Goodbye");
                break;
            } else if (upper.equals("STOP")) {
                handleStopCommand();
            } else if (upper.startsWith("ENABLEAPI")) {
                handleEnableAPICommand(cmd);
            } else if (upper.startsWith("SECURITYCODE")) {
                handleEnterCodeCommand(cmd);
            } else if (upper.equals("RECONNECTDATA")) {
            	handleReconnectDataCommand();
            } else if (upper.equals("RECONNECTACCOUNT")) {
            	handleReconnectAccountCommand();
            } else {
                handleInvalidCommand(cmd);
            }
            mChannel.writePrompt();
            cmd = mChannel.getCommand();
        }
        mChannel.close();
    }

    private void handleInvalidCommand(String cmd) {
        mChannel.writeNack("Command invalid");
        Utils.logError("CommandServer: invalid command received: " + cmd);
    }

    private void handleEnableAPICommand(String cmd) {
        if (Settings.settings().isGateway()) {
            mChannel.writeNack("ENABLEAPI is not valid for the IB Gateway");
            return;
        }

        String[] words = cmd.split("\\s+");
        boolean enableRemoteConnections = false;
        boolean disableReadonlyApi = false;

        for (int i = 1; i < words.length; i++) {
            if (words[i].equalsIgnoreCase("enable-remote-connections")) {
                enableRemoteConnections = true;
            } else if (words[i].equalsIgnoreCase("disable-readonly-api")) {
                disableReadonlyApi = true;
            }
        }

        // run on the current thread
        (new ConfigurationTask(new EnableApiTask(mChannel, enableRemoteConnections, disableReadonlyApi))).execute();
   }

    private void handleEnterCodeCommand(String cmd) {
        String[] words = cmd.split("\\s+");
        if (words.length != 2) {
            mChannel.writeNack("ENTERCODE requires one argument");
            return;
        }

        String securityCode = words[1];

        Window dialog = SecurityCodeDialogManager.getInstance().getDialog();
        if (dialog == null) {
            mChannel.writeNack("security code dialog was not presented");
            return;
        }

        if (! dialog.isShowing()) {
            mChannel.writeNack("security code dialog is not visible");
            return;
        }

        final JTextField code = SwingUtils.findTextField(dialog, 0);
        if (code == null) {
            mChannel.writeNack("cannot locate text input");
            return;
        }

        if (! SwingUtils.setTextField(dialog, 0, securityCode)) {
            mChannel.writeNack("could not set field value");
            return;
        }

        mChannel.writeAck("OK");
        GuiDeferredExecutor.instance().execute(() -> {
            SwingUtils.clickButton(dialog, "OK");
        });
    }

    private void handleReconnectDataCommand() {
        JFrame jf = MainWindowManager.mainWindowManager().getMainWindow(1, TimeUnit.MILLISECONDS);

        int modifiers = KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK;
        KeyEvent pressed=new KeyEvent(jf,  KeyEvent.KEY_PRESSED, System.currentTimeMillis(), modifiers, KeyEvent.VK_F, KeyEvent.CHAR_UNDEFINED);
        KeyEvent typed=new KeyEvent(jf, KeyEvent.KEY_TYPED, System.currentTimeMillis(), modifiers, KeyEvent.VK_UNDEFINED, 'F' );
        KeyEvent released=new KeyEvent(jf, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), modifiers, KeyEvent.VK_F,  KeyEvent.CHAR_UNDEFINED );
        jf.dispatchEvent(pressed);
        jf.dispatchEvent(typed);
        jf.dispatchEvent(released);
      
        mChannel.writeAck("");
   }

    private void handleReconnectAccountCommand() {
        JFrame jf = MainWindowManager.mainWindowManager().getMainWindow();

        int modifiers = KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK;
        KeyEvent pressed=new KeyEvent(jf,  KeyEvent.KEY_PRESSED, System.currentTimeMillis(), modifiers, KeyEvent.VK_R, KeyEvent.CHAR_UNDEFINED);
        KeyEvent typed=new KeyEvent(jf, KeyEvent.KEY_TYPED, System.currentTimeMillis(), modifiers, KeyEvent.VK_UNDEFINED, 'R' );
        KeyEvent released=new KeyEvent(jf, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), modifiers, KeyEvent.VK_R,  KeyEvent.CHAR_UNDEFINED );
        jf.dispatchEvent(pressed);
        jf.dispatchEvent(typed);
        jf.dispatchEvent(released);

        mChannel.writeAck("");
    }

    private void handleStopCommand() {
        (new StopTask(mChannel)).run();     // run on the current thread
    }
    
}
