package com.khai.xml.standard.base;

import com.khai.model.xml.Authors;
import com.khai.model.xml.Field;
import com.khai.model.xml.MultipartSeparator;
import com.khai.model.xml.Separator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class BaseStandard implements StandardContract {

    protected Document xmlDocument;
    protected Map<String, Separator> separators;
    protected Map<String, MultipartSeparator> multipartSeparatorsBefore;
    protected Map<String, MultipartSeparator> multipartSeparatorsAfter;
    protected Map<String, List<String>> citations;

    public BaseStandard(String standardPath) {
        separators = new HashMap();
        multipartSeparatorsBefore = new HashMap();
        multipartSeparatorsAfter = new HashMap();
        citations = new HashMap<>();
        initStandard(standardPath);
    }

    @Override
    public void make() {
        fillSeparators();
        fillMultipartSeparators(Constants.XmlPathToNode.MULTIPART_SEPARATORS_BEFORE, multipartSeparatorsBefore);
        fillMultipartSeparators(Constants.XmlPathToNode.MULTIPART_SEPARATORS_AFTER, multipartSeparatorsAfter);
        fillCitationParts();
        fillCitations();
    }

    @Override
    public Map<String, String> getTypes() {
        final Map<String, String> types = new HashMap<>();
        final List<Node> typesNodes = xmlDocument.selectNodes(Constants.XmlPathToNode.TYPES);
        for (Node typeNode : typesNodes) {
            final String typeName = typeNode.valueOf(Constants.XmlAttribute.NAME);
            final String typeTitle = typeNode.valueOf(Constants.XmlAttribute.TITLE);
            types.put(typeName, typeTitle);
        }
        return types;
    }

    /**
     * Initiation of standard
     *
     * @param standardPath chosen path to standard for initiating
     */
    private void initStandard(String standardPath) {
        final URL path = getClass().getClassLoader().getResource(standardPath);
        if (path == null) {
            throw new IllegalArgumentException("Something is wrong with standard's file path");
        }
        final File xmlSource = new File(path.getFile());
        final SAXReader reader = new SAXReader();
        try {
            xmlDocument = reader.read(xmlSource);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a string value which is located as the value of single node
     *
     * @param parentNode parent node of single node where value is
     * @param nodeName   name of single node
     * @return string value of single node
     */
    private String getStringOfSingleNode(Node parentNode, String nodeName) {
        final Node singleNode = parentNode.selectSingleNode(nodeName);
        return singleNode != null ? singleNode.getStringValue() : null;
    }

    /**
     * Fills separators of standard
     */
    private void fillSeparators() {
        final List<Node> separators = xmlDocument.selectNodes(Constants.XmlPathToNode.SEPARATORS);
        for (Node node : separators) {
            final Separator separator = new Separator();
            final String name = node.valueOf(Constants.XmlAttribute.NAME);
            final String def = getStringOfSingleNode(node, Constants.XmlNode.DEFAULT);
            final String ru = getStringOfSingleNode(node, Constants.XmlNode.RU);
            final String ua = getStringOfSingleNode(node, Constants.XmlNode.UA);
            final String en = getStringOfSingleNode(node, Constants.XmlNode.EN);
            separator.setSymbolDef(def);
            separator.setSymbolRu(ru);
            separator.setSymbolUa(ua);
            separator.setSymbolEn(en);
            this.separators.put(name, separator);
        }
    }


    /**
     * Fills multipart separators of standard
     */
    private void fillMultipartSeparators(String type, Map<String, MultipartSeparator> multipartSeparators) {
        final List<Node> multipartSeparatorsNodes = xmlDocument.selectNodes(type);
        final StringBuilder builder = new StringBuilder();
        for (Node nodeMultipartSeparator : multipartSeparatorsNodes) {
            final List<Node> separators = nodeMultipartSeparator.selectNodes(Constants.XmlNode.SEPARATOR);
            builder.setLength(0);
            for (Node nodeSeparator : separators) {
                final String separatorName = nodeSeparator.valueOf(Constants.XmlAttribute.NAME);
                builder.append(this.separators.get(separatorName).getSymbolDef());
            }
            final MultipartSeparator multipartSeparator = new MultipartSeparator();
            multipartSeparator.setName(nodeMultipartSeparator.valueOf(Constants.XmlAttribute.NAME));
            multipartSeparator.setType(nodeMultipartSeparator.valueOf(Constants.XmlAttribute.TYPE));
            multipartSeparator.setValue(builder.toString());
            multipartSeparators.put(multipartSeparator.getName() + multipartSeparator.getType(),
                    multipartSeparator);
        }
    }


    /**
     * Fills available citation types of standard
     */
    private void fillCitations() {
        final List<Node> citations = xmlDocument.selectNodes(Constants.XmlPathToNode.CITATIONS_CITATION);
        for (Node nodeCitation : citations) {
            final String type = nodeCitation.valueOf(Constants.XmlAttribute.TYPE);
            final List<Node> citationParts = nodeCitation.selectNodes(Constants.XmlNode.STRING);
            final List<String> parts = new ArrayList<>(citationParts.size());
            for (Node nodePart : citationParts) {
                parts.add(nodePart.getStringValue());
            }
            this.citations.put(type, parts);
        }
    }

    /**
     * Retrieves name of multipart separator (it's constructed with concatenating of name and type)
     * @param parentNode parent tag in xml {@link Constants.XmlNode},
     *                   where multipart separator is situated
     * @param multipartSeparatorNodeName tag of multipart separator in xml {@link Constants.XmlNode}
     * @return name of multipart separator (concatenation of name and type of multipart separator)
     */
    protected String getMultipartSeparatorName(Node parentNode, String multipartSeparatorNodeName) {
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

    /**
     * Fills citation part of field type with separators
     * @param fieldsNodes represents citation part of field type
     * @return map of {@link Field} objects for concrete type,
     * which are stored under type keys
     */
    protected Map<String, Field> getField(List<Node> fieldsNodes) {
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

    /**
     * Fills parts of citation for using them after
     */
    protected abstract void fillCitationParts();

}