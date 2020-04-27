package org.openhab.binding.veluxklf200.internal.commands.response;

import org.openhab.binding.veluxklf200.internal.engine.KLFCommandProcessor;
import org.openhab.binding.veluxklf200.internal.status.PasswordEnterCommandStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GW_PASSWORD_ENTER_CFM extends BaseResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_PASSWORD_ENTER_CFM.class);

    private PasswordEnterCommandStatus status;

    public GW_PASSWORD_ENTER_CFM(KLFCommandProcessor processor, KLFCommandFrame commandFrame) {
        super(processor, commandFrame);
        this.status = PasswordEnterCommandStatus.fromCode(this.getCommandFrame().readByte(1));

        if (this.status == PasswordEnterCommandStatus.SUCCESS) {
            logger.info("Login successful");
            // TODO : update KLFCommandProcessor "isLoggedIn"
        } else {
            logger.error("Login failed (bad password?)");
        }
    }

    public PasswordEnterCommandStatus getStatus() {
        return this.status;
    }

}
