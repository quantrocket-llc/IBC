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

import java.awt.Window;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

final class LoginFrameHandler extends AbstractLoginHandler {

    @Override
    public boolean recogniseWindow(Window window) {
        if (! (window instanceof JFrame)) return false;

        // we check for the presence of the Login button because
        // TWS displays a different (information-only) dialog, also
        // entitled Login, when it's trying to reconnect
        return ((SwingUtils.titleEquals(window, "New Login") ||
                SwingUtils.titleEquals(window, "Login")) &&
                (SwingUtils.findButton(window, "Login") != null ||
                SwingUtils.findButton(window, "Log In") != null ||          // TWS 974+
                SwingUtils.findButton(window, "Paper Log In") != null));    // TWS 974+
    }

    @Override
    protected final boolean initialise(final Window window, int eventID) throws IbcException {
        setTradingMode(window);

        JtsIniManager.reload();     // because TWS/Gateway modify the jts.ini file before this point

        final JTextField userName = SwingUtils.findTextField(window, 0);
        if (userName == null) throw new IbcException("Username field");

        // Add a DocumentListener to the username field, which will set the
        // "Use/store settings on server" checkbox as required. This is
        // necessary because when a valid username is entered, TWS sets the
        // checkbox according to the latest saved settings for that user, which
        // may not be what is now required by the StoreSettingsOnServer setting.
        userName.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent de) {
                setStoreSettingsOnServerCheckbox();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                setStoreSettingsOnServerCheckbox();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                setStoreSettingsOnServerCheckbox();
            }

            private void setStoreSettingsOnServerCheckbox() {
                Boolean store = Settings.settings().storeSettingsOnServer();
                if(store != null) {
                    final String STORE_SETTINGS_ON_SERVER_CHECKBOX = "Use/store settings on server";

                    // we defer setting the checkbox: if we do it inline, TWS's setting
                    // overwrites it
                    GuiDeferredExecutor.instance().execute(() -> {
                        if (! SwingUtils.setCheckBoxSelected(
                                window,
                                STORE_SETTINGS_ON_SERVER_CHECKBOX,
                                store.booleanValue())) {
                            Utils.exitWithError(ErrorCodes.ERROR_CODE_CANT_FIND_CONTROL, "could not login: could not find control: " + STORE_SETTINGS_ON_SERVER_CHECKBOX);
                        }
                        Utils.logToConsole("Use/store settings on server selected: " + store.booleanValue());
                    });
                }
            }

        });
        return true;
    }

    @Override
    protected final boolean preLogin(final Window window, int eventID) throws IbcException {
        if (Settings.settings().ibLoginId().length() == 0) {
            setMissingCredential(window, 0);
        } else if (Settings.settings().ibPassword().length() == 0) {
            setMissingCredential(window, 1);
        } else {
            return true;
        }
        return false;
    }

    @Override
    protected final boolean setFields(Window window, int eventID) throws IbcException {
        Utils.logToConsole("Setting user name");
        setCredential(window, "IBAPI user name", 0, Settings.settings().ibLoginId());
        Utils.logToConsole("Setting password");
        setCredential(window, "IBAPI password", 1, Settings.settings().ibPassword());
        return true;
    }

}

