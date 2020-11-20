package com.tcs.service.contract;

import com.tcs.service.contract.Provider;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.response.ResponseOptions;

import static org.springframework.cloud.contract.verifier.assertion.SpringCloudContractAssertions.assertThat;
import static org.springframework.cloud.contract.verifier.util.ContractVerifierUtil.*;
import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;

@SuppressWarnings("rawtypes")
public class ContractVerifierTest extends Provider {

	@Test
	public void validate_contract() throws Exception {
		// given:
			MockMvcRequestSpecification request = given();


		// when:
			ResponseOptions response = given().spec(request)
					.get("/api/v1/service-template/model/1");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);

		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("['responseCode']").isEqualTo("200");
			assertThatJson(parsedJson).field("['responseDescription']").isEqualTo("SUCCESS");
			assertThatJson(parsedJson).field("['response']").field("['id']").isEqualTo("1");
			assertThatJson(parsedJson).field("['response']").field("['modId']").isEqualTo("101");
			assertThatJson(parsedJson).field("['response']").field("['modDesc']").isEqualTo("Sample");
	}

}
