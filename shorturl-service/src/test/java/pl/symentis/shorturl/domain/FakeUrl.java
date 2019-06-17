package pl.symentis.shorturl.domain;

import com.devskiller.jfairy.Fairy;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class FakeUrl {

    private static Fairy fairy = Fairy.create();

    public static URL generateRandomUrl(){
        String url = fairy.company().getUrl();
        try {
            return new URI(url).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalStateException("Error during creating URL", e);
        }
    }
}
