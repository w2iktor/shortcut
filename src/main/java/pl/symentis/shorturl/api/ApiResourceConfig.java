package pl.symentis.shorturl.api;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

@ApplicationPath("/api")
@Component
public class ApiResourceConfig extends ResourceConfig{
	
	public ApiResourceConfig() {
        register(ApiListingResource.class);
        register(SwaggerSerializers.class);
		register(Redirects.class);
		register(Accounts.class);
		BeanConfig config = new BeanConfig();
		config.setBasePath("/api");
		config.setResourcePackage("pl.symentis.shorturl.api");
		config.setScan(true);
		register(config);
	}

}
