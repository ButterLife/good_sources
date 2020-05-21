package cn.zhuchuangsoft.footstone.utils;

public class StringUtil {

    private static final String REGEX = "^5A\\w+AA\\w*";

    public static int byteArrayToInt(byte[] b) {
        byte[] a = new byte[4];
        int i = a.length - 1, j = b.length - 1;
        for (; i >= 0; i--, j--) {//从b的尾部(即int值的低位)开始copy数据
            if (j >= 0)
                a[i] = b[j];
            else
                a[i] = 0;//如果b.length不足4,则将高位补0
        }
        //&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v0 = (a[0] & 0xff) << 24;
        int v1 = (a[1] & 0xff) << 16;
        int v2 = (a[2] & 0xff) << 8;
        int v3 = (a[3] & 0xff);
        return v0 + v1 + v2 + v3;
    }

    public static int byteFour(byte b1, byte b2, byte b3, byte b4) {
        int i0 = (b1 & 0xff) << 24;
        int i1 = (b2 & 0xff) << 16;
        int i2 = (b3 & 0xff) << 8;
        int i3 = (b4 & 0xff);
        return i0 + i1 + i2 + i3;
    }

    public static short byteTwo(byte b1, byte b2) {
        /*short s = 0;
        s = (short)(s ^ b1);  //将b1赋给s的低8位
        s = (short)(s << 8);  //s的低8位移动到高8位
        s = (short)(s ^ b2);*/
        return (short) ((0xff & b2) | (0xff00 & (b1 << 8)));
    }

    /**
     * 判断数据是否有效
     *
     * @param bytes
     * @return
     */
    public static String judge(byte[] bytes) {
        String hex1 = getHex(bytes, false);
        String[] str = hex1.split(":");
        String hex = hex1.replaceAll(":", "");
        //判断是否符合格式
        if (!hex.matches(REGEX)) {
            return null;
        }//判断长度是否符合
        if (!str[1].equals(Integer.toHexString(str.length))) {
            return null;
        }//校验和值是否为0
        if (!check(bytes)) {
            return null;
        }//判断是哪种业务
        return str[3];
    }

    /**
     * 校验和值是否为0
     *
     * @param bytes
     * @return
     */
    public static boolean check(byte[] bytes) {
        byte b = 0;
        for (byte bs : bytes) {
            b += bs;
        }
        char c = (char) b;
        return c == 0;
    }

    /**
     * 根据byte数组返回hex
     *
     * @param bytes
     * @param b     是否删除分隔符:
     * @return
     */
    public static String getHex(byte[] bytes, boolean b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex).append(":");
            if (hex.toUpperCase().equals("AA")) {
                break;
            }
        }
        if (b) {
            return sb.toString().replaceAll(":", "").toUpperCase().trim();
        }
        return sb.toString().toUpperCase().trim();
    }

    /**
     * 截取url最后一段字符
     *
     * @param url
     * @return
     */
    public static String getLastUrl(String url) {
        return url.substring(url.lastIndexOf("/", url.lastIndexOf("/") - 1));
    }

    /**
     * 将voltageA 转化为a_voltage
     *
     * @param name
     * @return
     */
    public static String getRexString(String name) {
        if (Character.isUpperCase(name.charAt(name.length() - 1))) {
            return (name.charAt(name.length() - 1) + "_" + name.substring(0, name.length() - 1)).toLowerCase();
        }
        return null;
    }
}
