package org.sid.cinema.sec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
@Configuration
@EnableWebSecurity
@CrossOrigin("*")
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	BCryptPasswordEncoder bCrye;
		
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	BCryptPasswordEncoder bcpe=getBCPE();
	
	  auth.inMemoryAuthentication().withUser("admin").password(bcpe.encode("1234"))
	  .roles("ADMIN","USER");
	  auth.inMemoryAuthentication().withUser("user").password(bcpe.encode("1234")).
	  roles("USER"); 
	  auth.inMemoryAuthentication().passwordEncoder(new
	  BCryptPasswordEncoder());
	 
	
	/*
	 * auth.jdbcAuthentication() .dataSource(dataSource).
	 * usersByUsernameQuery("select username as principal,password as credentials,active from users where username=?"
	 * )
	 * .authoritiesByUsernameQuery("select username as principale,roles as role from users_roles where username=?"
	 * ) .rolePrefix("ROLE_") .passwordEncoder(getBCPE());
	 */
	}

@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin();
		http.authorizeRequests().antMatchers("/AddCinema","/deleteVille","/AjouterVille","/deleteCinema","/AjouterCinema","/deleteSalle","/AddFilm","/deleteFilm","/ModifierVille").hasRole("ADMIN");
		http.exceptionHandling().accessDeniedPage("/403");
		
	}

	@Bean
	BCryptPasswordEncoder getBCPE() {
		return new BCryptPasswordEncoder();
	}

}
