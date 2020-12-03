package org.redhat.moj.ekp.pam;

import java.util.Collection;

import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.model.ProcessInstanceDesc;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.query.QueryContext;
import org.kie.internal.process.CorrelationKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Component
public class BPMAssertUtils {

    @Autowired
    private ProcessService processService;

    @Autowired
    private RuntimeDataService runtimeService;

    public void assertProcessCompleted(CorrelationKey ck) {

        Collection<ProcessInstanceDesc> pid = runtimeService.getProcessInstancesByCorrelationKey(ck,
                new QueryContext());
        assertNotNull(pid);

        ProcessInstanceDesc p = pid.iterator().next();
        runtimeService.getProcessInstanceFullHistory(p.getId(), new QueryContext());

        assertEquals(ProcessInstance.STATE_COMPLETED, p.getState());

    }

    public void assertProcessActive(CorrelationKey ck) {

        ProcessInstance pi = processService.getProcessInstance(ck);
        assertNotNull(pi);
        assertEquals(ProcessInstance.STATE_ACTIVE, pi.getState());
    }

    public void assertSignal(CorrelationKey ck, String signalName) {

        ProcessInstance pi = processService.getProcessInstance(ck);
        Collection<String> signals = processService.getAvailableSignals(pi.getId());
        assertNotNull(signals);
        assertEquals(signalName, signals.iterator().next());
    }

}
