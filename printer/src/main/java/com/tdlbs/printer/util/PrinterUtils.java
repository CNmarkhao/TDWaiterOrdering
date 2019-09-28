package com.tdlbs.printer.util;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Base64;
import android.widget.Toast;

import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.LabelCommand;
import com.gprinter.io.GpDevice;
import com.gprinter.io.PortParameters;
import com.gprinter.save.PortParamDataBase;
import com.gprinter.service.GpPrintService;

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author markgu
 * @date 2018/3/9
 */

public class PrinterUtils {

    private Handler deliver = new Handler(Looper.getMainLooper());
    private Context context;
    private GpService mGpService;
    private static HashMap<Integer, EscCommand.JUSTIFICATION> jMap = new HashMap<Integer, EscCommand.JUSTIFICATION>();
    private boolean isSM;

    static {
        jMap.put(FormatPrintContent.Justification.CENTER, EscCommand.JUSTIFICATION.CENTER);
        jMap.put(FormatPrintContent.Justification.LEFT, EscCommand.JUSTIFICATION.LEFT);
        jMap.put(FormatPrintContent.Justification.RIGHT, EscCommand.JUSTIFICATION.RIGHT);
    }

    private EscCommand.ENABLE getEnable(boolean isOn) {
        return isOn ? EscCommand.ENABLE.ON : EscCommand.ENABLE.OFF;
    }

    public PrinterUtils(Context context, GpService gpService) {
        this.context = context;
        mGpService = gpService;
    }


    /**
     * 生成博佳的Base64字符串
     */
    private String createContext(ArrayList<FormatPrintContent> content) {
        EscCommand esc = new EscCommand();

        esc.addText("\n");
        esc.addText("\n");
        boolean flag = false;
        for (FormatPrintContent cur : content) {
            if (cur.getBeforeFeetLins() > 0) {
                esc.addPrintAndFeedLines((byte) cur.getBeforeFeetLins());
            }
            if (cur.getAbsolutePrintPosition() > 0) {
                esc.addSetAbsolutePrintPosition((byte) cur.getAbsolutePrintPosition());
            }
            EscCommand.JUSTIFICATION justification = jMap.get(cur.getJustification());
            if (justification != null) {
                esc.addSelectJustification(justification);// 设置打印居中
            }
            esc.addSelectPrintModes(EscCommand.FONT.FONTA, getEnable(cur.isEmphasized()),
                    getEnable(cur.isDoubleheight()),
                    getEnable(cur.isDoublewidth()),
                    getEnable(cur.isUnderLine()));// 倍高倍宽
            esc.addSetKanjiFontMode(getEnable(cur.isDoublewidth()),
                    getEnable(cur.isDoubleheight()),
                    getEnable(cur.isUnderLine())); //汉字倍高倍宽
            esc.addText(cur.getContent()); // 打印文字
//			esc.addSetCharcterSize(WIDTH_ZOOM.MUL_1, HEIGHT_ZOOM.MUL_6);
            if (cur.isNewLine()) {
                esc.addText("\n");
            }
            if (cur.isOpenCashbox()) {
                esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
            }
            if (cur.getAfterFeetLins() > 0) {
                esc.addPrintAndFeedLines((byte) cur.getAfterFeetLins());
            }
            flag = cur.isCutPaper();
        }
        if (flag) {
            esc.addCutPaper();
        }
        Vector<Byte> datas = esc.getCommand(); // 发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String result = Base64.encodeToString(bytes, Base64.DEFAULT);
        return result;
    }

    private String gprint(int index, ArrayList<FormatPrintContent> content) {
        int rs;
        try {
            rs = mGpService.sendEscCommand(index, createContext(content));
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];

            if (r != GpCom.ERROR_CODE.SUCCESS) {
                return GpCom.getErrorText(r);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean[] getConnectState() {
        boolean[] state = new boolean[GpPrintService.MAX_PRINTER_CNT];
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            state[i] = false;
        }
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            try {
                if (mGpService.getPrinterConnectStatus(i) == GpDevice.STATE_CONNECTED) {
                    state[i] = true;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return state;
    }

    public FormatPrintContent create(String content, boolean ishd,
                                     boolean iswd, boolean isNewLine, int justfication, int afterLins,
                                     int position,boolean isCut) {
        FormatPrintContent content1 = new FormatPrintContent();
        content1.setContent(content);
        content1.setFontSize(32);
        content1.setDoubleheight(ishd);
        content1.setDoublewidth(iswd);
        content1.setNewLine(isNewLine);
        content1.setJustification(justfication);
        content1.setAfterFeetLins(afterLins);
        content1.setAbsolutePrintPosition(position);
        content1.setCutPaper(isCut);
        return content1;
    }


    public void print(final int index, String address, int port, final ArrayList<FormatPrintContent> contentList) {

        final Handler mHandler = new Handler(Looper.getMainLooper());

        try {
            if (!getConnectState()[index]) {
                PortParamDataBase database = new PortParamDataBase(context);
                PortParameters parameters = new PortParameters();
                parameters.setIpAddr(address);
                parameters.setPortNumber(port);
                parameters.setPortType(PortParameters.ETHERNET);
                database.insertPortParam(index, parameters);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
                int rel1 = mGpService.openPort(index, parameters.getPortType(),
                        parameters.getIpAddr(), parameters.getPortNumber());
                deliver.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String response = gprint(index, contentList);
                            if (response != null) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "打印失败,失败码：" + response, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);
            } else {

                final String response = gprint(index, contentList);
                if (response != null) {
                    PortParamDataBase database = new PortParamDataBase(context);
                    PortParameters parameters = new PortParameters();
                    parameters.setIpAddr(address);
                    parameters.setPortNumber(port);
                    parameters.setPortType(PortParameters.ETHERNET);
                    database.insertPortParam(index, parameters);

                    int rel1 = mGpService.openPort(index, parameters.getPortType(),
                            parameters.getIpAddr(), parameters.getPortNumber());
                    deliver.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final String response = gprint(index, contentList);
                                if (response != null) {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context, "打印失败,失败码：" + response, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 2000);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "打印失败,尝试重打印", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取USB打印机的名字
     *
     * @return 返回的打印设备的名字， noDevices：没有获取到任何打印设备
     */
    private String getUsbDevices() {
        String usbname = "";
        UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> devices = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = devices.values().iterator();
        int count = devices.size();
        if (count > 0) {
            while (deviceIterator.hasNext()) {
                UsbDevice device = deviceIterator.next();
                String devicename = device.getDeviceName();
                if (checkUsbDevicePidVid(device)) {
                    usbname = devicename;
                }
            }
        } else {
            usbname = "noDevices";
        }
        return usbname;
    }

    /**
     * 判断是否是USB打印机
     *
     * @param dev
     * @return
     */
    private boolean checkUsbDevicePidVid(UsbDevice dev) {
        int pid = dev.getProductId();
        int vid = dev.getVendorId();
        boolean rel = false;
        if ((vid == 34918 && pid == 256) || (vid == 1137 && pid == 85) || (vid == 6790 && pid == 30084) || (vid == 26728 && pid == 256) || (vid == 26728 && pid == 512) || (vid == 26728 && pid == 256) || (vid == 26728 && pid == 768) || (vid == 26728 && pid == 1024) || (vid == 26728 && pid == 1280) || (vid == 26728 && pid == 1536)) {
            rel = true;
        }
        return rel;
    }


}
