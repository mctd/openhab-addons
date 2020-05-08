package org.openhab.binding.veluxklf200.internal.commands.response;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.veluxklf200.internal.commands.status.PasswordEnterCommandStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public class GW_PASSWORD_ENTER_CFM extends BaseConfirmationResponse {
    private static final Logger logger = LoggerFactory.getLogger(GW_PASSWORD_ENTER_CFM.class);

    private PasswordEnterCommandStatus status;

    public GW_PASSWORD_ENTER_CFM(KLFCommandFrame commandFrame, ThingUID bridgeUID) {
        super(commandFrame, bridgeUID);
        this.status = PasswordEnterCommandStatus.fromCode(this.getCommandFrame().readByte(1));

        if (this.status == PasswordEnterCommandStatus.SUCCESS) {
            logger.trace("GW_PASSWORD_ENTER_CFM: login successful");
        } else {
            logger.trace("GW_PASSWORD_ENTER_CFM: login failed");
        }
    }

    public PasswordEnterCommandStatus getStatus() {
        return this.status;
    }

}
