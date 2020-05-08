package org.openhab.binding.veluxklf200.internal.commands.request;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.veluxklf200.internal.commands.GatewayCommands;
import org.openhab.binding.veluxklf200.internal.commands.VeluxKlf200Session;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_ACTIVATE_SCENE_CFM;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;
import org.openhab.binding.veluxklf200.internal.commands.status.CommandOriginator;
import org.openhab.binding.veluxklf200.internal.commands.status.Velocity;

@NonNullByDefault
public class GW_ACTIVATE_SCENE_REQ extends BaseRequest<GW_ACTIVATE_SCENE_CFM> {
    private int sessionId;
    private CommandOriginator commandOriginator = CommandOriginator.USER;
    private byte priorityLevel = 3; // User Level 2 (default)
    private int sceneId;
    private Velocity velocity;

    public GW_ACTIVATE_SCENE_REQ(int sceneId, Velocity velocity) {
        super(GatewayCommands.GW_ACTIVATE_SCENE_REQ);
        this.sessionId = VeluxKlf200Session.getInstance().getSessionId();
        this.sceneId = sceneId;
        this.velocity = velocity;
    }

    public int getSessionId() {
        return this.sessionId;
    }

    @Override
    protected void writeData(KLFCommandFrame commandFrame) {
        commandFrame.writeShort((short) this.sessionId);
        commandFrame.writeByte(commandOriginator.getCode());
        commandFrame.writeByte(priorityLevel);
        commandFrame.writeByte((byte) this.sceneId);
        commandFrame.writeByte(this.velocity.getCode());
    }

    @Override
    public boolean handleResponseImpl(GW_ACTIVATE_SCENE_CFM response) {
        // logger.debug("Get Node Information status: {}, sceneId: {}", response.getStatus(), response.getNodeId());
        if (response.getSessionId() == this.sessionId) {
            return true;
        }
        return false;
    }
}
