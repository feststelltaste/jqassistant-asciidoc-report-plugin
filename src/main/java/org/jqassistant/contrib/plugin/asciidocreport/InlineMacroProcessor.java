package org.jqassistant.contrib.plugin.asciidocreport;

import java.util.HashMap;
import java.util.Map;

import org.asciidoctor.ast.AbstractBlock;

public class InlineMacroProcessor extends org.asciidoctor.extension.InlineMacroProcessor {

    public static final String CONCEPT_REF = "conceptRef";
    public static final String CONSTRAINT_REF = "constraintRef";

    public InlineMacroProcessor() {
        super("jQA");
    }

    @Override
    public Object process(AbstractBlock parent, String target, Map<String, Object> attributes) {
        if (CONCEPT_REF.equals(target)) {
            DocumentParser documentParser = DocumentParser.parse(parent.getDocument());
            return processRef(parent, attributes, documentParser.getConceptBlocks());
        } else if (CONSTRAINT_REF.equals(target)) {
            DocumentParser documentParser = DocumentParser.parse(parent.getDocument());
            return processRef(parent, attributes, documentParser.getConstraintBlocks());
        }
        throw new IllegalArgumentException("Unknown jQAssistant macro '" + target + "'");
    }

    private Object processRef(AbstractBlock parent, Map<String, Object> attributes, Map<String, AbstractBlock> blocks) {
        Object rule = attributes.get("text");
        if (rule != null && blocks.containsKey(rule)) {
            Map<String, Object> options = new HashMap<>();
            options.put("type", ":link");
            options.put("target", "#" + rule);
            return createInline(parent, "anchor", rule.toString(), attributes, options).convert();
        }
        return rule;
    }

}
