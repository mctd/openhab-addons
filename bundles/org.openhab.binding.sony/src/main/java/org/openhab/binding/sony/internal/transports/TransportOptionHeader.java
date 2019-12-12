/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.sony.internal.transports;

import org.eclipse.jetty.http.HttpHeader;
import org.openhab.binding.sony.internal.net.Header;

/**
 * 
 * @author Tim Roberts - Initial contribution
 */
public class TransportOptionHeader implements TransportOption {
    private final Header header;

    public TransportOptionHeader(Header header) {
        this.header = header;
    }

    public TransportOptionHeader(HttpHeader hdr, String value) {
        this.header = new Header(hdr.asString(), value);
    }

    public TransportOptionHeader(String key, String value) {
        this.header = new Header(key, value);
    }

    public Header getHeader() {
        return header;
    }
}
