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
    private String name;
    private ProcessBuilder process;

    private SagaBuilder(String name) {
        this.name = name;
    }

    public static SagaBuilder newSaga(String name, Boolean async) {
        SagaBuilder builder = new SagaBuilder(name);
        return builder.start(async);
    }

    public BpmnModelInstance getModel() {
        if (bpmnModelInstance == null) {
            bpmnModelInstance = saga.done();
        }
        return bpmnModelInstance;
    }

    public SagaBuilder start(Boolean async) {
        process = Bpmn.createExecutableProcess(name);
        saga = process.startEvent("Start-" + name).camundaAsyncBefore(async);
        return this;
    }

    public SagaBuilder end() {
        saga = saga.endEvent("End-" + name);
        return this;
    }

    @SuppressWarnings("rawtypes")
    public SagaBuilder activity(String name, Class adapterClass) {
        // this is very handy and could also be done inline above directly
        String id = "Activity-" + name.replace(" ", "-"); // risky thing ;-)
        saga = saga.serviceTask(id).name(name).camundaClass(adapterClass.getName());
        return this;
    }

    @SuppressWarnings("rawtypes")
    public SagaBuilder compensationActivity(String name, Class adapterClass) {
        if (!(saga instanceof AbstractActivityBuilder)) {
            throw new RuntimeException("Compensation activity can only be specified right after activity");
        }

        String id = "Activity-" + name.replace(" ", "-") + "-compensation"; // risky thing ;-)

        ((AbstractActivityBuilder) saga)
                .boundaryEvent()
                .compensateEventDefinition()
                .compensateEventDefinitionDone()
                .compensationStart()
                .serviceTask(id).name(name).camundaClass(adapterClass.getName())
                .compensationDone();

        return this;
    }

    public SagaBuilder triggerCompensationOnAnyError() {
        process.eventSubProcess()
                .startEvent("ErrorCatched").error("java.lang.Throwable")
                .intermediateThrowEvent("ToBeCompensated").compensateEventDefinition().compensateEventDefinitionDone()
                .endEvent("ErrorHandled");

        return this;
    }

}