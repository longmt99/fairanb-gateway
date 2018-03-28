package com.fairanb.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class TrackingFilter extends ZuulFilter{
    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);

    private static final int      FILTER_ORDER =  1;
    private static final boolean  SHOULD_FILTER=true;

    @Autowired
    FilterUtils filterUtils;

    @Override
    public String filterType() {
        return FilterUtils.PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    private boolean isCorrelationIdPresent(){
      if (filterUtils.getCorrelationId() !=null){
          return true;
      }

      return false;
    }

    private String generateCorrelationId(){
        return java.util.UUID.randomUUID().toString();
    }

    public Object run() {

        if (isCorrelationIdPresent()) {
            logger.debug("fairanb-correlation-id found in tracking filter: {}. ", filterUtils.getCorrelationId());
        }
        else{
            filterUtils.setCorrelationId(generateCorrelationId());
            logger.debug("fairanb-correlation-id generated in tracking filter: {}.", filterUtils.getCorrelationId());
        }

        RequestContext ctx = RequestContext.getCurrentContext();
        logger.debug(String.format("Processing incoming request for {}.",  ctx.getRequest().getRequestURI()));
        return null;
    }
}