package hsim.checkpoint.init;

import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.msg.MethodSyncor;
import hsim.checkpoint.core.repository.ValidationDataRepository;
import hsim.checkpoint.core.store.ValidationStore;
import hsim.checkpoint.util.AnnotationScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * The type Init check point.
 */
public class InitCheckPoint implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private Object[] beans;

    private MethodSyncor methodSyncor = ComponentMap.get(MethodSyncor.class);
    private ValidationDataRepository validationDataRepository = ComponentMap.get(ValidationDataRepository.class);
    private ValidationStore validationStore = ComponentMap.get(ValidationStore.class);
    private AnnotationScanner annotationScanner = ComponentMap.get(AnnotationScanner.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        this.annotationScanner.initBeans(this.beans);
        this.validationStore.refresh();

        new Thread(() -> {
            this.validationDataRepository.refresh();
            this.methodSyncor.updateMethodKeyAsync();
            this.validationDataRepository.datasRuleSync();
        }).start();
    }
}
