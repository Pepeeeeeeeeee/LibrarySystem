package be.kdg.programming3.config;

import be.kdg.programming3.converter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final PageHistoryInterceptor pageHistoryInterceptor;
    private final StringToGenreConverter stringToGenreConverter;
    private final StringToMembershipTypeConverter stringToMembershipTypeConverter;
    private final StringToDepartmentConverter stringToDepartmentConverter;
    private final StringToCheckoutStatusConverter stringToCheckoutStatusConverter;

    @Autowired
    public WebConfig(
            StringToGenreConverter stringToGenreConverter,
            StringToMembershipTypeConverter stringToMembershipTypeConverter,
            StringToDepartmentConverter stringToDepartmentConverter,
            StringToCheckoutStatusConverter stringToCheckoutStatusConverter,
            PageHistoryInterceptor pageHistoryInterceptor
    ) {
        this.stringToGenreConverter = stringToGenreConverter;
        this.stringToMembershipTypeConverter = stringToMembershipTypeConverter;
        this.stringToDepartmentConverter = stringToDepartmentConverter;
        this.stringToCheckoutStatusConverter = stringToCheckoutStatusConverter;
        this.pageHistoryInterceptor = pageHistoryInterceptor;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToGenreConverter);
        registry.addConverter(stringToMembershipTypeConverter);
        registry.addConverter(stringToDepartmentConverter);
        registry.addConverter(stringToCheckoutStatusConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pageHistoryInterceptor)
                .addPathPatterns("/**") // track all pages
                .excludePathPatterns("/webjars/**", "/css/**", "/js/**", "/images/**");
    }
}
