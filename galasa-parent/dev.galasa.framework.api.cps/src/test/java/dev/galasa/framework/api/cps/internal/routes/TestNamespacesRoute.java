/*
 * Copyright contributors to the Galasa project
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package dev.galasa.framework.api.cps.internal.routes;
import dev.galasa.framework.api.cps.internal.CpsServletTest;
import dev.galasa.framework.api.cps.internal.mocks.MockCpsServlet;
import dev.galasa.framework.api.cps.internal.routes.TestNamespacesRoute;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

public class TestNamespacesRoute extends CpsServletTest{
    
    @Test
    public void TestGetNamespacesNoFrameworkReturnsError () throws Exception{
		// Given...
		setServlet("",null ,new HashMap<String,String[]>());
		MockCpsServlet servlet = getServlet();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		ServletOutputStream outStream = resp.getOutputStream();	
				
		// When...
		servlet.init();
		servlet.doGet(req,resp);

		// Then...
		// We expect an error back, because the API server couldn't find any Etcd store to query
		assertThat(resp.getStatus()).isEqualTo(500);
		assertThat(resp.getContentType()).isEqualTo("application/json");
		assertThat(resp.getHeader("Access-Control-Allow-Origin")).isEqualTo("*");

		checkErrorStructure(
			outStream.toString(),
			5000,
			"GAL5000E: ",
			"Error occured when trying to access the endpoint"
		);
    }

	@Test
	public void TestGetNamespacesWithFrameworkNoDataReturnsOk() throws Exception{
		// Given...
		setServlet("/","empty",new HashMap<String,String[]>());
		MockCpsServlet servlet = getServlet();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		ServletOutputStream outStream = resp.getOutputStream();	

		// When...
		servlet.init();
		servlet.doGet(req,resp);

		// Then...
		assertThat(resp.getStatus()).isEqualTo(200);
		assertThat(resp.getContentType()).isEqualTo("application/json");
		assertThat(resp.getHeader("Access-Control-Allow-Origin")).isEqualTo("*");
		assertThat(outStream.toString()).isEqualTo("[]");
	}

	@Test
	public void TestGetNamespacesWithFrameworkWithDataReturnsOk() throws Exception{
		// Given...
		setServlet("","framework",new HashMap<String,String[]>());
		MockCpsServlet servlet = getServlet();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		ServletOutputStream outStream = resp.getOutputStream();	

		// When...
		servlet.init();
		servlet.doGet(req,resp);
	
		// Then...
		assertThat(resp.getStatus()).isEqualTo(200);
		assertThat(resp.getContentType()).isEqualTo("application/json");
		assertThat(resp.getHeader("Access-Control-Allow-Origin")).isEqualTo("*");
		assertThat(outStream.toString()).isEqualTo("[\n"+
		"  {\n    \"name\": \"anamespace\",\n    \"propertiesUrl\": \"http://mock.galasa.server/cps/anamespace/properties\",\n    \"type\": \"normal\"\n  },\n"+
		"  {\n    \"name\": \"framework\",\n    \"propertiesUrl\": \"http://mock.galasa.server/cps/framework/properties\",\n    \"type\": \"normal\"\n  },\n"+
		"  {\n    \"name\": \"nampespace1\",\n    \"propertiesUrl\": \"http://mock.galasa.server/cps/nampespace1/properties\",\n    \"type\": \"normal\"\n  },\n"+
		"  {\n    \"name\": \"nampespace2\",\n    \"propertiesUrl\": \"http://mock.galasa.server/cps/nampespace2/properties\",\n    \"type\": \"normal\"\n  },\n"+
		"  {\n    \"name\": \"nampespace3\",\n    \"propertiesUrl\": \"http://mock.galasa.server/cps/nampespace3/properties\",\n    \"type\": \"normal\"\n  },\n"+
		"  {\n    \"name\": \"nampespace4\",\n    \"propertiesUrl\": \"http://mock.galasa.server/cps/nampespace4/properties\",\n    \"type\": \"normal\"\n  },\n"+
		"  {\n    \"name\": \"nampespace5\",\n    \"propertiesUrl\": \"http://mock.galasa.server/cps/nampespace5/properties\",\n    \"type\": \"normal\"\n  },\n"+
		"  {\n    \"name\": \"nampespace6\",\n    \"propertiesUrl\": \"http://mock.galasa.server/cps/nampespace6/properties\",\n    \"type\": \"normal\"\n  },\n"+
		"  {\n    \"name\": \"nampespace7\",\n    \"propertiesUrl\": \"http://mock.galasa.server/cps/nampespace7/properties\",\n    \"type\": \"normal\"\n  },\n"+
		"  {\n    \"name\": \"secure\",\n    \"propertiesUrl\": \"http://mock.galasa.server/cps/secure/properties\",\n    \"type\": \"secure\"\n  }"+
		"\n]");
	}

	@Test
	public void TestGetNamespacesWithFrameworkNullNamespacesReturnsError() throws Exception{
		// Given...
		setServlet("","error",new HashMap<String,String[]>());
		MockCpsServlet servlet = getServlet();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		ServletOutputStream outStream = resp.getOutputStream();	
				
		// When...
		servlet.init();
		servlet.doGet(req,resp);

		// Then...
		// We expect an error back, because the API server has thrown a ConfigurationPropertyStoreException
		assertThat(resp.getStatus()).isEqualTo(500);
		assertThat(resp.getContentType()).isEqualTo("application/json");
		assertThat(resp.getHeader("Access-Control-Allow-Origin")).isEqualTo("*");

		checkErrorStructure(
			outStream.toString(),
			5015,
			"E: Error occured when trying to access the Configuration Property Store.",
			" Report the problem to your Galasa Ecosystem owner."
		);
    }

	@Test
	public void TestGetNamespacesWithFrameworkBadPathReturnsError() throws Exception{
		// Given...
		setServlet(".","framework",new HashMap<String,String[]>());
		MockCpsServlet servlet = getServlet();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		ServletOutputStream outStream = resp.getOutputStream();	
				
		// When...
		servlet.init();
		servlet.doGet(req,resp);

		// Then...
		// We expect an error back, because the API server has thrown a ConfigurationPropertyStoreException
		assertThat(resp.getStatus()).isEqualTo(404);
		assertThat(resp.getContentType()).isEqualTo("application/json");
		assertThat(resp.getHeader("Access-Control-Allow-Origin")).isEqualTo("*");

		checkErrorStructure(
			outStream.toString(),
			5404,
			"E: Error occured when trying to identify the endpoint '.'. Please check your endpoint URL or report the problem to your Galasa Ecosystem owner."
		);
    }

	@Test
	public void TestGetNamespacesWithFrameworkBadPathWithSlashReturnsError() throws Exception{
		// Given...
		setServlet("/.","framework",new HashMap<String,String[]>());
		MockCpsServlet servlet = getServlet();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		ServletOutputStream outStream = resp.getOutputStream();	
				
		// When...
		servlet.init();
		servlet.doGet(req,resp);

		// Then...
		// We expect an error back, because the API server has thrown a ConfigurationPropertyStoreException
		assertThat(resp.getStatus()).isEqualTo(404);
		assertThat(resp.getContentType()).isEqualTo("application/json");
		assertThat(resp.getHeader("Access-Control-Allow-Origin")).isEqualTo("*");

		checkErrorStructure(
			outStream.toString(),
			5404,
			"E: Error occured when trying to identify the endpoint '/.'. Please check your endpoint URL or report the problem to your Galasa Ecosystem owner."
		);
    }

	/*
	 * TEST - HANDLE PUT REQUEST - should error as this method is not supported by this API end-point
	 */
	@Test
	public void TestGetNamespacesPUTRequestReturnsError() throws Exception{
		// Given...
		setServlet("","framework", null , "PUT");
		MockCpsServlet servlet = getServlet();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		ServletOutputStream outStream = resp.getOutputStream();	
				
		// When...
		servlet.init();
		servlet.doPut(req,resp);

		// Then...
		// We expect an error back, because the API server has thrown a ConfigurationPropertyStoreException
		assertThat(resp.getStatus()).isEqualTo(405);
		assertThat(resp.getContentType()).isEqualTo("application/json");
		assertThat(resp.getHeader("Access-Control-Allow-Origin")).isEqualTo("*");

		checkErrorStructure(
			outStream.toString(),
			5405,
			"E: Error occured when trying to access the endpoint ''. The method 'PUT' is not allowed."
		);
    }

	/*
	 * TEST - HANDLE POST REQUEST - should error as this method is not supported by this API end-point
	 */
	@Test
	public void TestGetNamespacesPOSTRequestReturnsError() throws Exception{
		// Given...
		setServlet("","framework",null, "POST");
		MockCpsServlet servlet = getServlet();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		ServletOutputStream outStream = resp.getOutputStream();	
				
		// When...
		servlet.init();
		servlet.doPost(req,resp);

		// Then...
		// We expect an error back, because the API server has thrown a ConfigurationPropertyStoreException
		assertThat(resp.getStatus()).isEqualTo(405);
		assertThat(resp.getContentType()).isEqualTo("application/json");
		assertThat(resp.getHeader("Access-Control-Allow-Origin")).isEqualTo("*");

		checkErrorStructure(
			outStream.toString(),
			5405,
			"E: Error occured when trying to access the endpoint ''. The method 'POST' is not allowed."
		);
    }

	/*
	 * TEST - HANDLE DELETE REQUEST - should error as this method is not supported by this API end-point
	 */
	@Test
	public void TestGetNamespacesDELETERequestReturnsError() throws Exception{
		// Given...
		setServlet("","framework",null, "DELETE");
		MockCpsServlet servlet = getServlet();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		ServletOutputStream outStream = resp.getOutputStream();	
				
		// When...
		servlet.init();
		servlet.doDelete(req,resp);

		// Then...
		// We expect an error back, because the API server has thrown a ConfigurationPropertyStoreException
		assertThat(resp.getStatus()).isEqualTo(405);
		assertThat(resp.getContentType()).isEqualTo("application/json");
		assertThat(resp.getHeader("Access-Control-Allow-Origin")).isEqualTo("*");

		checkErrorStructure(
			outStream.toString(),
			5405,
			"E: Error occured when trying to access the endpoint ''. The method 'DELETE' is not allowed."
		);
    }
}