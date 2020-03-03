package com.amr.api.service;

import com.amr.api.dao.PublicationRepository;
import com.amr.api.model.PublicationResponsePublication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.amr.api.model.Publication;
import com.amr.api.model.PublicationAPIResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceImpl implements com.amr.api.service.Service {
    private final PublicationRepository publicationRepository;

    @Override
    public PublicationAPIResponse getPublications() {
        List<Publication> publications = publicationRepository.findAll();
        List<PublicationResponsePublication> publicationResponsePublications = publications.stream().map(publication -> new PublicationResponsePublication(publication.getPublicationID(), publication.getTitle())).collect(Collectors.toList());
        return new PublicationAPIResponse(publicationResponsePublications);
    }
}
