//////////////////////////////////////////////////////////////////////////////////
//                                                                              //
//     This Source Code Form is subject to the terms of the Mozilla Public      //
//     License, v. 2.0. If a copy of the MPL was not distributed with this      //
//     file, You can obtain one at http://mozilla.org/MPL/2.0/.                 //
//                                                                              //
//////////////////////////////////////////////////////////////////////////////////

package com.github.tiwindetea.oggplayer;

/**
 * Created by organic-code on 5/30/16.
 */
public class ObservableThread extends Thread {
    private TerminatedThreadHandler listener = null;

    public ObservableThread(Runnable target) {
        super(target);
    }

    public void setListener(TerminatedThreadHandler listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        super.run();
        if (this.listener != null) {
            this.listener.handle();
        }
    }
}
