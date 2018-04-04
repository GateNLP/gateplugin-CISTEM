package gate.creole;

import gate.Annotation;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;
import weissweiler.leonie.Cistem;

@CreoleResource(name = "CISTEM (German Stemmer)")
public class CISTEM extends AbstractLanguageAnalyser {

  private static final long serialVersionUID = 7038059455779146803L;

  private String annotationSetName;

  private String tokenAnnotationType;

  private Boolean caseSensitive;

  private Boolean rootOnly;

  public Boolean getRootOnly() {
    return rootOnly;
  }

  @RunTime
  @CreoleParameter(defaultValue = "true")
  public void setRootOnly(Boolean rootOnly) {
    this.rootOnly = rootOnly;
  }

  public String getAnnotationSetName() {
    return annotationSetName;
  }

  @Optional
  @RunTime
  @CreoleParameter
  public void setAnnotationSetName(String annotationSetName) {
    this.annotationSetName = annotationSetName;
  }

  public String getTokenAnnotationType() {
    return tokenAnnotationType;
  }

  @RunTime
  @CreoleParameter(defaultValue = "Token")
  public void setTokenAnnotationType(String tokenAnnotationType) {
    this.tokenAnnotationType = tokenAnnotationType;
  }

  public Boolean getCaseSensitive() {
    return caseSensitive;
  }

  @RunTime
  @CreoleParameter(defaultValue = "true")
  public void setCaseSensitive(Boolean caseSensitive) {
    this.caseSensitive = caseSensitive;
  }

  @Override
  public void execute() throws ExecutionException {
    for(Annotation token : document.getAnnotations(annotationSetName)
        .get(tokenAnnotationType)) {

      String text = gate.Utils.cleanStringFor(document, token);

      if(rootOnly) {
        // if we just want the morphological root then call stem
        String stem = Cistem.stem(text, caseSensitive);

        token.getFeatures().put("root", stem);
      } else {
        // if we want the stem and affix (which can be glued back together) then
        // call segment
        String[] result = Cistem.segment(text, caseSensitive);

        token.getFeatures().put("stem", result[0]);
        token.getFeatures().put("affix", result[1]);
      }
    }
  }
}
