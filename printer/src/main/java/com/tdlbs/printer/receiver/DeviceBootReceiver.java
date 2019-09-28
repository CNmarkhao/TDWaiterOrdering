package com.tdlbs.printer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gprinter.service.GpPrintService;

/**
 * @author markgu
 */
public class DeviceBootReceiver extends BroadcastReceiver {
    /**
     * 开机广播
     **/
    static final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BOOT_COMPLETED)) {
            Intent i = new Intent(context, GpPrintService.class);
            context.startService(i);
            Log.i("DeviceBootReceiver", "GpPrintService.start");
        }
    }
}
