package com.toptal.essienntaemmanuel2ndattempt.config;

import com.toptal.essienntaemmanuel2ndattempt.domain.Role;
import com.toptal.essienntaemmanuel2ndattempt.domain.Account;
import com.toptal.essienntaemmanuel2ndattempt.dto.AccountDto;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Collectors;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
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

        factory.classMap(Account.class, AccountDto.class)
                .customize(new CustomMapper<Account, AccountDto>() {

                    @Override
                    public void mapAtoB(Account a, AccountDto b, MappingContext context) {
                        b.setVerified(!a.getVerificationToken().isPresent());
                        b.setRoles(a.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
                    }

                    @Override
                    public void mapBtoA(AccountDto b, Account a, MappingContext context) {
                        a.setRoles(b.getRoles().stream().map(Role::new).collect(Collectors.toList()));
                        a.setPassword(passwordEncoder.encode(b.getPassword()));
                    }
                })
                .byDefault().register();

        converterFactory.registerConverter(new BidirectionalConverter<LocalDate, Date>() {

            @Override
            public Date convertTo(LocalDate source, Type<Date> destinationType, MappingContext mappingContext) {
                return Date.from(source.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            }

            @Override
            public LocalDate convertFrom(Date source, Type<LocalDate> destinationType, MappingContext mappingContext) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(source.getTime()), ZoneId.systemDefault()).toLocalDate();
            }
        });

        converterFactory.registerConverter(new BidirectionalConverter<LocalTime, Date>() {

            @Override
            public Date convertTo(LocalTime source, Type<Date> destinationType, MappingContext mappingContext) {
                return Date.from(source.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
            }

            @Override
            public LocalTime convertFrom(Date source, Type<LocalTime> destinationType, MappingContext mappingContext) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(source.getTime()), ZoneId.systemDefault()).toLocalTime();
            }
        });
        return factory.getMapperFacade();
    }
}
