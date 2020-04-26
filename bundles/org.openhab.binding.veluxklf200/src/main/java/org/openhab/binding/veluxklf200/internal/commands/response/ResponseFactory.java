package org.openhab.binding.veluxklf200.internal.commands.response;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.openhab.binding.veluxklf200.internal.engine.KLFCommandProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ResponseFactory {
    private static final Logger logger = LoggerFactory.getLogger(ResponseFactory.class);

    public static BaseResponse createFromCommandFrame(KLFCommandProcessor processor, KLFCommandFrame commandFrame) {

        // TODO : implement a Class Annotation (ex: //@KLFCommand(name = "GW_GET_NODE_INFORMATION_NTF", value =
        // KLFGatewayCommands.GW_GET_NODE_INFORMATION_NTF)) and scan for classes with matching Annotation ?

        Object retobj = null;

        Class<?> responseClass = null;
        try {
            responseClass = Class
                    .forName(BaseResponse.class.getPackage().getName() + "." + commandFrame.getCommand().name());
        } catch (ClassNotFoundException e) {
            logger.error("Unable to find a class for {}", commandFrame.getCommand().name());
            e.printStackTrace();
        }

        if (responseClass != null) {
            Class<?> ctParameters[] = new Class[2];
            ctParameters[0] = KLFCommandProcessor.class;
            ctParameters[1] = KLFCommandFrame.class;
            Constructor<?> ct = null;
            try {
                ct = responseClass.getConstructor(ctParameters);
            } catch (NoSuchMethodException | SecurityException e) {
                logger.error("Unable to find a valid constructor for class {}", responseClass);
                e.printStackTrace();
            }
            if (ct != null) {
                Object arglist[] = new Object[2];
                arglist[0] = processor;
                arglist[1] = commandFrame;
                try {
                    // Instantiate the response object
                    retobj = ct.newInstance(arglist);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e) {
                    logger.error("Error invoking constructor for class {}.", responseClass);
                    e.printStackTrace();
                }
            }
        }

        return (BaseResponse) retobj;
        /*
         * switch (commandFrame.getCommand()) {
         * case GW_PASSWORD_ENTER_CFM:
         * return new GW_PASSWORD_ENTER_CFM(commandFrame);
         * case GW_GET_PROTOCOL_VERSION_CFM:
         * return new GW_GET_PROTOCOL_VERSION_CFM(commandFrame);
         * case GW_GET_VERSION_CFM:
         * return new GW_GET_VERSION_CFM(commandFrame);
         * case GW_GET_STATE_CFM:
         * return new GW_GET_STATE_CFM(commandFrame);
         * case GW_SET_UTC_CFM:
         * return new GW_SET_UTC_CFM(commandFrame);
         * case GW_HOUSE_STATUS_MONITOR_ENABLE_CFM:
         * return new GW_HOUSE_STATUS_MONITOR_ENABLE_CFM(commandFrame);
         * case GW_GET_NODE_INFORMATION_CFM:
         * return new GW_GET_NODE_INFORMATION_CFM(commandFrame);
         *
         * default:
         * logger.error("Response factory not implemented: {}", commandFrame.getCommand().name());
         * return null;
         * }
         */
    }
}
