package hsim.checkpoint.init;

import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.msg.MethodSyncor;
import hsim.checkpoint.core.repository.ValidationDataRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Init check point.
 */
@Slf4j
public class InitCheckPoint {

    private MethodSyncor methodSyncor = ComponentMap.get(MethodSyncor.class);
    private ValidationDataRepository validationDataRepository = ComponentMap.get(ValidationDataRepository.class);

    /**
     * Instantiates a new Init check point.
     */
    public InitCheckPoint() {
        this.validationDataRepository.refresh();
        this.methodSyncor.updateMethodKeyAsync();
        new Thread(() -> {
            this.validationDataRepository.datasRuleSync();
        }).start();
    }

}
