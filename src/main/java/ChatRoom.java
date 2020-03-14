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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoom implements HttpHandler {
    private final String root = "views";
    private final String templatePath = "/chatroom/chatroom.html";
    private final String invalidTopic = "";
    private final List<String> topics = new ArrayList<String>();

    public ChatRoom(){
        topics.add("sports");
        topics.add("food");
        topics.add("movies");
    }

    @Override
    public void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control) throws Exception {
        String topic = request.uri().split("/")[2];

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("uri", Rest.params(request));
        System.out.println(request.uri());
        HttpHandler delegate = new EmbeddedJmustacheHandler(root, context);

        if(isValidTopic(topic)){
            request.uri(templatePath);
        } else{
            request.uri(invalidTopic);
        }

        delegate.handleHttpRequest(request, response, control);
    }

    private boolean isValidTopic(String topic){
        return topics.contains(topic);
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
            return new ChatTopicsWorker(request, response, control, context);
        }

        protected class ChatTopicsWorker extends ResourceWorker {
            protected ChatTopicsWorker(HttpRequest request, HttpResponse response, HttpControl control, Object context) {
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
