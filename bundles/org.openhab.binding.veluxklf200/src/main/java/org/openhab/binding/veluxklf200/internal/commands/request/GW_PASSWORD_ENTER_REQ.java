package org.openhab.binding.veluxklf200.internal.commands.request;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.veluxklf200.internal.commands.GatewayCommands;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_PASSWORD_ENTER_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;
import org.openhab.binding.veluxklf200.internal.commands.status.PasswordEnterCommandStatus;

/**
 * Enter password to authenticate request
 *
 * @author emmanuel
 *
 */
@NonNullByDefault
public class GW_PASSWORD_ENTER_REQ extends BaseRequest<GW_PASSWORD_ENTER_CFM> {
    private String password;
    private boolean isLoggedIn = false;

    public GW_PASSWORD_ENTER_REQ(String password) {
        super(GatewayCommands.GW_PASSWORD_ENTER_REQ);
        this.password = password;
    }

    @Override
    protected void writeData(KLFCommandFrame commandFrame) {
        commandFrame.writeUtf8String(this.password, 32);
    }

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    @Override
    public boolean handleResponseImpl(GW_PASSWORD_ENTER_CFM response) {
        if (response.getStatus() == PasswordEnterCommandStatus.SUCCESS) {
            this.isLoggedIn = true;
        } else {
            this.isLoggedIn = false;
        }
        return true;
    }
}
