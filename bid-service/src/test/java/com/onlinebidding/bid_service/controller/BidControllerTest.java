package com.onlinebidding.bid_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.onlinebidding.bid_service.dto.BidDto;
import com.onlinebidding.bid_service.service.BidService;

@SpringBootTest
class BidControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private BidService bidService;

    @MockitoBean
    private SimpMessagingTemplate messagingTemplate;

    private BidDto testBid;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        testBid = BidDto.builder()
                .id(1L)
                .auctionId(101L)
                .bidderId(5L)
                .bidderName("Alice")
                .amount(new BigDecimal("150.00"))
                .bidTime(LocalDateTime.of(2026, 7, 28, 21, 0, 0))
                .build();
    }

    // --- PLACE BID ---

    @Test
    void placeBid_success() throws Exception {
        when(bidService.placeBid(any(BidDto.class))).thenReturn(testBid);

        String jsonPayload = "{"
                + "\"auctionId\":101,"
                + "\"bidderId\":5,"
                + "\"bidderName\":\"Alice\","
                + "\"amount\":150.00"
                + "}";

        mockMvc.perform(post("/bids")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(150.00))
                .andExpect(jsonPath("$.bidderName").value("Alice"));

        verify(messagingTemplate).convertAndSend("/topic/auction/101", testBid);
    }

    @Test
    void placeBid_invalidBid_returns400() throws Exception {
        when(bidService.placeBid(any(BidDto.class)))
                .thenThrow(new IllegalArgumentException("Bid amount must be higher than current highest bid"));

        String jsonPayload = "{"
                + "\"auctionId\":101,"
                + "\"bidderId\":5,"
                + "\"bidderName\":\"Alice\","
                + "\"amount\":50.00"
                + "}";

        mockMvc.perform(post("/bids")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bid amount must be higher than current highest bid"));
    }

    // --- GET BIDS FOR AUCTION ---

    @Test
    void getBidsForAuction_success() throws Exception {
        when(bidService.getBidsForAuction(101L)).thenReturn(Arrays.asList(testBid));

        mockMvc.perform(get("/bids/auction/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].bidderName").value("Alice"));
    }

    // --- GET HIGHEST BID FOR AUCTION ---

    @Test
    void getHighestBidForAuction_found() throws Exception {
        when(bidService.getHighestBidForAuction(101L)).thenReturn(Optional.of(testBid));

        mockMvc.perform(get("/bids/auction/101/highest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(150.00));
    }

    @Test
    void getHighestBidForAuction_notFound_returns404() throws Exception {
        when(bidService.getHighestBidForAuction(101L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/bids/auction/101/highest"))
                .andExpect(status().isNotFound());
    }

    // --- GET BIDS BY USER ---

    @Test
    void getBidsByUser_success() throws Exception {
        when(bidService.getBidsByUser(5L)).thenReturn(Arrays.asList(testBid));

        mockMvc.perform(get("/bids/user/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].bidderId").value(5));
    }
}
