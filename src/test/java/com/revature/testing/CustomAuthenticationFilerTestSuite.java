package com.revature.testing;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.revature.exceptions.ProjectNotFoundException;
import com.revature.exceptions.SubversionAttemptException;
import com.revature.security.CustomAuthenticationFilter;
import com.revature.security.ZuulConfig;

/**
 * Test suite for class CustomAuthenticationFilter
 * 
 * @author Alonzo Muncy (190107-Java-Spark-USF)
 *
 */

@RunWith(MockitoJUnitRunner.class)
public class CustomAuthenticationFilerTestSuite {
	
	@Mock
	ZuulConfig mockZuulConfig;
	
	@Mock
	HttpServletRequest mockHttpServletRequest;
	
	@Mock
	HttpServletResponse mockHttpServletResponse;
	
	@Mock
	FilterChain mockFilterChain;
	
	@InjectMocks
	CustomAuthenticationFilter testClass = new CustomAuthenticationFilter(mockZuulConfig);
	
	
/**
 * This is for testing the do filter. We are not testing the servlet request or the servlet response, or the filter chain. So we input mock objects. As we are unit testing, we are going to stub validate header to return true when our valid response is passed to it.
 */
	@Test
	public void testDoFilterActuatorTrue() {
		
		//Getting past the first if statement.
		when(mockHttpServletRequest.getRequestURI()).thenReturn("/project/actuator");
		
		//This bit of fun is brought about by us because we need to bypass security.
		String aSecret = "Secret";
		String aSalt = "Salt";
		String validResponse = testClass.get_SHA_512_SecureHash(aSecret, aSalt);
		
		//When ZullConfig asks for header, then respond with our valid response.
		when(mockZuulConfig.getHeader()).thenReturn(validResponse);
		when(mockHttpServletRequest.getHeader(validResponse)).thenReturn(validResponse);
		try {
		testClass.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
			verify(mockFilterChain, times(1)).doFilter(mockHttpServletRequest, mockHttpServletResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
@Test
public void testDoFilterActuatorFalse() {
		
		//Getting past the first if statement.
		when(mockHttpServletRequest.getRequestURI()).thenReturn("/somethingElse");
		
		//This bit of fun is brought about by us because we need to bypass security.
		String aSecret = "Secret";
		String aSalt = "Salt";
		String validResponse = testClass.get_SHA_512_SecureHash(aSecret, aSalt);
		when(mockZuulConfig.getSecret()).thenReturn(aSecret);
		when(mockZuulConfig.getSalt()).thenReturn(aSalt);
		
		//When ZullConfig asks for header, then respond with our valid response.
		when(mockZuulConfig.getHeader()).thenReturn(validResponse);
		when(mockHttpServletRequest.getHeader(validResponse)).thenReturn(validResponse);
		try {
		testClass.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
			verify(mockFilterChain, times(1)).doFilter(mockHttpServletRequest, mockHttpServletResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

@Test
public void testDoFilterInvalidHeader() {
	when(mockHttpServletRequest.getRequestURI()).thenReturn("/somethingElse");
	try {
		testClass.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
		verify(mockHttpServletRequest, times(1)).getHeader("X-FORWARDED-FOR");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ServletException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

}
