package com.shuai.android.common_lib.library_common.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * XSS防注入攻击的工具类。
 */
public class XSSFilterUtil {

    // XSS处理Map
    private static Map<String, String> xssMap = new LinkedHashMap<String, String>();

    static {
        // 含有脚本： script
        xssMap.put("[s|S][c|C][r|R][i|C][p|P][t|T]", "");
        // 含有脚本 javascript
        xssMap.put("[\\\"\\\'][\\s]*[j|J][a|A][v|V][a|A][s|S][c|C][r|R][i|I][p|P][t|T]:(.*)[\\\"\\\']", "\"\"");
        // 含有函数： eval
        xssMap.put("[e|E][v|V][a|A][l|L]\\((.*)\\)", "");
        // 含有符号 <
        xssMap.put("<", "&lt;");
        // 含有符号 >
        xssMap.put(">", "&gt;");
        // 含有符号 (
        xssMap.put("\\(", "(");
        // 含有符号 )
        xssMap.put("\\)", ")");
        // 含有符号 '
        xssMap.put("'", "'");
    }

    /**
     * 清除恶意的XSS脚本
     *
     * @param value
     * @return
     */
    public static String cleanXSS(String value) {
        Set<String> keySet = xssMap.keySet();
        for (String key : keySet) {
            String v = xssMap.get(key);
            value = value.replaceAll(key, v);
        }
        return value;
    }

}
