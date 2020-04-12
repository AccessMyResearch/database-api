package com.amr.api.service;

import com.amr.api.dao.DAO;
import com.amr.api.model.GetPublicationsAPIResponse;
import com.amr.api.model.PublicationValues;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static com.amr.api.TestConstants.*;
import static com.amr.api.asserts.TestAssertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTest {

    @Mock
    private DAO dao;

    @InjectMocks
    private ServiceImpl service;

    @Before
    public void setUp() {
        when(dao.getPublications())
                .thenReturn(validPublicationList);
    }

    @Test
    public void getPublications_hasPublicationValues() {
        //when
        GetPublicationsAPIResponse getPublicationsAPIResponse = service.getPublications();

        //then
        assertThat(getPublicationsAPIResponse).hasPublicationValues(Collections.singleton(validPublicationValues));
    }
}