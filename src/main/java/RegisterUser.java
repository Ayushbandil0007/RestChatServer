import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;
import org.webbitserver.handler.EmbeddedResourceHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class RegisterUser implements HttpHandler {
    private final String root = "views";
    private final String templatePath = "/register/register.html";

    @Override
    public void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control) throws Exception {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("uri", Rest.params(request));

        HttpHandler delegate = new EmbeddedJmustacheHandler(root, context);
        request.uri(templatePath);
        delegate.handleHttpRequest(request, response, control);
    }

    private static class EmbeddedJmustacheHandler extends EmbeddedResourceHandler {
        private final Mustache.Compiler mf = Mustache.compiler();
        private final Object context;

        public EmbeddedJmustacheHandler(String root, Object context) {
            super(root);
            this.context = context;
        }

        @Override
        protected IOWorker createIOWorker(HttpRequest request, HttpResponse response, HttpControl control) {
            return new LoginUserWorker(request, response, control, context);
        }

        protected class LoginUserWorker extends ResourceWorker {
            protected LoginUserWorker(HttpRequest request, HttpResponse response, HttpControl control, Object context) {
                super(request, response, control);
            }

            @Override
            protected ByteBuffer read(int length, InputStream in) throws IOException {
                Template t = mf.compile(new InputStreamReader(in, "UTF-8"));
                String result = t.execute(context);
                return ByteBuffer.wrap(result.getBytes("UTF-8"));
            }
        }
    }
}
