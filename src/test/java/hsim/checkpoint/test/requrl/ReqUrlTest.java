package hsim.checkpoint.test.requrl;

import hsim.checkpoint.core.domain.ReqUrl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpMethod;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class ReqUrlTest {


    @Test
    public void test_ReqUrlReformat1(){
        String url = "/api/order/filter/";
        ReqUrl reqUrl = new ReqUrl(HttpMethod.GET.name(), url);

        Assert.assertEquals(reqUrl.getUrl(), "/api/order/filter" );
    }

    @Test
    public void test_ReqUrlReformat2(){
        String url = "/api/order/filter///";
        ReqUrl reqUrl = new ReqUrl(HttpMethod.GET.name(), url);

        Assert.assertEquals(reqUrl.getUrl(), "/api/order/filter" );
    }


}
