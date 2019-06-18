package pl.symentis.shorturl.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Configuration
@ConfigurationProperties( prefix = "payment-gateway" )
public class PaymentGatewayProperties
{
    private URL url;

    public PaymentGatewayProperties()
    {
        super();
    }

    public PaymentGatewayProperties( URL url )
    {
        this.url = url;
    }

    public URL getUrl()
    {
        return url;
    }

    public void setUrl( URL url )
    {
        this.url = url;
    }
}
