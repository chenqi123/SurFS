/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.surfs.nas.protocol;

import com.surfs.nas.server.Volume;
import com.surfs.nas.transport.ErrorResponse;
import com.surfs.nas.transport.OriginRequest;
import com.surfs.nas.transport.OriginResponse;
import com.surfs.nas.transport.TcpCommandType;
import java.io.IOException;
import java.util.logging.Logger;

public class GetSpaceRequest extends OriginRequest {

    private static final Logger log = Logger.getLogger(GetSpaceRequest.class.getName());

    public GetSpaceRequest() {
        super(TcpCommandType.SYS_VOLUME_SPACE_GET);
    }

    public GetSpaceRequest(byte commandType, int sequence) {
        super(commandType, sequence);
    }

    @Override
    public void run() {
        String volid = this.getString();
        try {

            Volume vol = getServerSourceMgr().getSelector().getVolume(volid);
            vol.getChecker().check();
            OriginResponse tr = new OriginResponse(this);
            tr.setString(vol.getChecker().toString());
            this.getSession().sendMessage(tr);
        } catch (Throwable e) {
            this.getSession().sendMessage(new ErrorResponse(this, e instanceof IOException ? (IOException) e : new IOException(e)));
        }
    }

}
