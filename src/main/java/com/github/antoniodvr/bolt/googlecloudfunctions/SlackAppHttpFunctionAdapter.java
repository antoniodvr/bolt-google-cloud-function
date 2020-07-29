package com.github.antoniodvr.bolt.googlecloudfunctions;

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.request.Request;
import com.slack.api.bolt.request.RequestHeaders;
import com.slack.api.bolt.response.Response;
import com.slack.api.bolt.util.QueryStringParser;
import com.slack.api.bolt.util.SlackRequestParser;

import java.io.IOException;

public class SlackAppHttpFunctionAdapter {

    private SlackRequestParser requestParser;

    public SlackAppHttpFunctionAdapter(AppConfig appConfig) {
        this.requestParser = new SlackRequestParser(appConfig);
    }

    public Request<?> buildSlackRequest(HttpRequest req) throws IOException {
        String requestBody = this.doReadRequestBodyAsString(req);
        RequestHeaders headers = new RequestHeaders(ServletAdapterOps.toHeaderMap(req));
        SlackRequestParser.HttpRequest rawRequest = SlackRequestParser.HttpRequest.builder()
                .requestUri(req.getUri())
                .queryString(QueryStringParser.toMap(req.getQuery().orElse(null)))
                .headers(headers)
                .requestBody(requestBody)
                .build();
        return this.requestParser.parse(rawRequest);
    }

    protected String doReadRequestBodyAsString(HttpRequest req) throws IOException {
        return ServletAdapterOps.doReadRequestBodyAsString(req);
    }

    public void writeResponse(HttpResponse resp, Response slackResp) throws IOException {
        ServletAdapterOps.writeResponse(resp, slackResp);
    }

}
