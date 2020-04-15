/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.commands;

import org.apache.commons.lang.NotImplementedException;
import org.openhab.binding.veluxklf200.internal.commands.structure.KLFCommandStructure;
import org.openhab.binding.veluxklf200.internal.commands.structure.KLFGatewayCommands;

/**
 * Special command to indicates the command queue processing should terminate.
 *
 * @author emmanuel
 */
public class KlfCmdTerminate extends BaseKLFCommand {

    @Override
    public KLFCommandStructure getKLFCommandStructure() {
        return KLFCommandStructure.TERMINATE;
    }

    @Override
    protected boolean handleResponseImpl(KLFGatewayCommands responseCommand, byte[] data) {
        throw new NotImplementedException();
    }

    @Override
    protected byte[] pack() {
        throw new NotImplementedException();
    }
}