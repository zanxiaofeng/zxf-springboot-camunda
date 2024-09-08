package zxf.camunda.saga.base;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.builder.AbstractActivityBuilder;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.builder.ProcessBuilder;
import org.camunda.bpm.model.xml.impl.util.IoUtil;

@Slf4j
public class SagaBuilder {
    private final String name;
    private final Boolean async;
    private ProcessBuilder process;
    @SuppressWarnings("rawtypes")
    private AbstractFlowNodeBuilder saga;
    private BpmnModelInstance bpmnModelInstance;

    private SagaBuilder(String name, Boolean async) {
        this.name = name;
        this.async = async;
    }

    public static SagaBuilder newSaga(String name, Boolean async) {
        SagaBuilder builder = new SagaBuilder(name, async);
        return builder.start();
    }

    public BpmnModelInstance getModel() {
        if (bpmnModelInstance == null) {
            bpmnModelInstance = saga.done();
        }
        log.info("BPMN: " + IoUtil.convertXmlDocumentToString(bpmnModelInstance.getDocument()));
        return bpmnModelInstance;
    }

    public SagaBuilder start() {
        process = Bpmn.createExecutableProcess(name);
        saga = process.startEvent("Start-" + name)
                .camundaAsyncBefore(async)
                .camundaAsyncAfter(async);
        return this;
    }

    public SagaBuilder end() {
        saga = saga.endEvent("End-" + name);
        return this;
    }

    @SuppressWarnings("rawtypes")
    public SagaBuilder activityNoRetry(String name, String adapterClass) {
        String id = "Activity-" + name.replace(" ", "-");
        //By default, a failed job will be retried three times and the retries are performed immediately after the failure
        saga = saga.serviceTask(id)
                .name(name)
                //Override the value of camunda.bpm.default-number-of-retries.
                .camundaFailedJobRetryTimeCycle("R0/PT0S")
                .camundaClass(adapterClass)
                .camundaAsyncBefore(async)
                .camundaAsyncAfter(async);
        return this;
    }

    @SuppressWarnings("rawtypes")
    public SagaBuilder activity(String name, String adapterClass, String retryTimeCycle) {
        String id = "Activity-" + name.replace(" ", "-");
        saga = saga.serviceTask(id)
                .name(name)
                .camundaFailedJobRetryTimeCycle(retryTimeCycle)
                .camundaClass(adapterClass)
                .camundaAsyncBefore(async)
                .camundaAsyncAfter(async);
        return this;
    }

    @SuppressWarnings("rawtypes")
    public SagaBuilder compensationActivity(String name, String adapterClass) {
        if (!(saga instanceof AbstractActivityBuilder)) {
            throw new RuntimeException("Compensation activity can only be specified right after activity");
        }

        String id = "Activity-" + name.replace(" ", "-") + "-compensation";
        ((AbstractActivityBuilder) saga)
                .boundaryEvent()
                .compensateEventDefinition()
                .compensateEventDefinitionDone()
                .compensationStart()
                .serviceTask(id)
                .name(name)
                .camundaClass(adapterClass)
                .camundaAsyncBefore(async)
                .camundaAsyncAfter(async)
                .compensationDone();

        return this;
    }

    public SagaBuilder triggerCompensationOnAnyError() {
        process.eventSubProcess()
                .startEvent("ErrorCatched")
                .error("java.lang.Throwable")
                .intermediateThrowEvent("ToBeCompensated")
                .compensateEventDefinition()
                .compensateEventDefinitionDone()
                .endEvent("ErrorHandled");

        return this;
    }

    public SagaBuilder triggerCompensationActivityOnAnyError(String name, String adapterClass) {
        String id = "CompensationActivity-" + name.replace(" ", "-");
        process.eventSubProcess()
                .startEvent("ErrorCatched")
                .error("java.lang.Throwable")
                .intermediateThrowEvent("ToBeCompensated")
                .compensateEventDefinition()
                .compensateEventDefinitionDone()
                .serviceTask(id)
                .name(name)
                .camundaClass(adapterClass)
                .camundaAsyncBefore(async)
                .camundaAsyncAfter(async)
                .endEvent("ErrorHandled");

        return this;
    }
}