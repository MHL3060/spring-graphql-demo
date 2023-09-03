package local.demo;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

//@TestConfiguration
//public class SecurityTestConfiguration {
//
//    @Bean
//    @Primary
//    public MapReactiveUserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails user = User
//                .withUsername("foo")
//                .password(passwordEncoder.encode("bar"))
//                .build();
//        return new MapReactiveUserDetailsService(user);
//    }
//}
