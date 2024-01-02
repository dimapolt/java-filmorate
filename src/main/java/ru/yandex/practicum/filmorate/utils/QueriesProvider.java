package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;

/**
 * Клас для хранения запросов в БД
 */
public class QueriesProvider {

    public static String SEARCH_BY_DIRECTOR = "SELECT f.film_id FROM films f WHERE f.film_id IN " +
                                               "(SELECT fd.film_id FROM film_director fd WHERE fd.director_id IN " +
                                               "(SELECT d.director_id FROM directors d " +
                                               "WHERE LOWER (d.name) LIKE '%%%s%%'))";
    public static String SEARCH_BY_TITLE = "SELECT f.film_id FROM films f WHERE LOWER (f.name) LIKE '%%%s%%'";
    public static String SEARCH_BY_DIRECTOR_AND_TITLE = SEARCH_BY_DIRECTOR + " UNION " + SEARCH_BY_TITLE;

    public static String getQuery(String query, String queryFlag) {
        switch (queryFlag) {
            case "director,title":
            case "title,director":
                                  return String.format(SEARCH_BY_DIRECTOR_AND_TITLE, query, query);
            case "director":      return String.format(SEARCH_BY_DIRECTOR, query);
            case "title":         return String.format(SEARCH_BY_TITLE, query);
            default:              throw new ValidationException("Передан неверный параметр для поиска!");
        }
    }

}