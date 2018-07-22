package hsim.checkpoint.test.repoistory;

import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.core.repository.ValidationDataRepository;
import hsim.checkpoint.type.ParamType;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RepositoryTest {

    private ValidationDataRepository validationDataRepository = ComponentMap.get(ValidationDataRepository.class);

    @Test
    public void test_order_0_init() {
        this.validationDataRepository.refresh();
    }

    @Test
    public void test_order_1_saveTest() {
        ValidationData data = new ValidationData();
        data.setParamType(ParamType.BODY);
        data.setUrl("/order/url");
        data.setMethod("POST");
        data.setName("order");
        data.setTypeClass(String.class);
        data.setType("String");
        data = this.validationDataRepository.save(data);
        Assert.assertNotNull(data.getId());
    }

    @Test
    public void test_order_2_findTest() {
        List<ValidationData> datas = this.validationDataRepository.findByParamTypeAndMethodAndUrlAndName(ParamType.BODY, "POST", "/order/url", "order");
        Assert.assertNotNull(datas.get(0));
    }
}
