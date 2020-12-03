## RHPAM Integration interface

### Start a process

queue name:
```
moj.pam.process.start
```

| Header name   |      required      |  example |
|----------|:-------------:|------:|
| CORRELATION_KEY |  true | 1234L |
| PROCESS_ID |    true   |   `ekp-workflows.determine-message` or `ekp-workflows.RetrieveMessage` |
| EXECUTION_PLAN | false |   ```<?xml version="1.0" encoding="UTF-8"?> <ns3:processExecutionPlan xmlns:ns3="http://xmlns.ekp.justiz.de/process" xmlns:ns2="http://xmlns.retrieve.ekp.justiz.de" xmlns:ns4="http://egvp_enterprise.service.ekp.justiz.de/2.12.1/"> <ns3:activities> <ns3:activity id="EGVPEnterpriseService::fetchMessageByNachrichtId" executed="false" /> <ns3:activity id="EGVPEnterpriseService::confirmReception" executed="false" /> </ns3:activities> </ns3:processExecutionPlan>```|

Payload is valid JAXB XML. See `data-model` project for more details.

### Signal a process
queueu name:
```
moj.pam.process.signal
```

| Header name   |      required      |  example |
|----------|:-------------:|------:|
| CORRELATION_KEY |  true | 1234L |
| STATUS |    false   |   `SUCCESS` or `FAILURE` |

Payload is valid JAXB XML. When Status is set to `FAILURE` the payload is ignored on the RHPAM side.


### How to run me ?
Execute  `mvn clean install` in the root of `jbpm` folder - it will install all the sub-modules in correct order.
Then `cd` to `ekp-pam-orchestrator` and execute `mvn spring-boot:run -Dspring-boot.run.profiles=local`. RHPAM will try to connect to AMQ instance as configured in `src/main/resources/application-local.yml`

