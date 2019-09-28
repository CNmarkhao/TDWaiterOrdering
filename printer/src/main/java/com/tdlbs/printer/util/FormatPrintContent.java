package com.tdlbs.printer.util;

/**
 * 格式化输出内容
 * 
 * @author zhonghong.chenli
 * @date 2016-6-27 上午9:25:19
 */
public class FormatPrintContent {
	public int getFontSize() {
		return fontSize;
	}

	public boolean isUnderLine() {
		return underLine;
	}

	public FormatPrintContent setUnderLine(boolean underLine) {
		this.underLine = underLine;
		return this;
	}

	public FormatPrintContent setFontSize(int fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	private int fontSize;

	public FormatPrintContent setAlignment(int alignment) {
		this.alignment = alignment;
		return this;
	}

	public int getAlignment() {
		return alignment;
	}

	private int alignment;
	private  boolean underLine;
	/** 文本内容 */
	private String str;
	/** 是否换行 */
	private boolean isNL;
	/** 对齐方式 */
	private int jf;
	/** 绝对位置 */
	private int absP;

	/** 输入前喂行数 */
	private int bfl;
	/** 输入完喂行数 */
	private int afl;
	// 输出模式
	/** 字体(先不定) */

	/** 是否着重显示 emphasized */
	private boolean isE;
	/** 是否倍高 */
	private boolean isDH;
	/** 是否倍宽 */
	private boolean isDW;
	/** 是否下划线 underline */
	private boolean isUdl;
	/** 是否打开钱箱  */
	private boolean isOC;
	/** 是否切纸*/
	private boolean isCP;

	public boolean isBR() {
		return isBR;
	}

	public void setBR(boolean BR) {
		isBR = BR;
	}

	private boolean isBR;

	public boolean isQR() {
		return isQR;
	}

	public void setQR(boolean QR) {
		isQR = QR;
	}

	private boolean isQR;
	public String getContent() {
		return str;
	}

	public void setContent(String content) {
		this.str = content;
	}

	public boolean isNewLine() {
		return isNL;
	}

	public void setNewLine(boolean isNewLine) {
		this.isNL = isNewLine;
	}

	public int getJustification() {
		return jf;
	}

	public void setJustification(int justification) {
		this.jf = justification;
	}

	public boolean isEmphasized() {
		return isE;
	}

	public void setEmphasized(boolean isEmphasized) {
		this.isE = isEmphasized;
	}

	public boolean isDoubleheight() {
		return isDH;
	}

	public void setDoubleheight(boolean isDoubleheight) {
		this.isDH = isDoubleheight;
	}

	public boolean isDoublewidth() {
		return isDW;
	}

	public void setDoublewidth(boolean isDoublewidth) {
		this.isDW = isDoublewidth;
	}



	public int getBeforeFeetLins() {
		return bfl;
	}

	public void setBeforeFeetLins(int beforeFeetLins) {
		this.bfl = beforeFeetLins;
	}

	public int getAfterFeetLins() {
		return afl;
	}

	public void setAfterFeetLins(int afterFeetLins) {
		this.afl = afterFeetLins;
	}

	public int getAbsolutePrintPosition() {
		return absP;
	}

	public void setAbsolutePrintPosition(int absolutePrintPosition) {
		this.absP = absolutePrintPosition;
	}

	public boolean isOpenCashbox() {
		return isOC;
	}

	public void setOpenCashbox(boolean isOpenCashbox) {
		this.isOC = isOpenCashbox;
	}

	public boolean isCutPaper() {
		return isCP;
	}

	public void setCutPaper(boolean isCutPaper) {
		this.isCP = isCutPaper;
	}



	/**
	 * 对齐方式定义 默认为0
	 */
	public final static class Justification {
		/** default */
		public static final int DEFAULT = 0;
		/** 左对齐 */
		public static final int LEFT = 1;
		/** 右对齐 */
		public static final int RIGHT = 2;
		/** 居中 */
		public static final int CENTER = 3;
	}
}
