/*
 * Copyright contributors to the Galasa project
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package dev.galasa.framework.api.ras.internal;
import dev.galasa.framework.spi.IRunResult;
import dev.galasa.framework.spi.ResultArchiveStoreException;
import dev.galasa.framework.spi.teststructure.TestStructure;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.RandomStringUtils;

import dev.galasa.framework.api.common.mocks.MockHttpServletRequest;
import dev.galasa.framework.api.ras.internal.mocks.*;
import static org.assertj.core.api.Assertions.*;

import java.util.*;

import dev.galasa.framework.spi.utils.GalasaGsonBuilder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

public class TestResultNamesRoute extends RasServletTest{

    final static Gson gson = GalasaGsonBuilder.build();

    public List<IRunResult> generateTestData (int resSize){
		List<IRunResult> mockInputRunResults = new ArrayList<IRunResult>();
		// Build the results the DB will return.
		for(int c =0 ; c < resSize; c++){
			String runName = RandomStringUtils.randomAlphanumeric(5);
			String testShortName = RandomStringUtils.randomAlphanumeric(5);
			String requestor = RandomStringUtils.randomAlphanumeric(8);
			String runId = RandomStringUtils.randomAlphanumeric(16);
			TestStructure testStructure = new TestStructure();
			testStructure.setRunName(runName);
			testStructure.setRequestor(requestor);
			testStructure.setTestShortName(testShortName);
			testStructure.setBundle(RandomStringUtils.randomAlphanumeric(16));
			testStructure.setTestName(testShortName + "." + RandomStringUtils.randomAlphanumeric(8));
			testStructure.setQueued(Instant.now());
			testStructure.setStartTime(Instant.now());
			testStructure.setEndTime(Instant.now());
            switch (c % 5){
                case 0:	testStructure.setResult("Passed");
                    break;
				case 1:	testStructure.setResult("Failed");
                    break;
                case 2:	testStructure.setResult("EnvFail");
                    break;
                case 3:	testStructure.setResult("UNKNOWN");
                    break;
                case 4:	testStructure.setResult("Ignored");
                    break;
            }
			Path artifactRoot = Paths.get(RandomStringUtils.randomAlphanumeric(12));
			String log = RandomStringUtils.randomAlphanumeric(6);
			IRunResult result = new MockRunResult( runId, testStructure, artifactRoot , log);
			mockInputRunResults.add(result);
		}
		return mockInputRunResults;
	}

    private String generateExpectedJSON (List<IRunResult> mockInputRunResults, boolean reverse) throws ResultArchiveStoreException{
        List<String> resultNames = new ArrayList<>();
		resultNames.add("Passed");
		resultNames.add("Failed");
		resultNames.add("EnvFail");
		resultNames.add("Ignored");
		for (IRunResult run : mockInputRunResults){
				String result  = run.getTestStructure().getResult().toString();
				if (!resultNames.contains(result)){
					resultNames.add(result);
				}
		}
        Collections.sort(resultNames);
        if (reverse == true) {
            Collections.reverse(resultNames);
        }
		JsonElement jsonResultsArray = new Gson().toJsonTree(resultNames);
		JsonObject json = new JsonObject();
		json.add("resultnames", jsonResultsArray);
		return json.toString();
    }
    /*
     * Tests
     */

    @Test
	public void testResultNamesWithOnePassingTestReturnsOK() throws Exception {
		//Given..
		List<IRunResult> mockInputRunResults = generateTestData(1);
		//Build Http query parameters

        Map<String, String[]> parameterMap = new HashMap<String,String[]>();
		MockHttpServletRequest mockRequest = new MockHttpServletRequest(parameterMap, "/resultnames");
		MockRasServletEnvironment mockServletEnvironment = new MockRasServletEnvironment( mockInputRunResults,mockRequest);

		RasServlet servlet = mockServletEnvironment.getServlet();
		HttpServletRequest req = mockServletEnvironment.getRequest();
		HttpServletResponse resp = mockServletEnvironment.getResponse();
		ServletOutputStream outStream = resp.getOutputStream();

		//When...
		servlet.init();
		servlet.doGet(req,resp);

		//Then...
		// Expecting:
        //[
        //  "Passed"
        //]
		String expectedJson = generateExpectedJSON(mockInputRunResults, false);
		assertThat(resp.getStatus()).isEqualTo(200);
		assertThat( outStream.toString() ).isEqualTo(expectedJson);
		assertThat( resp.getContentType()).isEqualTo("application/json");
		assertThat( resp.getHeader("Access-Control-Allow-Origin")).isEqualTo("*");
	}

    @Test
	public void testResultNamesWithOnePassingOneFailingTestReturnsOK() throws Exception {
		//Given..
		List<IRunResult> mockInputRunResults = generateTestData(2);
		//Build Http query parameters

        Map<String, String[]> parameterMap = new HashMap<String,String[]>();
		MockHttpServletRequest mockRequest = new MockHttpServletRequest(parameterMap, "/resultnames");
		MockRasServletEnvironment mockServletEnvironment = new MockRasServletEnvironment( mockInputRunResults,mockRequest);

		RasServlet servlet = mockServletEnvironment.getServlet();
		HttpServletRequest req = mockServletEnvironment.getRequest();
		HttpServletResponse resp = mockServletEnvironment.getResponse();
		ServletOutputStream outStream = resp.getOutputStream();

		//When...
		servlet.init();
		servlet.doGet(req,resp);

		//Then...
		// Expecting:
        //[
		//  "Failed",
        //  "Passed"
        //]
		String expectedJson = generateExpectedJSON(mockInputRunResults, false);
		assertThat(resp.getStatus()).isEqualTo(200);
		assertThat( outStream.toString() ).isEqualTo(expectedJson);
		assertThat( resp.getContentType()).isEqualTo("application/json");
		assertThat( resp.getHeader("Access-Control-Allow-Origin")).isEqualTo("*");
	}

    @Test
	public void testResultNamesWithTenTestsFiveResultsReturnsOK() throws Exception {
		//Given..
		List<IRunResult> mockInputRunResults = generateTestData(10);
		//Build Http query parameters

        Map<String, String[]> parameterMap = new HashMap<String,String[]>();
		MockHttpServletRequest mockRequest = new MockHttpServletRequest(parameterMap, "/resultnames");
		MockRasServletEnvironment mockServletEnvironment = new MockRasServletEnvironment( mockInputRunResults,mockRequest);

		RasServlet servlet = mockServletEnvironment.getServlet();
		HttpServletRequest req = mockServletEnvironment.getRequest();
		HttpServletResponse resp = mockServletEnvironment.getResponse();
		ServletOutputStream outStream = resp.getOutputStream();

		//When...
		servlet.init();
		servlet.doGet(req,resp);

		//Then...
		// Expecting:
        //[
		//  "EnvFail",
		//  "Failed",
		//  "Ignored",
        //  "Passed",
		//  "UNKNOWN"
        //]
		String expectedJson = generateExpectedJSON(mockInputRunResults, false);
		assertThat(resp.getStatus()).isEqualTo(200);
		assertThat( outStream.toString() ).isEqualTo(expectedJson);
		assertThat( resp.getContentType()).isEqualTo("application/json");
		assertThat( resp.getHeader("Access-Control-Allow-Origin")).isEqualTo("*");
	}

    @Test
	public void testResultNamesWithTenTestsFiveResultsWithSortDescendingReturnsOK() throws Exception {
		//Given..
		List<IRunResult> mockInputRunResults = generateTestData(10);
		//Build Http query parameters

        Map<String, String[]> parameterMap = new HashMap<String,String[]>();
		parameterMap.put("sort", new String[] {"resultnames:desc"});
		MockHttpServletRequest mockRequest = new MockHttpServletRequest(parameterMap, "/resultnames");
		MockRasServletEnvironment mockServletEnvironment = new MockRasServletEnvironment( mockInputRunResults,mockRequest);

		RasServlet servlet = mockServletEnvironment.getServlet();
		HttpServletRequest req = mockServletEnvironment.getRequest();
		HttpServletResponse resp = mockServletEnvironment.getResponse();
		ServletOutputStream outStream = resp.getOutputStream();

		//When...
		servlet.init();
		servlet.doGet(req,resp);

		//Then...
		// Expecting:
        //[
		//  "UNKNOWN",
		//  "Passed",
		//  "Ignored",
		//  "Failed",
		//  "EnvFail"
        //]
		String expectedJson = generateExpectedJSON(mockInputRunResults, true);
		assertThat(resp.getStatus()).isEqualTo(200);
		assertThat( outStream.toString() ).isEqualTo(expectedJson);
		assertThat( resp.getContentType()).isEqualTo("application/json");
		assertThat( resp.getHeader("Access-Control-Allow-Origin")).isEqualTo("*");
	}

    @Test
	public void testResultNamesWithTenTestsFiveResultsWithSortAscendingReturnsOK() throws Exception {
		//Given..
		List<IRunResult> mockInputRunResults = generateTestData(10);
		//Build Http query parameters

        Map<String, String[]> parameterMap = new HashMap<String,String[]>();
		parameterMap.put("sort", new String[] {"resultnames:asc"});
		MockHttpServletRequest mockRequest = new MockHttpServletRequest(parameterMap, "/resultnames");
		MockRasServletEnvironment mockServletEnvironment = new MockRasServletEnvironment( mockInputRunResults,mockRequest);

		RasServlet servlet = mockServletEnvironment.getServlet();
		HttpServletRequest req = mockServletEnvironment.getRequest();
		HttpServletResponse resp = mockServletEnvironment.getResponse();
		ServletOutputStream outStream = resp.getOutputStream();

		//When...
		servlet.init();
		servlet.doGet(req,resp);

		//Then...
		// Expecting:
        //[
		//  "EnvFail",
		//  "Failed",
		//  "Ignored",
        //  "Passed",
		//  "UNKNOWN"
        //]
		String expectedJson = generateExpectedJSON(mockInputRunResults, false);
		assertThat(resp.getStatus()).isEqualTo(200);
		assertThat( outStream.toString() ).isEqualTo(expectedJson);
		assertThat( resp.getContentType()).isEqualTo("application/json");
		assertThat( resp.getHeader("Access-Control-Allow-Origin")).isEqualTo("*");
	}

    @Test
	public void testResultNamesWithBadSortReturnsError() throws Exception {
		//Given..
		List<IRunResult> mockInputRunResults = generateTestData(10);
		//Build Http query parameters

        Map<String, String[]> parameterMap = new HashMap<String,String[]>();
		parameterMap.put("sort", new String[] {"resultnames:jindex"});
		MockHttpServletRequest mockRequest = new MockHttpServletRequest(parameterMap, "/resultnames");
		MockRasServletEnvironment mockServletEnvironment = new MockRasServletEnvironment( mockInputRunResults,mockRequest);

		RasServlet servlet = mockServletEnvironment.getServlet();
		HttpServletRequest req = mockServletEnvironment.getRequest();
		HttpServletResponse resp = mockServletEnvironment.getResponse();
		ServletOutputStream outStream = resp.getOutputStream();

		//When...
		servlet.init();
		servlet.doGet(req,resp);

		//Then...
		// Expecting:
        //[
        //  GAL5011E: Error parsing the query parameters. sort value 'resultnames' not recognised.
		//  Expected query parameter in the format sort={fieldName}:{order} where order is asc for ascending or desc for descending.
        //]
		checkErrorStructure(
			outStream.toString(),
			5011,
			"GAL5011E: ",
			"resultnames"
		);
	}

	@Test
	public void testResultNamesWithNoResultsReturnsError() throws Exception {
		//Given..
		List<IRunResult> mockInputRunResults = new ArrayList<IRunResult>();
		// Build the results the DB will return.
			String runName = RandomStringUtils.randomAlphanumeric(5);
			String testShortName = RandomStringUtils.randomAlphanumeric(5);
			String requestor = RandomStringUtils.randomAlphanumeric(8);
			String runId = RandomStringUtils.randomAlphanumeric(16);
			TestStructure testStructure = new TestStructure();
			testStructure.setRunName(runName);
			testStructure.setRequestor(requestor);
			testStructure.setTestShortName(testShortName);
			testStructure.setBundle(RandomStringUtils.randomAlphanumeric(16));
			testStructure.setTestName(testShortName + "." + RandomStringUtils.randomAlphanumeric(8));
			testStructure.setQueued(Instant.now());
			testStructure.setStartTime(Instant.now());
			testStructure.setEndTime(Instant.now());
			testStructure.setResult("ForceException");
			IRunResult result = new MockRunResult( runId, testStructure, null , null);
			mockInputRunResults.add(result);
		//Build Http query parameters

        Map<String, String[]> parameterMap = new HashMap<String,String[]>();
		MockHttpServletRequest mockRequest = new MockHttpServletRequest(parameterMap, "/resultnames");
		MockRasServletEnvironment mockServletEnvironment = new MockRasServletEnvironment( mockInputRunResults,mockRequest);

		RasServlet servlet = mockServletEnvironment.getServlet();
		HttpServletRequest req = mockServletEnvironment.getRequest();
		HttpServletResponse resp = mockServletEnvironment.getResponse();
		ServletOutputStream outStream = resp.getOutputStream();

		//When...
		servlet.init();
		servlet.doGet(req,resp);

		//Then...
		// Expecting:
        //[
        //  GAL5011E: Error parsing the query parameters. sort value 'resultnames' not recognised.
		//  Expected query parameter in the format sort={fieldName}:{order} where order is asc for ascending or desc for descending.
        //]
		checkErrorStructure(
			outStream.toString(),
			5004,
			"GAL5004E: ",
			"Error retrieving page."
		);
	}

	@Test
	public void testResultNamesWithZeroTestsReturnsOK() throws Exception {
		//Given..
		List<IRunResult> mockInputRunResults = generateTestData(0);
		//Build Http query parameters

        Map<String, String[]> parameterMap = new HashMap<String,String[]>();
		MockHttpServletRequest mockRequest = new MockHttpServletRequest(parameterMap, "/resultnames");
		MockRasServletEnvironment mockServletEnvironment = new MockRasServletEnvironment( mockInputRunResults,mockRequest);

		RasServlet servlet = mockServletEnvironment.getServlet();
		HttpServletRequest req = mockServletEnvironment.getRequest();
		HttpServletResponse resp = mockServletEnvironment.getResponse();
		ServletOutputStream outStream = resp.getOutputStream();

		//When...
		servlet.init();
		servlet.doGet(req,resp);

		//Then...
		// Expecting:
        //[
		//  "EnvFail",
		//  "Failed",
		//  "Ignored",
        //  "Passed"
        //]
		String expectedJson = generateExpectedJSON(mockInputRunResults, false);
		assertThat(resp.getStatus()).isEqualTo(200);
		assertThat( outStream.toString() ).isEqualTo(expectedJson);
		assertThat( resp.getContentType()).isEqualTo("application/json");
		assertThat( resp.getHeader("Access-Control-Allow-Origin")).isEqualTo("*");
	}
}