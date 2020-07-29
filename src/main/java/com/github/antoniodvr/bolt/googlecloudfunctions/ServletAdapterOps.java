package com.github.antoniodvr.bolt.googlecloudfunctions;

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.slack.api.bolt.response.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServletAdapterOps {

    private ServletAdapterOps() { }

    public static String doReadRequestBodyAsString(HttpRequest req) throws IOException {
        return req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    }

    public static Map<String, List<String>> toHeaderMap(HttpRequest req) {
        return new HashMap<>(req.getHeaders());
    }

    public static void writeResponse(HttpResponse resp, Response slackResp) throws IOException {
        resp.setStatusCode(slackResp.getStatusCode());
        for (Map.Entry<String, List<String>> header : slackResp.getHeaders().entrySet()) {
            String name = header.getKey();
            for (String value : header.getValue()) {
                resp.appendHeader(name, value);
            }
        }
        resp.appendHeader("Content-Type", slackResp.getContentType());
        if (slackResp.getBody() != null) {
            resp.getWriter().write(slackResp.getBody());
        }
    }

}
