package com.boardwords.modal;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "word",
        "results",
        "syllables",
        "pronunciation",
        "frequency"
})
public class WordsApi {

    @JsonProperty("word")
    private String word;
    @JsonProperty("results")
    private List<Result> results = null;
    @JsonProperty("syllables")
    private Syllables syllables;
    @JsonProperty("pronunciation")
    private Pronunciation pronunciation;
    @JsonProperty("frequency")
    private Double frequency;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("word")
    public String getWord() {
        return word;
    }

    @JsonProperty("word")
    public void setWord(String word) {
        this.word = word;
    }

    @JsonProperty("results")
    public List<Result> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<Result> results) {
        this.results = results;
    }

    @JsonProperty("syllables")
    public Syllables getSyllables() {
        return syllables;
    }

    @JsonProperty("syllables")
    public void setSyllables(Syllables syllables) {
        this.syllables = syllables;
    }

    @JsonProperty("pronunciation")
    public Pronunciation getPronunciation() {
        return pronunciation;
    }

    @JsonProperty("pronunciation")
    public void setPronunciation(Pronunciation pronunciation) {
        this.pronunciation = pronunciation;
    }

    @JsonProperty("frequency")
    public Double getFrequency() {
        return frequency;
    }

    @JsonProperty("frequency")
    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "count",
            "list"
    })
    public class Syllables {

        @JsonProperty("count")
        private Integer count;
        @JsonProperty("list")
        private List<String> list = null;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        @JsonProperty("count")
        public Integer getCount() {
            return count;
        }

        @JsonProperty("count")
        public void setCount(Integer count) {
            this.count = count;
        }

        @JsonProperty("list")
        public List<String> getList() {
            return list;
        }

        @JsonProperty("list")
        public void setList(List<String> list) {
            this.list = list;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "all"
    })
    public class Pronunciation {

        @JsonProperty("all")
        private String all;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        @JsonProperty("all")
        public String getAll() {
            return all;
        }

        @JsonProperty("all")
        public void setAll(String all) {
            this.all = all;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "definition",
            "partOfSpeech",
            "synonyms",
            "typeOf",
            "examples",
            "hasTypes"
    })
    public class Result {

        @JsonProperty("definition")
        private String definition;
        @JsonProperty("partOfSpeech")
        private String partOfSpeech;
        @JsonProperty("synonyms")
        private List<String> synonyms = null;
        @JsonProperty("typeOf")
        private List<String> typeOf = null;
        @JsonProperty("examples")
        private List<String> examples = null;
        @JsonProperty("hasTypes")
        private List<String> hasTypes = null;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        @JsonProperty("definition")
        public String getDefinition() {
            return definition;
        }

        @JsonProperty("definition")
        public void setDefinition(String definition) {
            this.definition = definition;
        }

        @JsonProperty("partOfSpeech")
        public String getPartOfSpeech() {
            return partOfSpeech;
        }

        @JsonProperty("partOfSpeech")
        public void setPartOfSpeech(String partOfSpeech) {
            this.partOfSpeech = partOfSpeech;
        }

        @JsonProperty("synonyms")
        public List<String> getSynonyms() {
            return synonyms;
        }

        @JsonProperty("synonyms")
        public void setSynonyms(List<String> synonyms) {
            this.synonyms = synonyms;
        }

        @JsonProperty("typeOf")
        public List<String> getTypeOf() {
            return typeOf;
        }

        @JsonProperty("typeOf")
        public void setTypeOf(List<String> typeOf) {
            this.typeOf = typeOf;
        }

        @JsonProperty("examples")
        public List<String> getExamples() {
            return examples;
        }

        @JsonProperty("examples")
        public void setExamples(List<String> examples) {
            this.examples = examples;
        }

        @JsonProperty("hasTypes")
        public List<String> getHasTypes() {
            return hasTypes;
        }

        @JsonProperty("hasTypes")
        public void setHasTypes(List<String> hasTypes) {
            this.hasTypes = hasTypes;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }
}