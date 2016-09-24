package com.stronans.domotics.interceptors;

/**
 * Created by S.King on 24/09/2016.
 */

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ExecuteTimeInterceptor implements HandlerInterceptor {

    private static final Logger logger = Logger.getLogger(ExecuteTimeInterceptor.class);

    //before the actual handler will be executed
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (logger.isDebugEnabled()) {
            logger.debug("Request: " + request.getRequestURL());

            long startTime = System.currentTimeMillis();
            request.setAttribute("startTime", startTime);
        }

        return true;
    }

    //after the handler is executed
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        //log it
        if (logger.isDebugEnabled()) {
            long startTime = (Long) request.getAttribute("startTime");
            long endTime = System.currentTimeMillis();
            long executeTime = endTime - startTime;

            logger.debug("ExecuteTime: " + executeTime + "ms  [" + handler + "]");
        }
    }
}