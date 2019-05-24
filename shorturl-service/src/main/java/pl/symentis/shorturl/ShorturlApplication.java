package pl.symentis.shorturl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@EnableSwagger2
public class ShorturlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShorturlApplication.class, args);
	}

//  @Bean
//  public Docket api() {
//      return new Docket(DocumentationType.SWAGGER_2)
//          .select()
//          .apis(RequestHandlerSelectors.basePackage("pl.symentis.shorturl.api"))
//          .paths(PathSelectors.regex("/.*"))
//          .build()
//          .apiInfo(apiEndPointsInfo());
//  }
//  private ApiInfo apiEndPointsInfo() {
//      return new ApiInfoBuilder()
//          .title("Shorturl for MongoDB")
//          .description("REST API")
//          .contact(new Contact("Symentis.pl", "www.symentis.pl", "kontakt@symentis.pl"))
//          .license("Apache 2.0")
//          .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
//          .version("1.0.0")
//          .build();
//  }

}
