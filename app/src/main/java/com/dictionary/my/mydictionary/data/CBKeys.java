package com.dictionary.my.mydictionary.data;

/**
 * Keys for CouchBase lite database
 */

public interface CBKeys {
    String KEY_ID = "_id";
    String KEY_TYPE = "type";

    String DB_WORDS = "dbWords";
    String WORD_TYPE = "word";
    String KEY_ENG = "eng";
    String KEY_RUS = "rus";
    String KEY_NOTE = "note";
    String KEY_TRANSCRIPTION = "transcription";
    String KEY_SOUND = "sound";
    String KEY_PART_OF_SPEECH = "part_of_speech";
    String KEY_PREVIEW_IMAGE = "preview_image";
    String KEY_IMAGE = "image";
    String KEY_DEFINITION = "definition";
    String KEY_EXAMPLES = "examples";
    String KEY_ALTERNATIVE = "alternative";
    String KEY_DATE = "date";
    String KEY_GROUP_ID = "group_id";
    String KEY_COUNT_OF_RIGHT_ANSWER = "count_of_right_answer";

    String DB_GROUPS = "dbGroups";
    String GROUP_TYPE = "group";
    String KEY_TITLE = "title";

    String DEFAULT_GROUP = "Without group";

}
