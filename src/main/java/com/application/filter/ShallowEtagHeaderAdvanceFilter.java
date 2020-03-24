package com.application.filter;

import org.springframework.util.DigestUtils;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.io.IOException;
import java.io.InputStream;

/**
 * 增强Etag
 *
 * @author yanghaiyong
 * 2020年3月24日 13:18:46
 */
public class ShallowEtagHeaderAdvanceFilter extends ShallowEtagHeaderFilter {
    @Override
    protected String generateETagHeaderValue(InputStream inputStream, boolean isWeak) throws IOException {
        StringBuilder builder = new StringBuilder(37);
        if (isWeak) {
            builder.append("W/");
        }
        builder.append("0");
        DigestUtils.appendMd5DigestAsHex(inputStream, builder);
        return builder.toString();
    }
}
