package web;

import com.ccx.demo.business.example.entity.TabDemoList;
import com.ccx.demo.business.example.enums.DemoStatus;
import com.ccx.demo.config.WebMvcConfig;
import com.ccx.demo.config.init.AppConfig;
import com.support.mvc.entity.base.Param;
import com.utils.util.Dates;
import com.utils.util.Range;
import com.utils.util.RangeInt;
import com.utils.util.Util;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static com.utils.util.Dates.Pattern.yyyy_MM_dd_HH_mm_ss;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


/**
 * @author 谢长春 on 2017/10/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebMvcConfig.class})
@WebAppConfiguration
@Slf4j
public class DemoListControllerTest implements ITest {
    @Getter
    private String urlPrefix = "/demo-list/{version}";
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
    public void save() {
        RangeInt.of(1, 1).forEach(v -> {
            Tester.builder()
                    .clazz(this.getClass())
                    .methodName(getMethodName())
                    .mockMvc(mockMvc)
                    .url(urlPrefix)
                    .version(1)
                    .build()
                    .post(Param.of(BeanMap.create(
                            TabDemoList.builder()
                                    .name("JX")
                                    .content(RandomStringUtils.randomAlphabetic(15))
                                    .status(DemoStatus.WATING)
                                    .amount((double) Util.randomMax(9999))
                                    .build()
                            ))
                    )
            ;
        });
    }

    @Test
    public void update() {
        Tester.builder()
                .clazz(this.getClass())
                .methodName(getMethodName())
                .mockMvc(mockMvc)
                .url(urlPrefix)
                .version(1)
                .build()
                .updateById(1,
                        Param.of(BeanMap.create(
                                TabDemoList.builder()
                                        .uid("7d2df4ec9b5d43ca8fc628cb186bea7c")
                                        .name(RandomStringUtils.randomAlphabetic(10))
                                        .content(RandomStringUtils.randomAlphabetic(15))
                                        .status(DemoStatus.RUNNING)
                                        .amount((double) Util.randomMax(9999))
                                        .modifyTime(Dates.of("2018-12-18 10:45:10", yyyy_MM_dd_HH_mm_ss).timestamp())
                                        .build()
                        ))
                )
        ;
    }

    @Test
    public void deleteById() {
        Tester.builder()
                .clazz(this.getClass())
                .methodName(getMethodName())
                .mockMvc(mockMvc)
                .url(urlPrefix)
                .version(1)
                .build()
                .deleteById(19)
        ;
    }

    @Test
    public void deleteByUid() {
        Tester.builder()
                .clazz(this.getClass())
                .methodName(getMethodName())
                .mockMvc(mockMvc)
                .url(urlPrefix)
                .version(1)
                .build()
                .deleteByUid(20, "958c9c45053341288e4cfcc7dfa6e555")
        ;
    }

    @Test
    public void markDeleteById() {
        Tester.builder()
                .clazz(this.getClass())
                .methodName(getMethodName())
                .mockMvc(mockMvc)
                .url(urlPrefix)
                .version(1)
                .build()
                .markDeleteById(11)
        ;
    }

    @Test
    public void markDeleteByUid() {
        Tester.builder()
                .clazz(this.getClass())
                .methodName(getMethodName())
                .mockMvc(mockMvc)
                .url(urlPrefix)
                .version(1)
                .build()
                .markDeleteByUid(12, "8a50ffbaf1b94f40b13e8e4263e8bf9b")
        ;
    }

    @Test
    public void markDelete() {
        Tester.builder()
                .clazz(this.getClass())
                .methodName(getMethodName())
                .mockMvc(mockMvc)
                .url(urlPrefix)
                .version(1)
                .build()
                .markDelete(Param.of(
                        Arrays.asList(
                                TabDemoList.builder().id(13L).uid("8073e7d668564de189bdac8465af99f0").build(),
                                TabDemoList.builder().id(14L).uid("bb2a507c8db14107810cf315fb8f166cq").build()
                        )
                ))
        ;
    }

//    @Test
//    public void findById() {
//        Tester.builder()
//                .clazz(this.getClass())
//                .methodName(getMethodName())
//                .mockMvc(mockMvc)
//                .url(urlPrefix)
//                .version(1)
//                .build()
//                .findById(2L)
//        ;
//    }
//
//    @Test
//    public void findByUid() {
//        Tester.builder()
//                .clazz(this.getClass())
//                .methodName(getMethodName())
//                .mockMvc(mockMvc)
//                .url(urlPrefix)
//                .version(1)
//                .build()
//                .findByUid(3L, "0a485c2fa89b4b029840b32394773f40")
//        ;
//    }

    @Test
    public void findByIdTimestamp() {
        Tester.builder()
                .clazz(this.getClass())
                .methodName(getMethodName())
                .mockMvc(mockMvc)
                .url(urlPrefix)
                .version(1)
                .build()
                .findByIdTimestamp(2L, yyyy_MM_dd_HH_mm_ss.parse("2018-12-12 20:32:10").getTimeMillis())
        ;
    }

    @Test
    public void findByUidTimestamp() {
        Tester.builder()
                .clazz(this.getClass())
                .methodName(getMethodName())
                .mockMvc(mockMvc)
                .url(urlPrefix)
                .version(1)
                .build()
                .findByUidTimestamp(3L, "793b0f03fc8249a38793ff696a7afbd2", Dates.now().formatDateTime())
        ;
    }

    @Test
    public void search() {
        Tester.builder()
                .clazz(this.getClass())
                .methodName(getMethodName())
                .mockMvc(mockMvc)
                .url(urlPrefix)
                .version(1)
                .build()
                .get(
                        Param.of(BeanMap.create(
                                TabDemoList.builder()
                                        .createTimeRange(Dates.Range.today())
                                        .build()
                        ))
                )
        ;
    }

    @Test
    public void page() {
        Tester.builder()
                .clazz(this.getClass())
                .methodName(getMethodName())
                .mockMvc(mockMvc)
                .url(urlPrefix)
                .version(1)
                .build()
                .page(1, 10,
                        Param.of(BeanMap.create(
                                TabDemoList.builder()
                                        .amountRange(Range.of(0d, 2000d))
                                        .build()
                        ))
                )
        ;
    }
}
