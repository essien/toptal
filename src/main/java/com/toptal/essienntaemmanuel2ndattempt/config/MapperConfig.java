package com.toptal.essienntaemmanuel2ndattempt.config;

import com.toptal.essienntaemmanuel2ndattempt.domain.User;
import com.toptal.essienntaemmanuel2ndattempt.dto.UserDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.TimeZone;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author bodmas
 */
@Configuration
public class MapperConfig {

    private static final Logger log = LoggerFactory.getLogger(MapperConfig.class);

    @Bean
    public MapperFacade createMapperFacade(PasswordEncoder passwordEncoder) {
        ma.glasnost.orika.MapperFactory factory = new DefaultMapperFactory.Builder().build();

        ConverterFactory converterFactory = factory.getConverterFactory();
        converterFactory.registerConverter(new PassThroughConverter(LocalDateTime.class,
                LocalDate.class, TimeZone.class, LocalTime.class));

        factory.classMap(User.class, UserDto.class)
                .customize(new CustomMapper<User, UserDto>() {

                    @Override
                    public void mapAtoB(User a, UserDto b, MappingContext context) {
                        b.setVerified(!a.getVerificationToken().isPresent());
                    }

                    @Override
                    public void mapBtoA(UserDto b, User a, MappingContext context) {
                        a.setPassword(passwordEncoder.encode(b.getPassword()));
                    }
                })
                .byDefault().register();

        return factory.getMapperFacade();
    }
}
