package org.xxpay.common.util;

public class PayChannelUtils {
    private PayChannelUtils() {
    }

    private static final String CHANNEL_CODE_SPLIT = "_";

    /**
     * 生成通道Code
     *
     * @param channelName
     * @param payType
     * @return
     */
    public static String buildChannelCode(String channelName, String payType) {
        String result = channelName + CHANNEL_CODE_SPLIT + payType;
        result = result.toUpperCase();
        return result;
    }
}
