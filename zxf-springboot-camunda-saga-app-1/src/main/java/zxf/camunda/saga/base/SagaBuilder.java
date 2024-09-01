package zxf.camunda.saga.base;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.builder.AbstractActivityBuilder;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.builder.ProcessBuilder;

public class SagaBuilder {
    @SuppressWarnings("rawtypes")
    private AbstractFlowNodeBuilder saga;
    private BpmnModelInstance bpmnModelInstance;
    private final String name;
    private final Boolean async;
    private ProcessBuilder process;

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
    public SagaBuilder activity(String name, Class adapterClass) {
        String id = "Activity-" + name.replace(" ", "-");
        saga = saga.serviceTask(id)
                .name(name)
                .camundaClass(adapterClass.getName())
                .camundaAsyncBefore(async)
                .camundaAsyncAfter(async)
                .camundaFailedJobRetryTimeCycle("R2/PT2M");
        return this;
    }

    @SuppressWarnings("rawtypes")
    public SagaBuilder activityWithoutRetry(String name, Class adapterClass) {
        String id = "Activity-" + name.replace(" ", "-");
        saga = saga.serviceTask(id)
                .name(name)
                .camundaClass(adapterClass.getName())
                .camundaAsyncBefore(async)
                .camundaAsyncAfter(async);
        return this;
    }

    @SuppressWarnings("rawtypes")
    public SagaBuilder compensationActivity(String name, Class adapterClass) {
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
                .camundaClass(adapterClass.getName())
                .compensationDone()
                .camundaAsyncBefore(async)
                .camundaAsyncAfter(async);

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
}