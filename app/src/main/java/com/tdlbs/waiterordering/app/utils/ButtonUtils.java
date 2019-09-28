package com.tdlbs.waiterordering.app.utils;

/**
 * ButtonUtils
 * 判断按钮是否被多次点击
 *
 * @author markgu
 */

public class ButtonUtils {
    public final static long MIDDLE_DIFF = 3 * 1000;
    public final static long LARGE_DIFF = 5 * 1000;
    private static long lastClickTime = 0;
    private static long DIFF = 500;
    private static int lastButtonId = -1;

    /**
     *    * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *    *
     *    * @return
     *    
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(-1, DIFF);
    }

    /**
     *    * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *    *
     *    * @return
     *    
     */
    public static boolean isFastDoubleClick(int buttonId) {
        return isFastDoubleClick(buttonId, DIFF);
    }

    /**
     *    * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
     *    *
     *    * @param diff
     *    * @return
     *    
     */
    public static boolean isFastDoubleClick(int buttonId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {
            return true;
        }
        lastClickTime = time;
        lastButtonId = buttonId;
        return false;
    }

}