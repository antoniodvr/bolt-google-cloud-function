package com.github.antoniodvr.bolt.googlecloudfunctions;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.slack.api.bolt.App;
import com.slack.api.bolt.request.Request;
import com.slack.api.bolt.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlackAppHttpFunction implements HttpFunction {

    private static final Logger log = LoggerFactory.getLogger(SlackAppHttpFunction.class);

    private final App app;
    private final SlackAppHttpFunctionAdapter adapter;

    public SlackAppHttpFunction(App app) {
        this.app = app;
        this.adapter = new SlackAppHttpFunctionAdapter(this.app.config());
    }

    public void service(HttpRequest request, HttpResponse response) throws Exception {
        Request slackReq = this.adapter.buildSlackRequest(request);
        if (slackReq != null) {
            try {
                Response slackResp = this.app.run(slackReq);
                this.adapter.writeResponse(response, slackResp);
            } catch (Exception e) {
                log.error("Failed to handle a request - {}", e.getMessage(), e);
                response.setStatusCode(500);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Something is wrong\"}");
            }
        }
    }
}
