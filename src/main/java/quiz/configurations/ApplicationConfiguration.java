package quiz.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quiz.models.UserHolder;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public UserHolder userHolderInstance() {
        return UserHolder.getInstance();
    }
}
