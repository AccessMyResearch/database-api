package com.amr.api.service;

import com.amr.api.dao.DAO;
import com.amr.api.model.GetAuthorsAPIResponse;
import com.amr.api.model.GetPublicationsAPIResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

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
                .thenReturn(allPublicationList);
        when(dao.getPublicationsByAuthor(validAuthorName))
                .thenReturn(allPublicationList);
        when(dao.getPublicationsByAuthor(missingAuthorName))
                .thenReturn(Collections.EMPTY_LIST);
        when(dao.getPublicationsRecent())
                .thenReturn(allPublicationList);
        when(dao.getPublicationsByYear(validStartYear, validEndYear))
                .thenReturn(dateRangePublicationList);
        when(dao.getAuthors())
                .thenReturn(validAuthorList);
    }

    @Test
    public void getPublications_hasPublicationValues() {
        //when
        GetPublicationsAPIResponse getPublicationsAPIResponse = service.getPublications();

        //then
        assertThat(getPublicationsAPIResponse).hasPublicationValues(Collections.singleton(validPublicationValues));
    }

    @Test
    public void getPublicationsByAuthor_whenValidAuthorName_thenHasValidPublicationValues() {
        //when
        GetPublicationsAPIResponse getPublicationsAPIResponse = service.getPublicationsByAuthor(validAuthorName);

        //then
        assertThat(getPublicationsAPIResponse).hasPublicationValues(Collections.singleton(validPublicationValues));
    }

    @Test
    public void getPublicationsByAuthor_whenMissingAuthorName_thenHasNoPublicationValues() {
        //when
        GetPublicationsAPIResponse getPublicationsAPIResponse = service.getPublicationsByAuthor(missingAuthorName);

        //then
        assertThat(getPublicationsAPIResponse).hasNoPublicationValues();
    }

    @Test
    public void getPublicationsRecent_hasPublicationValues() {
        //when
        GetPublicationsAPIResponse getPublicationsAPIResponse = service.getPublicationsRecent();

        //then
        assertThat(getPublicationsAPIResponse).hasPublicationValues(Collections.singleton(validPublicationValues));
    }

    @Test
    public void getPublicationsByYear_whenValidDates_thenHasValidPublicationValues() {
        //when
        GetPublicationsAPIResponse getPublicationsAPIResponse = service.getPublicationsByYear(validStartYear, validEndYear);

        //then
        assertThat(getPublicationsAPIResponse).hasPublicationValues(Collections.singleton(validPublicationValues));
    }

    @Test
    public void getPublicationsByYear_whenValidDates_thenDoesNotHaveBeforeRangePublicationValues() {
        //when
        GetPublicationsAPIResponse getPublicationsAPIResponse = service.getPublicationsByYear(validStartYear, validEndYear);

        //then
        assertThat(getPublicationsAPIResponse).doesNotHavePublicationValues(Collections.singleton(beforeDateRangePublicationValues));
    }

    @Test
    public void getPublicationsByYear_whenValidDates_thenDoesNotHaveAfterRangePublicationValues() {
        //when
        GetPublicationsAPIResponse getPublicationsAPIResponse = service.getPublicationsByYear(validStartYear, validEndYear);

        //then
        assertThat(getPublicationsAPIResponse).doesNotHavePublicationValues(Collections.singleton(afterDateRangePublicationValues));
    }

    @Test
    public void getPublicationsByYear_whenValidDates_thenHasEndOfDateRangePublicationValues() {
        //when
        GetPublicationsAPIResponse getPublicationsAPIResponse = service.getPublicationsByYear(validStartYear, validEndYear);

        //then
        assertThat(getPublicationsAPIResponse).hasPublicationValues(Collections.singleton(endOfDateRangePublicationValues));
    }

    @Test
    public void getAuthors_hasValidAuthorValues() {
        //when
        GetAuthorsAPIResponse getAuthorsAPIResponse = service.getAuthors();

        //then
        assertThat(getAuthorsAPIResponse).hasAuthorValues(Collections.singleton(validAuthorValues));
    }
}