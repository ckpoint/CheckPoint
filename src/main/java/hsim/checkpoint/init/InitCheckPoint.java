package hsim.checkpoint.init;

import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.msg.MethodSyncor;
import hsim.checkpoint.core.repository.ValidationDataRepository;
import hsim.checkpoint.core.store.ValidationStore;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Init check point.
 */
@Slf4j
public class InitCheckPoint {

    private MethodSyncor methodSyncor = ComponentMap.get(MethodSyncor.class);
    private ValidationDataRepository validationDataRepository = ComponentMap.get(ValidationDataRepository.class);
    private ValidationStore validationStore = ComponentMap.get(ValidationStore.class);

    /**
     * Instantiates a new Init check point.
     */
    public InitCheckPoint() {
        this.validationStore.refresh();

        new Thread(() -> {
            this.validationDataRepository.refresh();
            this.methodSyncor.updateMethodKeyAsync();
            this.validationDataRepository.datasRuleSync();
        }).start();
    }

}
