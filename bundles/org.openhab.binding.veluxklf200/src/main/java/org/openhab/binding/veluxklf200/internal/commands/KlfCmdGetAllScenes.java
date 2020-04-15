/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.veluxklf200.internal.commands;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.veluxklf200.internal.commands.structure.KLFCommandStructure;
import org.openhab.binding.veluxklf200.internal.commands.structure.KLFGatewayCommands;
import org.openhab.binding.veluxklf200.internal.components.VeluxScene;
import org.openhab.binding.veluxklf200.internal.utility.KLFUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Get all scenes.
 *
 * @author emmanuel
 */
public class KlfCmdGetAllScenes extends BaseKLFCommand {

    private final Logger logger = LoggerFactory.getLogger(KlfCmdGetAllScenes.class);
    private List<VeluxScene> scenes;

    /**
     * Default constructor.
     */
    public KlfCmdGetAllScenes() {
        super();
        this.scenes = new ArrayList<VeluxScene>();
    }

    /**
     * Gets the list of discovered scenes.
     *
     * @return List of scenes
     */
    public List<VeluxScene> getScenes() {
        return this.scenes;
    }

    @Override
    protected boolean handleResponseImpl(KLFGatewayCommands responseCommand, byte[] data) {
        switch (responseCommand) {
            case GW_GET_SCENE_LIST_CFM:
                int scenesExpected = data[FIRSTBYTE];
                logger.debug("Command executing, expecting data for {} scenes.", scenesExpected);
                if (0 == scenesExpected) {
                    this.commandStatus = CommandStatus.COMPLETE;
                }
                return true;
            case GW_GET_SCENE_LIST_NTF:
                int scenesFound = data[FIRSTBYTE];
                logger.debug("Command recieved data for {} scenes.", scenesFound);
                int framePos = FIRSTBYTE + 1;
                for (int i = 0; i < scenesFound; ++i) {
                    logger.debug("Found scene Id:{} - {}", data[framePos],
                            KLFUtils.extractUTF8String(data, framePos + 1, framePos + 64));
                    this.scenes.add(new VeluxScene(data[framePos],
                            KLFUtils.extractUTF8String(data, framePos + 1, framePos + 64)));
                    framePos += 65;
                }
                this.commandStatus = CommandStatus.COMPLETE;
                return true;
            default:
                return false;
        }

    }

    @Override
    public KLFCommandStructure getKLFCommandStructure() {
        return KLFCommandStructure.GET_ALL_SCENES;
    }

    @Override
    protected byte[] pack() {
        return new byte[] {};
    }
}