package org.openhab.binding.veluxklf200.internal.commands.request;

import java.lang.reflect.ParameterizedType;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.veluxklf200.internal.commands.GatewayCommands;
import org.openhab.binding.veluxklf200.internal.commands.response.BaseConfirmationResponse;
import org.openhab.binding.veluxklf200.internal.commands.response.BaseResponse;
import org.openhab.binding.veluxklf200.internal.commands.response.GW_ERROR_NTF;
import org.openhab.binding.veluxklf200.internal.commands.response.KLFCommandFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public abstract class BaseRequest<T extends BaseConfirmationResponse> {
    private final Logger logger = LoggerFactory.getLogger(BaseRequest.class);
    private KLFCommandFrame commandFrame;
    private @Nullable T response = null;

    public BaseRequest(GatewayCommands command) {
        this.commandFrame = new KLFCommandFrame(command);
    }

    protected abstract void writeData(KLFCommandFrame commandFrame);

    public byte[] getSlipFrame() {
        this.writeData(this.commandFrame);
        byte[] slipFrame = commandFrame.buildSlipFrame();

        return slipFrame;
    }

    public void handleResponse2(BaseResponse response) {
    }

    public void handleResponse(BaseConfirmationResponse response) {
        boolean handled = false;

        // TODO : do something better here...
        @Nullable
        T castedResponse;
        Class<T> expectedType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        if (expectedType.isInstance(response)) {
            castedResponse = (T) response;
        } else {
            castedResponse = null;
        }

        // GW_ERROR_NTF can be handled by any request
        if (response instanceof GW_ERROR_NTF) {
            handled = true;
        } else if (castedResponse != null) {
            handled = this.handleResponseImpl(castedResponse);
        } else {
            logger.error("Response type ({}) does not match the one expected by the request ({}) !", response,
                    expectedType);
        }

        if (handled) {
            this.response = castedResponse;
            logger.trace("Command {} acknowledged by {}.", this, response);
        } else {
            logger.error("Command {} NOT acknowledged by {} (CFM not matching REQ?).", this, response);
        }
    }

    public abstract boolean handleResponseImpl(T response);

    public @Nullable T getResponse() {
        return this.response;
    }
}