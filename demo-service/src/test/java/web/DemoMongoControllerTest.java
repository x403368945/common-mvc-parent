package web;

import com.ccx.demo.business.example.entity.DemoMongo;
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
import java.util.stream.Stream;

import static com.utils.util.Dates.Pattern.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


/**
 * @author 谢长春 on 2017/10/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebMvcConfig.class})
@WebAppConfiguration
@Slf4j
public class DemoMongoControllerTest implements ITest {
    @Getter
    private String urlPrefix = "/demo-mongo/{version}";
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
        RangeInt.of(1, 20).forEach(v -> Tester.builder()
                .clazz(this.getClass())
                .methodName(getMethodName())
                .mockMvc(mockMvc)
                .url(urlPrefix)
                .version(1)
                .build()
                .post(Param.of(BeanMap.create(
                        DemoMongo.builder()
                                .name(RandomStringUtils.randomAlphabetic(10))
                                .phone("187".concat(RandomStringUtils.randomNumeric(8)))
                                .status(DemoStatus.WATING)
                                .age((short) Util.randomMax(100))
                                .build()
                        ))
                )
        );
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
                .updateById("0899cdda57ca44e58e3505cbb6f60cb1",
                        Param.of(BeanMap.create(
                                DemoMongo.builder()
                                        .name(RandomStringUtils.randomAlphabetic(10))
                                        .status(DemoStatus.RUNNING)
                                        .age((short) Util.randomMax(100))
                                        .modifyTime(yyyy_MM_dd_HH_mm_ss_SSS.parse("2019-01-06 14:02:41.914").timestamp())
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
                .deleteById("bacc3d1b0b3841988db3a105af8be6e0")
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
                .markDeleteById("a21a356bf3bb4e1189e6dcfdefa938b1")
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
                                "80246fb8d0eb4a5da808e9e4ef9ac213",
                                "b892fab7d0a84c02879697e8a262c880"
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

    @Test
    public void findByIdTimestamp() {
        Stream.of(
                "a21a356bf3bb4e1189e6dcfdefa938b1",
                "80246fb8d0eb4a5da808e9e4ef9ac213",
                "b892fab7d0a84c02879697e8a262c880"
        ).forEach(id -> Tester.builder()
                        .clazz(this.getClass())
                        .methodName(getMethodName())
                        .mockMvc(mockMvc)
                        .url(urlPrefix)
                        .version(1)
                        .build()
                        .findByIdTimestamp(id, yyyy_MM_dd_HH_mm_ss.parse("2019-01-04 20:32:10").getTimeMillis())
        );
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
                                DemoMongo.builder()
                                        .createTimeRange(Dates.Range.builder()
                                                .begin(yyyy_MM_dd.parse("2019-01-04").timestamp())
                                                .end(yyyy_MM_dd.parse("2019-01-04").timestamp())
                                                .build())
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
                                DemoMongo.builder()
                                        .ageRange(Range.of((short) 1, (short) 20))
                                        .build()
                        ))
                )
        ;
    }
}
