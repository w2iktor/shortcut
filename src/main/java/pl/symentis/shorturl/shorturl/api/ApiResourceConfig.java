package pl.symentis.shorturl.shorturl.api;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

@ApplicationPath("/api")
@Component
public class ApiResourceConfig extends ResourceConfig{
	
	public ApiResourceConfig() {
        register(ApiListingResource.class);
        register(SwaggerSerializers.class);
        
		register(Shortcodes.class);
	}

}
