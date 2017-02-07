package com.khai.xmlnew.standard;

import com.khai.db.model.CitationModel;
import com.khai.db.model.Person;
import com.khai.utils.TextUtils;
import com.khai.xmlnew.standard.base.BaseStandard;
import com.khai.xmlnew.standard.base.Constants;
import com.khai.xmlnew.standard.model.Authors;
import com.khai.xmlnew.standard.model.CitationParts;
import com.khai.xmlnew.standard.model.Field;
import com.khai.xmlnew.standard.model.MultipartSeparator;
import org.dom4j.Node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Dstu712006 extends BaseStandard {

    private CitationParts citationParts;

    public Dstu712006(String standardPath) {
        super(standardPath);
        citationParts = new CitationParts();
    }

    @Override
    public String getCitation(CitationModel citationModel, String type) {
        //todo change to using of 'type' variable when it will be properly made
        final List<String> parts = citations.get("book-author");
        final StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            addCitationPart("book-author", part, citationModel, builder);
        }
        return builder.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void fillCitationParts() {
        final List<Node> firstAuthorNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATION_PARTS_FIRST_AUTHOR);
        final List<Node> titleNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATION_PARTS_TITLE);
        final List<Node> editionTypeNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATION_PARTS_EDITION_TYPE);
        final List<Node> typeNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATION_PARTS_TYPE);
        final List<Node> additionalInfoNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATION_PARTS_ADDITIONAL_INFO);
        final List<Node> editorsNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATION_PARTS_EDITORS);
        final List<Node> authorsAfterNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATION_PARTS_AUTHORS_AFTER);
        final List<Node> directorsNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATION_PARTS_DIRECTORS);
        final List<Node> executorsNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATION_PARTS_EXECUTORS);
        final List<Node> publisherCityNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATION_PARTS_PUBLISHER_CITY);
        final List<Node> publisherNameNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATION_PARTS_PUBLISHER_NAME);
        final List<Node> publisherNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATION_PARTS_PUBLISHER);
        final List<Node> yearDateNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATION_PARTS_YEAR_DATE);
        final List<Node> volumeNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATION_PARTS_VOLUME);
        final List<Node> noNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATION_PARTS_NO);
        final List<Node> pagesNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATION_PARTS_PAGES);
        final List<Node> officialDateNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATION_PARTS_OFFICIAL_DATE);
        citationParts.setFirstAuthors(getAuthors(firstAuthorNodes));
        citationParts.setTitles(getField(titleNodes));
        citationParts.setEditionTypes(getField(editionTypeNodes));
        citationParts.setTypes(getField(typeNodes));
        citationParts.setAdditionalInfos(getField(additionalInfoNodes));
        citationParts.setEditors(getAuthors(editorsNodes));
        citationParts.setAuthorsAfter(getAuthors(authorsAfterNodes));
        citationParts.setDirectors(getAuthors(directorsNodes));
        citationParts.setExecutors(getAuthors(executorsNodes));
        citationParts.setPublisherCities(getField(publisherCityNodes));
        citationParts.setPublisherNames(getField(publisherNameNodes));
        citationParts.setPublishers(getField(publisherNodes));
        citationParts.setYearDates(getField(yearDateNodes));
        citationParts.setVolume(getField(volumeNodes));
        citationParts.setNo(getField(noNodes));
        citationParts.setPages(getField(pagesNodes));
        citationParts.setOfficialDates(getField(officialDateNodes));
    }

    private Map<String, Field> getField(List<Node> fieldsNodes) {
        final Map<String, Field> fields = new HashMap<>();
        final StringBuilder builder = new StringBuilder();
        String separatorBeforeName;
        String separatorAfterName;
        for (Node node : fieldsNodes) {
            final Field field = new Field();
            final String type = node.valueOf(Constants.XmlAttribute.TYPE);
            MultipartSeparator multipartSeparator;
            builder.setLength(0);
            separatorBeforeName = getMultipartSeparatorName(node, Constants.XmlNode.MULTIPART_SEPARATOR_BEFORE);
            separatorAfterName = getMultipartSeparatorName(node, Constants.XmlNode.MULTIPART_SEPARATOR_AFTER);
            field.setType(type);
            multipartSeparator = multipartSeparatorsBefore.get(separatorBeforeName);
            builder.append(multipartSeparator != null
                    ? multipartSeparator.getValue()
                    : "");
            builder.append("%s");
            multipartSeparator = multipartSeparatorsAfter.get(separatorAfterName);
            builder.append(multipartSeparator != null
                    ? multipartSeparator.getValue()
                    : "");
            field.setFormattedValue(builder.toString());
            fields.put(type, field);
        }
        return fields;
    }

    private Map<String, Authors> getAuthors(List<Node> authorsNodes) {
        //todo resolve issue with 'Authors' name -> review xml structure of authors (see TODO there)
        //todo implement multipart-separator-after after all authors
        final Map<String, Authors> authorsMap = new HashMap<>();
        String separatorSurname;
        String separatorName1;
        String separatorName2;
        for (Node node : authorsNodes) {
            final Authors authors = new Authors();
            final String condition = node.valueOf(Constants.XmlAttribute.CONDITION);
            final String type = node.valueOf(Constants.XmlAttribute.TYPE);
            MultipartSeparator multipartSeparator;
            separatorSurname = getMultipartSeparatorName(node.selectSingleNode(Constants.XmlNode.SURNAME),
                    Constants.XmlNode.MULTIPART_SEPARATOR_AFTER);
            separatorName1 = getMultipartSeparatorName(node.selectSingleNode(Constants.XmlNode.NAME1),
                    Constants.XmlNode.MULTIPART_SEPARATOR_AFTER);
            separatorName2 = getMultipartSeparatorName(node.selectSingleNode(Constants.XmlNode.NAME2),
                    Constants.XmlNode.MULTIPART_SEPARATOR_AFTER);
            authors.setType(type);
            authors.setCondition(condition);
            multipartSeparator = multipartSeparatorsAfter.get(separatorSurname);
            authors.setFormattedSurname(multipartSeparator != null
                    ? "%s" + multipartSeparator.getValue()
                    : "%s");
            multipartSeparator = multipartSeparatorsAfter.get(separatorName1);
            authors.setFormattedName1(multipartSeparator != null
                    ? "%s" + multipartSeparator.getValue()
                    : "%s");
            multipartSeparator = multipartSeparatorsAfter.get(separatorName2);
            authors.setFormattedName2(multipartSeparator != null
                    ? "%s" + multipartSeparator.getValue()
                    : "%s");

            authorsMap.put(condition + type, authors);
        }
        return authorsMap;
    }

    private String getMultipartSeparatorName(Node parentNode, String multipartSeparatorNodeName) {
        final String name;
        final String type;
        final Node multipartSeparatorNode = parentNode.selectSingleNode(multipartSeparatorNodeName);
        if (multipartSeparatorNode != null) {
            name = multipartSeparatorNode.valueOf(Constants.XmlAttribute.NAME);
            type = multipartSeparatorNode.valueOf(Constants.XmlAttribute.TYPE);
            return name + type;
        }
        return "";
    }

    private void addCitationPart(String type, String citationPart,
                                 CitationModel model, StringBuilder builder) {
        Authors authors;
        Field value;
        switch (citationPart) {
            case "first-author":
                if (model.getAuthors() == null || model.getAuthors().isEmpty()) return;
                if (model.getAuthors().size() > 4) return;
                final Person firstAuthor;
                authors = citationParts.getFirstAuthors().get(type);
                if (authors == null) {
                    authors = citationParts.getFirstAuthors().get("default");
                }
                firstAuthor = model.getAuthors().iterator().next();
                builder.append(String.format(authors.getFormattedSurname(), firstAuthor.getSurname()))
                        .append(String.format(authors.getFormattedName1(), firstAuthor.getName1()))
                        .append(String.format(authors.getFormattedName2(), firstAuthor.getName2()));
                break;
            case "title":
                if (TextUtils.isEmpty(model.getTitle())) return;
                value = citationParts.getTitles().get(type);
                if (value == null) {
                    value = citationParts.getTitles().get("default");
                }
                builder.append(String.format(value.getFormattedValue(), model.getTitle()));
                break;
            case "type":
                if (TextUtils.isEmpty(model.getType())) return;
                value = citationParts.getTypes().get(type);
                if (value == null) {
                    value = citationParts.getTypes().get("default");
                }
                builder.append(String.format(value.getFormattedValue(), model.getType()));
                break;
            case "authors-after":
                if (model.getAuthors() == null || model.getAuthors().isEmpty()) return;
                final String authorsType = model.getAuthors().size() > 4
                        ? "lq4" + type
                        : "qt4" + type;
                authors = citationParts.getAuthorsAfter().get(authorsType);
                if (authors == null) {
                    authors = citationParts.getAuthorsAfter().get("lq4default");
                }
                final Iterator<Person> authorsIterator = model.getAuthors().iterator();
                do {
                    final Person person = authorsIterator.next();
                    builder.append(String.format(authors.getFormattedName1(), person.getName1()))
                            .append(String.format(authors.getFormattedName2(), person.getName2()))
                            .append(String.format(authors.getFormattedSurname(), person.getSurname()));

                } while (authorsIterator.hasNext());
                break;
            case "publisher":
                if (TextUtils.isEmpty(model.getPublisher())) return;
                value = citationParts.getPublishers().get(type);
                if (value == null) {
                    value = citationParts.getPublishers().get("default");
                }
                builder.append(String.format(value.getFormattedValue(), model.getPublisher()));
                break;
            case "editors":

                break;
            case "publisher-city":
                if (TextUtils.isEmpty(model.getPublisherInfo())) return;
                value = citationParts.getPublisherCities().get(type);
                if (value == null) {
                    value = citationParts.getPublisherCities().get("default");
                }
                builder.append(String.format(value.getFormattedValue(), model.getPublisherInfo()));
                break;
            case "publisher-name":
                if (TextUtils.isEmpty(model.getPublisherInfo())) return;
                value = citationParts.getPublisherNames().get(type);
                if (value == null) {
                    value = citationParts.getPublisherNames().get("default");
                }
                builder.append(String.format(value.getFormattedValue(), model.getPublisherInfo()));
                break;
            case "year-date":
                if (TextUtils.isEmpty(model.getYear())) return;
                value = citationParts.getYearDates().get(type);
                if (value == null) {
                    value = citationParts.getYearDates().get("default");
                }
                builder.append(String.format(value.getFormattedValue(), model.getYear()));
                break;
            case "volume":
                if (TextUtils.isEmpty(model.getVolume())) return;
                value = citationParts.getVolume().get(type);
                if (value == null) {
                    value = citationParts.getVolume().get("default");
                }
                builder.append(String.format(value.getFormattedValue(), model.getVolume()));
                break;
            case "no":
                if (TextUtils.isEmpty(model.getNo())) return;
                value = citationParts.getNo().get(type);
                if (value == null) {
                    value = citationParts.getNo().get("default");
                }
                builder.append(String.format(value.getFormattedValue(), model.getNo()));
                break;
            case "pages":
                if (TextUtils.isEmpty(model.getPage())) return;
                value = citationParts.getPages().get(type);
                if (value == null) {
                    value = citationParts.getPages().get("default");
                }
                builder.append(String.format(value.getFormattedValue(), model.getPage()));
                break;
        }
    }

}