package org.redhat.moj.ekp.pam;

import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.RuntimeDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.internal.KieInternalServices;
import org.kie.internal.process.CorrelationKeyFactory;
import org.kie.server.api.model.definition.QueryParam;
import org.kie.server.api.model.definition.SearchQueryFilterSpec;
import org.kie.server.api.model.instance.ProcessInstanceUserTaskWithVariables;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.QueryServicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kie.server.api.util.QueryParamFactory.equalsTo;


@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class SearchAPITest {

    private CorrelationKeyFactory correlationKeyFactory = KieInternalServices.Factory.get().newCorrelationKeyFactory();

    @Autowired
    private ProcessService processService;

    @Autowired
    private RuntimeDataService runtimeService;

    private static final Logger log = LoggerFactory.getLogger(SearchAPITest.class);

    private static final String PROCESS_ID_WITH_DASH = "temenos-examples-1.process-ht";
    private static final String PROCESS_ID_WITHOUT_DASH = "withoutDash";

    private static final String DEPLOYMENT_ID = "searc-api-container";
    private static final String URL =  "http://localhost:8090/rest/server";
    private static final String USERNAME = "kieserver";
    private static final String PW = "password1!";


    @Test //this will fail because of parameter-1
    public void searchApiTestParameterWithDash() {

        Long pid = processService.startProcess(DEPLOYMENT_ID,PROCESS_ID_WITH_DASH);

        SearchQueryFilterSpec searchSpec = new SearchQueryFilterSpec();
        List<QueryParam> taskVars = new ArrayList<QueryParam>();
        taskVars.add(equalsTo("parameter-1","parameter-1-value"));
        searchSpec.setTaskVariablesQueryParams(taskVars);

        KieServicesConfiguration configuration = KieServicesFactory.newRestConfiguration(URL,USERNAME,PW);
        KieServicesClient client = KieServicesFactory.newKieServicesClient(configuration);
        QueryServicesClient queryServicesClient = client.getServicesClient(QueryServicesClient.class);

        List<ProcessInstanceUserTaskWithVariables> result = queryServicesClient.queryUserTaskByVariables(searchSpec,0,5);
        result.forEach( r -> {

            log.info(" {} ", r);
        });

        assertEquals(1,result.size());


    }

    @Test //this works just fine because of parameter1
    public void searchApiTestParameterWithoutDash() {

        Long pid = processService.startProcess(DEPLOYMENT_ID,PROCESS_ID_WITHOUT_DASH);

        SearchQueryFilterSpec searchSpec = new SearchQueryFilterSpec();
        List<QueryParam> taskVars = new ArrayList<QueryParam>();
        taskVars.add(equalsTo("parameter1","parameter-1-value"));
        searchSpec.setTaskVariablesQueryParams(taskVars);

        KieServicesConfiguration configuration = KieServicesFactory.newRestConfiguration(URL,USERNAME,PW);
        KieServicesClient client = KieServicesFactory.newKieServicesClient(configuration);
        QueryServicesClient queryServicesClient = client.getServicesClient(QueryServicesClient.class);

        List<ProcessInstanceUserTaskWithVariables> result = queryServicesClient.queryUserTaskByVariables(searchSpec,0,5);
        result.forEach( r -> {

            log.info(" {} ", r);
        });

        assertEquals(1,result.size());


    }

}
