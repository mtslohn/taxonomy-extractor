package br.ufsc.egc.curriculumextractor.conf;

public enum ServiceProperty {

    DBPEDIA_TDB_SCHEMA_FOLDER("dbpedia.tdb.schema.folder"),
    DBPEDIA_FILE_CATEGORIES_LABELS("dbpedia.file.categories.labels"),
    DBPEDIA_FILE_CATEGORIES_SKOS("dbpedia.file.categories.skos"),

    AGROVOC_TDB_SCHEMA_FOLDER("agrovoc.tdb.schema.folder"),
    AGROVOC_FILE_THESAURUS("agrovoc.file.thesaurus");

    final String propertyName;

    ServiceProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }

}
