import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;
import org.webbitserver.WebServer;
import utils.FuriTemplateEngine;
import utils.HttpVerbHandler;
import utils.UriTemplateEngine;
import utils.UriTemplateHandler;
import java.util.Map;

public class Rest {
    private final WebServer webServer;
    private final UriTemplateEngine uriTemplateEngine;

    public Rest(WebServer webServer) {
        this(webServer, new FuriTemplateEngine());
    }

    public Rest(WebServer webServer, UriTemplateEngine uriTemplateEngine) {
        this.webServer = webServer;
        this.uriTemplateEngine = uriTemplateEngine;
    }

    public Rest GET(String uriTemplate, HttpHandler httpHandler) {
        return verbHandler("GET", uriTemplate, httpHandler);
    }

    public Rest PUT(String uriTemplate, HttpHandler httpHandler) {
        return verbHandler("PUT", uriTemplate, httpHandler);
    }

    public Rest POST(String uriTemplate, HttpHandler httpHandler) {
        return verbHandler("POST", uriTemplate, httpHandler);
    }

    private Rest verbHandler(String verb, String uriTemplate, HttpHandler httpHandler) {
        webServer.add(new UriTemplateHandler(uriTemplate, new HttpVerbHandler(verb, httpHandler), uriTemplateEngine));
        return this;
    }

    public static Object param(HttpRequest request, String name) {
        return params(request).get(name);
    }

    public static Map<String, Object> params(HttpRequest request) {
        return (Map<String, Object>) request.data(UriTemplateHandler.URI_MATCH);
    }

    private static void redirect(HttpResponse response, String uri) {
        response.header("Location", uri).status(302).end();
    }
}