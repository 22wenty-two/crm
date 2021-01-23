package com.xxxx.crm.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author NeXT
 */
public class LoginUserUtil {

    /**
     * 从cookie中获取userId
     * @param request
     * @return
     */
    public static Integer releaseUserIdFromCookie(HttpServletRequest request) {
        String userIdString = CookieUtil.getCookieValue(request, "userIdStr");
        if (StringUtils.isBlank(userIdString)) {
            return 0;
        }
        Integer userId = UserIDBase64.decoderUserID(userIdString);
        return userId;
    }
}
