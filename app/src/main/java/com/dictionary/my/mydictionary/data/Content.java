package com.dictionary.my.mydictionary.data;

/**
 * Created by luxso on 29.09.2017.
 */

public interface Content {
    String DB_NAME = "MyDictionaryDataBase";
    int DB_VERSION = 8;

    String TABLE_GROUPS = "all_groups";
    String COLUMN_ROWID = "rowid";
    String COLUMN_TITLE = "title";

    String COLUMN_TITLE_WITHOUT_GROUP_ITEM = "Without group";



    String TABLE_ALL_WORD = "all_word";
    String COLUMN_ENG = "eng";
    String COLUMN_RUS = "rus";
    String COLUMN_NOTE = "note";
    String COLUMN_TRANSCRIPTION = "transcription";
    String COLUMN_SOUND = "sound";
    String COLUMN_PART_OF_SPEECH = "part_of_speech";
    String COLUMN_PREVIEW_IMAGE = "preview_image";
    String COLUMN_IMAGE = "image";
    String COLUMN_DEFINITION = "definition";
    String COLUMN_EXAMPLES = "examples";
    String COLUMN_ALTERNATIVE = "alternative";
    String COLUMN_DATE = "date";
    String COLUMN_GROUP_ID = "group_id";
    String COLUMN_COUNT_OF_RIGHT_ANSWER = "count_of_right_answer";

    String ARRAY_SEPARATOR = "___,___";



    String TABLE_TRAININGS = "training_words";
    String COLUMN_TRAININGS = "training";
    String COLUMN_TRAINING_WORDS_ID = "words_Id";
    String COLUMN_TRAINING_COUNT_OF_WORDS = "count_of_words";

    String COLUMN_TRAININGS_ITEM_ENG_RUS = "eng_rus";
    String COLUMN_TRAININGS_ITEM_RUS_ENG = "rus_eng";
    String COLUMN_TRAININGS_ITEM_CONSTRUCTOR = "constructor";
    String COLUMN_TRAININGS_ITEM_SPRINT = "sprint";
    String COLUMN_TRAININGS_ITEM_FOR_ALL = "for_all";

}
