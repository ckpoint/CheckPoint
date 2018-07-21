package hsim;

import hsim.checkpoint.core.component.validationRule.rule.AssistType;
import hsim.checkpoint.core.component.validationRule.type.StandardValueType;
import hsim.checkpoint.helper.CheckPointHelper;
import hsim.model.TestRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The type Boot app.
 */
@SpringBootApplication
@EnableScheduling
@Configuration
@PropertySource("classpath:app.properties")
public class BootApp {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BootApp.class, args);

        CheckPointHelper checkPointHelper = new CheckPointHelper();
        checkPointHelper.addValidationRule("testRule", StandardValueType.NUMBER, new TestRule(), AssistType.all()).flush();
    }

}
