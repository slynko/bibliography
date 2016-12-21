package com.khai.db.service;

import com.khai.db.model.CitationModel;

import java.util.List;

/**
 * Bibliography crud service.
 */
public interface BibliographyService {
    /**
     * Find all citations.
     * @return all citations
     */
    List<CitationModel> findAll();

    /**
     * Save all bibliographies.
     * @param bibliographies to save
     */
    void saveAll(Iterable<CitationModel> bibliographies);

    /**
     * Add a bibliography.
     * @param model to add
     * @return added model
     */
    CitationModel add(CitationModel model);

    List<CitationModel> findByTitle(List<String> titles);
}