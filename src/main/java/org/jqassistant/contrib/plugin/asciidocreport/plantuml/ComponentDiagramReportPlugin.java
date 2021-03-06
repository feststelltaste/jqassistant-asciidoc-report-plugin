package org.jqassistant.contrib.plugin.asciidocreport.plantuml;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.buschmais.jqassistant.core.analysis.api.Result;
import com.buschmais.jqassistant.core.analysis.api.rule.ExecutableRule;
import com.buschmais.jqassistant.core.report.api.AbstractReportPlugin;
import com.buschmais.jqassistant.core.report.api.ReportContext;
import com.buschmais.jqassistant.core.report.api.ReportException;
import com.buschmais.jqassistant.core.report.api.graph.SubGraphFactory;
import com.buschmais.jqassistant.core.report.api.graph.model.SubGraph;

public class ComponentDiagramReportPlugin extends AbstractReportPlugin {

    private PlantUMLRenderer plantUMLRenderer;

    private ReportContext reportContext;

    private File directory;

    @Override
    public void initialize() {
        plantUMLRenderer = new PlantUMLRenderer();
    }

    @Override
    public void configure(ReportContext reportContext, Map<String, Object> properties) {
        this.reportContext = reportContext;
        directory = reportContext.getReportDirectory("plantuml");
    }

    @Override
    public void setResult(Result<? extends ExecutableRule> result) throws ReportException {
        SubGraphFactory subGraphFactory = new SubGraphFactory();
        SubGraph subGraph = subGraphFactory.createSubGraph(result);
        String componentDiagram = plantUMLRenderer.createComponentDiagram(subGraph);
        File file = plantUMLRenderer.renderDiagram(componentDiagram, result.getRule(), directory);
        URL url;
        try {
            url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new ReportException("Cannot convert file '" + file.getAbsolutePath() + "' to URL");
        }
        reportContext.addReport("Component Diagram", result.getRule(), ReportContext.ReportType.IMAGE, url);
    }
}
