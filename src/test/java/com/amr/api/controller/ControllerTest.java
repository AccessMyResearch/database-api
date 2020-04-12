package com.amr.api.controller;

import com.amr.api.service.Service;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.amr.api.asserts.TestAssertions.assertThat;
import static org.mockito.Mockito.when;
import static com.amr.api.TestConstants.*;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

    @Mock
    private Service service;

    @InjectMocks
    private Controller controller;

    @Before
    public void setUp() {
        when(service.getPublications())
                .thenReturn(getPublicationsAPIResponse);
        when(service.getPublicationsByAuthor(validAuthorName))
                .thenReturn(getPublicationsAPIResponse);
        when(service.getPublicationsByAuthor(missingAuthorName))
                .thenReturn(null);
        when(service.getPublicationsRecent())
                .thenReturn(getPublicationsAPIResponse);
        when(service.getPublicationsByYear(validStartYear, validEndYear))
                .thenReturn(getPublicationsAPIResponse);
        when(service.getAuthors())
                .thenReturn(getAuthorsAPIResponse);
        when(service.getUsers())
                .thenReturn(getUsersAPIResponse);
    }

    @Test
    public void getPublications_hasHttpStatusOK() {
        //when
        ResponseEntity response = controller.getPublications();

        //then
        assertThat(response).hasStatusCode(HttpStatus.OK);
    }

    @Test
    public void getPublications_hasResponseBodyNotNull() {
        //when
        ResponseEntity response = controller.getPublications();

        //then
        assertThat(response).hasBody(getPublicationsAPIResponse);
    }

    @Test
    public void getPublicationsByAuthor_whenValidAuthorName_thenHttpsStatusOK() {
        //when
        ResponseEntity response = controller.getPublicationsByAuthor(validAuthorName);

        //then
        assertThat(response).hasStatusCode(HttpStatus.OK);
    }

    @Test
    public void getPublicationsByAuthor_whenValidAuthorName_thenResponseBodyNotNull() {
        //when
        ResponseEntity response = controller.getPublicationsByAuthor(validAuthorName);

        //then
        assertThat(response).hasBody(getPublicationsAPIResponse);
    }

    @Test
    public void getPublicationsByAuthor_whenMissingAuthorName_thenHttpsStatusNotFound() {
        //when
        ResponseEntity response = controller.getPublicationsByAuthor(missingAuthorName);

        //then
        assertThat(response).hasStatusCode(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getPublicationsByAuthor_whenMissingAuthorName_thenResponseBodyNull() {
        //when
        ResponseEntity response = controller.getPublicationsByAuthor(missingAuthorName);

        //then
        assertThat(response).doesNotHaveBody();
    }

    @Test
    public void getPublicationsByAuthor_whenNullAuthorName_thenHttpsStatusBadRequest() {
        //when
        ResponseEntity response = controller.getPublicationsByAuthor(null);

        //then
        assertThat(response).hasStatusCode(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getPublicationsByAuthor_whenNullAuthorName_thenResponseBodyNull() {
        //when
        ResponseEntity response = controller.getPublicationsByAuthor(null);

        //then
        assertThat(response).doesNotHaveBody();
    }

    @Test
    public void getPublicationsRecent_hasHttpStatusOK() {
        //when
        ResponseEntity response = controller.getPublicationsRecent();

        //then
        assertThat(response).hasStatusCode(HttpStatus.OK);
    }

    @Test
    public void getPublicationsRecent_hasResponseBodyNotNull() {
        //when
        ResponseEntity response = controller.getPublicationsRecent();

        //then
        assertThat(response).hasBody(getPublicationsAPIResponse);
    }

    @Test
    public void getPublicationsByYear_whenValidYears_thenHttpsStatusOK() {
        //when
        ResponseEntity response = controller.getPublicationsByYear(validStartYear, validEndYear);

        //then
        assertThat(response).hasStatusCode(HttpStatus.OK);
    }

    @Test
    public void getPublicationsByYear_whenValidYears_thenResponseBodyNotNull() {
        //when
        ResponseEntity response = controller.getPublicationsByYear(validStartYear, validEndYear);

        //then
        assertThat(response).hasBody(getPublicationsAPIResponse);
    }

    @Test
    public void getPublicationsByAuthor_whenNullStartYear_thenHttpsStatusBadRequest() {
        //when
        ResponseEntity response = controller.getPublicationsByYear(null, validEndYear);

        //then
        assertThat(response).hasStatusCode(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getPublicationsByAuthor_whenNullStartYear_thenResponseBodyNull() {
        //when
        ResponseEntity response = controller.getPublicationsByYear(null, validEndYear);

        //then
        assertThat(response).doesNotHaveBody();
    }

    @Test
    public void getPublicationsByAuthor_whenNullEndYear_thenHttpsStatusBadRequest() {
        //when
        ResponseEntity response = controller.getPublicationsByYear(validStartYear, null);

        //then
        assertThat(response).hasStatusCode(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getPublicationsByAuthor_whenNullEndYear_thenResponseBodyNull() {
        //when
        ResponseEntity response = controller.getPublicationsByYear(validStartYear, null);

        //then
        assertThat(response).doesNotHaveBody();
    }

    @Test
    public void getPublicationsByAuthor_whenInvalidYears_thenHttpsStatusBadRequest() {
        //when
        ResponseEntity response = controller.getPublicationsByYear(validEndYear, validStartYear);

        //then
        assertThat(response).hasStatusCode(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getPublicationsByAuthor_whenInvalidYears_thenResponseBodyNull() {
        //when
        ResponseEntity response = controller.getPublicationsByYear(validEndYear, validStartYear);

        //then
        assertThat(response).doesNotHaveBody();
    }

    @Test
    public void getAuthors_hasHttpStatusOK() {
        //when
        ResponseEntity response = controller.getAuthors();

        //then
        assertThat(response).hasStatusCode(HttpStatus.OK);
    }

    @Test
    public void getAuthors_hasResponseBodyNotNull() {
        //when
        ResponseEntity response = controller.getAuthors();

        //then
        assertThat(response).hasBody(getAuthorsAPIResponse);
    }

    @Test
    public void getUsers_hasHttpStatusOK() {
        //when
        ResponseEntity response = controller.getUsers();

        //then
        assertThat(response).hasStatusCode(HttpStatus.OK);
    }

    @Test
    public void getUsers_hasResponseBodyNotNull() {
        //when
        ResponseEntity response = controller.getUsers();

        //then
        assertThat(response).hasBody(getUsersAPIResponse);
    }
}