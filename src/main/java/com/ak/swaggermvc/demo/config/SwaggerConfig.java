package com.ak.swaggermvc.demo.config;

import com.mangofactory.swagger.configuration.JacksonScalaSupport;
import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.configuration.SpringSwaggerModelConfig;
import com.mangofactory.swagger.configuration.SwaggerGlobalSettings;
import com.mangofactory.swagger.core.SwaggerApiResourceListing;
import com.mangofactory.swagger.scanners.ApiListingReferenceScanner;
import com.wordnik.swagger.model.ApiInfo;
import com.wordnik.swagger.model.ApiKey;
import com.wordnik.swagger.model.AuthorizationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

   @Autowired
   private SpringSwaggerConfig springSwaggerConfig;
   @Autowired
   private SpringSwaggerModelConfig springSwaggerModelConfig;

   /**
    * Adds the jackson scala module to the MappingJackson2HttpMessageConverter registered with spring
    * Swagger  core models are scala so we need to be able to convert to JSON
    */
   @Bean
   public JacksonScalaSupport jacksonScalaSupport(){
      JacksonScalaSupport jacksonScalaSupport = new JacksonScalaSupport();
      //Set to false to disable
      jacksonScalaSupport.setRegisterScalaModule(true);
      return jacksonScalaSupport;
   }

   @Bean
   @Autowired
   public SwaggerApiResourceListing swaggerApiResourceListing() {
      SwaggerApiResourceListing swaggerApiResourceListing = new SwaggerApiResourceListing(springSwaggerConfig.swaggerCache(), "business-api");
      swaggerApiResourceListing.setSwaggerPathProvider(demoPathProvider());
      swaggerApiResourceListing.setApiInfo(apiInfo());
      swaggerApiResourceListing.setAuthorizationTypes(authorizationTypes());
      swaggerApiResourceListing.setSwaggerGlobalSettings(swaggerGlobalSettings());

      ApiListingReferenceScanner apiListingReferenceScanner = apiListingReferenceScanner();
      swaggerApiResourceListing.setApiListingReferenceScanner(apiListingReferenceScanner);
      return swaggerApiResourceListing;
   }

   @Bean
   public SwaggerGlobalSettings swaggerGlobalSettings(){
      SwaggerGlobalSettings swaggerGlobalSettings = new SwaggerGlobalSettings();
      swaggerGlobalSettings.setGlobalResponseMessages(springSwaggerConfig.defaultResponseMessages());
      swaggerGlobalSettings.setIgnorableParameterTypes(springSwaggerConfig.defaultIgnorableParameterTypes());
      swaggerGlobalSettings.setParameterDataTypes(springSwaggerModelConfig.defaultParameterDataTypes());
      return  swaggerGlobalSettings;
   }

   @Bean
   public ApiListingReferenceScanner apiListingReferenceScanner() {
      ApiListingReferenceScanner apiListingReferenceScanner = new ApiListingReferenceScanner();
      apiListingReferenceScanner.setRequestMappingHandlerMapping(springSwaggerConfig.swaggerRequestMappingHandlerMappings());
      apiListingReferenceScanner.setExcludeAnnotations(springSwaggerConfig.defaultExcludeAnnotations());
      apiListingReferenceScanner.setControllerNamingStrategy(springSwaggerConfig.defaultControllerResourceNamingStrategy());
//      apiListingReferenceScanner.setSwaggerPathProvider(springSwaggerConfig.defaultSwaggerPathProvider());
      apiListingReferenceScanner.setSwaggerPathProvider(demoPathProvider());
      //Must match the swagger group set on SwaggerApiResourceListing
      apiListingReferenceScanner.setSwaggerGroup("business-api");
      //Only add the businesses endpoints to this api listing
      apiListingReferenceScanner.setIncludePatterns(
            Arrays.asList(new String[]{
                  "/business.*",
                  "/operations.*",
                   "/contacts.*"
            })
      );
      return apiListingReferenceScanner;
   }

    @Bean
    public DemoPathProvider demoPathProvider(){
        DemoPathProvider demoPathProvider = new DemoPathProvider();
        demoPathProvider.setDefaultSwaggerPathProvider(springSwaggerConfig.defaultSwaggerPathProvider());
        return demoPathProvider;
    }

    private List<AuthorizationType> authorizationTypes() {
      ArrayList<AuthorizationType> authorizationTypes = new ArrayList<AuthorizationType>();
      authorizationTypes.add(new ApiKey("x-auth-token", "header"));
      return authorizationTypes;
   }

   private ApiInfo apiInfo() {
      ApiInfo apiInfo = new ApiInfo(
            "Demo Spring MVC swagger 1.2 api",
            "Sample spring mvc api based on the swagger 1.2 spec",
            "http://en.wikipedia.org/wiki/Terms_of_service",
            "somecontact@somewhere.com",
            "Apache 2.0",
            "http://www.apache.org/licenses/LICENSE-2.0.html"
      );
      return apiInfo;
   }
}
