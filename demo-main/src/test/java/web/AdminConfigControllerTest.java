package web;
/*

import com.ccx.demo.config.WebMvcConfig;
import com.ccx.demo.config.init.AppConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


*/
/**
 * @author 谢长春 on 2017/10/13.
 *//*

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebMvcConfig.class})
@WebAppConfiguration
@Slf4j
public class AdminConfigControllerTest implements ITest {
    @Getter
    private String urlPrefix = "/admin-config/{version}";
    @Getter
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        AppConfig.setJunit(true);
    }

    @Test
    public void config() {
        Tester.builder()
                .clazz(this.getClass())
                .methodName(getMethodName())
                .mockMvc(mockMvc)
                .url(urlPrefix)
                .version(1)
                .build()
                .get(new Param())
        ;
    }
}
*/
