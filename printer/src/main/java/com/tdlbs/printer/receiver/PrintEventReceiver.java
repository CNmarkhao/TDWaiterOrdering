package  com.tdlbs.printer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.gprinter.command.GpCom;

/**
 * @author markgu
 */
public class PrintEventReceiver extends BroadcastReceiver {
    private static final int MAIN_QUERY_PRINTER_STATUS = 0xfe;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(GpCom.ACTION_DEVICE_REAL_STATUS)) {
            int requestCode = intent.getIntExtra(GpCom.EXTRA_PRINTER_REQUEST_CODE, -1);
            if (requestCode == MAIN_QUERY_PRINTER_STATUS) {
                int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
                String str;
                if (status == GpCom.STATE_NO_ERR) {
                    str = "打印机正常";
                } else {
                    str = "打印机 ";
                    if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
                        str += "脱机";
                    }
                    if ((byte) (status & GpCom.STATE_PAPER_ERR) > 0) {
                        str += "缺纸";
                    }
                    if ((byte) (status & GpCom.STATE_COVER_OPEN) > 0) {
                        str += "打印机开盖";
                    }
                    if ((byte) (status & GpCom.STATE_ERR_OCCURS) > 0) {
                        str += "打印机出错";
                    }
                    if ((byte) (status & GpCom.STATE_TIMES_OUT) > 0) {
                        str += "查询超时";
                    }
                }
                Toast.makeText(context.getApplicationContext(), "打印机状态：" + str, Toast.LENGTH_SHORT).show();
            }
        }
    }
};
