package ReadTestCommon;

import org.springframework.config.java.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AppContext {

//	@Bean
//    public SOAClientAnnotationProcessor postProcessor() {
//        return new SOAClientAnnotationProcessor();
//    }

    @Bean
    public SoaConfig serviceConfig() {
    	SoaConfig soaClientConfig = new SoaConfig();
        soaClientConfig.setConsumerId("1234abcd-ab32-cd12-abcd-8d363e44c8d9");
        soaClientConfig.setServiceName("service-artifact");
        soaClientConfig.setServiceVersion("0.0.1");
        String[] staticHeaders = {
        		"Key1=Value1",
        		"Key2=Value2"};
        soaClientConfig.setStaticHeaders(staticHeaders);
        return soaClientConfig;
    }
       
}
