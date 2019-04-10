package com.sujya.prj;

import com.sujya.prj.auth.common.JwtAuthenticationToken;
import com.sujya.prj.auth.common.UserContext;
import com.sujya.prj.controller.PrjController;
import com.sujya.prj.entity.LocationVO;
import com.sujya.prj.service.LocationService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.*;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@RestClientTest(PrjController.class)
public class PrjControllerTest {

    @InjectMocks
    private PrjController controller;

    @Mock
    private LocationService service;

    private JwtAuthenticationToken token;

    private UserContext userContext;

    private MockMvc mvc;

    private static String username = "sj";
    private static String password = "sj";


    @Before
    public void beforeTest() throws Exception{
        // 1. login and get jwt token

        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();

        userContext = UserContext.create(username, null);
        token = new JwtAuthenticationToken(userContext, null);

    }


    @Test
    public void testUploadFile() throws Exception {
        FileInputStream uploadStream = new FileInputStream(new File("assetsTest/test_sample.csv"));
        InputStreamReader isr = new InputStreamReader(uploadStream, "EUC-KR");
        StringBuilder builder = new StringBuilder();
        char[] charBuffer = new char[8*1024];
        int numCharRead;
        while((numCharRead = isr.read(charBuffer, 0, charBuffer.length)) != -1){
            builder.append(charBuffer, 0, numCharRead);
        }
        InputStream is = new ByteArrayInputStream(builder.toString().getBytes("EUC-KR"));
        MockMultipartFile file = new MockMultipartFile("file", "test_sample.csv", MediaType.APPLICATION_JSON_VALUE, is);

        assert uploadStream != null;

        this.mvc.perform(MockMvcRequestBuilders.fileUpload("/uploadFile")
                .file(file)
                .principal(token))
                .andExpect(status().isOk());

        verify(service, times(1)).saveFile(any());
    }

    @Test
    public void testAll() throws Exception {


//        1,강릉시,강릉시 소재 중소기업으로서 강릉시장이 추천한 자,운전,추천금액 이내,3%,강릉시,강릉지점,강릉시 소재 영업점
        final LocationVO locVO = new LocationVO();
        locVO.setLocId("1");
        locVO.setRegion("강릉시");
        locVO.setTarget("강릉시 소재 중소기업으로서 강릉시장이 추천한 자");
        locVO.setUsage("운전");
        locVO.setLimit("추천금액 이내");
        locVO.setRate("3%");
        locVO.setInstitute("강릉시");
        locVO.setMgmt("강릉지점");
        locVO.setReception("강릉시 소재 영업점");

        List<LocationVO> expectedResult = Lists.newArrayList(locVO);

        when(service.allLocation()).thenReturn(expectedResult);

        mvc.perform(get("/all")
                .principal(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print());

        verify(service, times(1)).allLocation();

    }

    @Test
    public void testSearchLocation() throws Exception {
        String body = "{ \"region\" : \"강릉시\" }";
//        final LocationVO locInputVO = new LocationVO();
//        locInputVO.setRegion("강릉시");

        final LocationVO locVO = new LocationVO();
        locVO.setLocId("1");
        locVO.setRegion("강릉시");
        locVO.setTarget("강릉시 소재 중소기업으로서 강릉시장이 추천한 자");
        locVO.setUsage("운전");
        locVO.setLimit("추천금액 이내");
        locVO.setRate("3%");
        locVO.setInstitute("강릉시");
        locVO.setMgmt("강릉지점");
        locVO.setReception("강릉시 소재 영업점");

        List<LocationVO> expectedResult = Lists.newArrayList(locVO);

        when(service.searchLocation(anyString())).thenReturn(expectedResult);

        mvc.perform(MockMvcRequestBuilders.post("/searchLocation")
                .principal(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        verify(service, times(1)).searchLocation(anyString());

    }

    @Test
    public void testUpdateLocation() throws Exception {
        String body = "{ \"region\" : \"강릉시\", \"locId\" : \"1\", \"limit\": \"7억원 이상\" }";

//        final LocationVO locInputVO = new LocationVO();
//        locInputVO.setLocId("1");
//        locInputVO.setRegion("강릉시");
//        locInputVO.setLimit("7억원 이상");

        final LocationVO locVO = new LocationVO();
        locVO.setLocId("1");
        locVO.setRegion("강릉시");
        locVO.setTarget("강릉시 소재 중소기업으로서 강릉시장이 추천한 자");
        locVO.setUsage("운전");
        locVO.setLimit("추천금액 이내");
        locVO.setRate("3%");
        locVO.setInstitute("강릉시");
        locVO.setMgmt("강릉지점");
        locVO.setReception("강릉시 소재 영업점");

        when(service.updateLocation(any())).thenReturn(locVO);

        mvc.perform(MockMvcRequestBuilders.post("/updateLocation")
                .principal(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        verify(service, times(1)).updateLocation(any());

    }

    @Test
    public void testLimitK() throws Exception {


        List<String> expectedResult = Lists.newArrayList("강릉시", "원주시", "대구광역시");


        when(service.limitK(anyInt())).thenReturn(expectedResult);

        mvc.perform(MockMvcRequestBuilders.get("/limitTop/{k}", 3)
                .principal(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        verify(service, times(1)).limitK(anyInt());

    }

    @Test
    public void testSmallestRate() throws Exception {
        final LocationVO locVO = new LocationVO();
        locVO.setLocId("1");
        locVO.setRegion("강릉시");
        locVO.setTarget("강릉시 소재 중소기업으로서 강릉시장이 추천한 자");
        locVO.setUsage("운전");
        locVO.setLimit("추천금액 이내");
        locVO.setRate("3%");
        locVO.setInstitute("강릉시");
        locVO.setMgmt("강릉지점");
        locVO.setReception("강릉시 소재 영업점");

        when(service.smallestRate()).thenReturn(locVO);

        mvc.perform(MockMvcRequestBuilders.get("/smallestRate")
                .principal(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        verify(service, times(1)).smallestRate();

    }





}
