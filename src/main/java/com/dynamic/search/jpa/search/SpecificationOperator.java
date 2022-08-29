package com.dynamic.search.jpa.search;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * A functional interface represents Query Operators that will be used to build a Specification.
 */
@SuppressWarnings("ALL")
@FunctionalInterface
interface SpecificationOperator {


    /**
     * Represents equality function
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator eq() {
        return (Path<?> path, CriteriaBuilder cb, String attribute, Comparable value) ->
                cb.equal(path.get(attribute), value);
    }

    /**
     * Represents not equal function
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator notEq() {
        return (Path<?> path, CriteriaBuilder cb, String attribute, Comparable value) ->
                cb.notEqual(path.get(attribute), value);
    }

    /**
     * Represents greater than function
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator gt() {
        return (Path<?> path, CriteriaBuilder cb, String attribute, Comparable value) ->
                cb.greaterThan(path.get(attribute), value);
    }

    /**
     * Represents greater than or equal to function
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator ge() {
        return (Path<?> path, CriteriaBuilder cb, String attribute, Comparable value) ->
                cb.greaterThanOrEqualTo(path.get(attribute), value);
    }

    /**
     * Represents less than function
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator lt() {
        return (Path<?> path, CriteriaBuilder cb, String attribute, Comparable value) ->
                cb.lessThan(path.get(attribute), value);
    }

    /**
     * Represents less than or equal to function
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator le() {
        return (Path<?> path, CriteriaBuilder cb, String attribute, Comparable value) ->
                cb.lessThanOrEqualTo(path.get(attribute), value);
    }

    /**
     * Represents like function
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator like() {
        return (Path<?> path, CriteriaBuilder cb, String attribute, Comparable value) ->
                cb.like(cb.lower(path.get(attribute)), String.format("%%%s%%", value).toLowerCase());
    }

    /**
     * Represents like function with start
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator likeStart() {
        return (Path<?> path, CriteriaBuilder cb, String attribute, Comparable value) -> cb.like(
                cb.lower(path.get(attribute)), value.toString().toLowerCase() + "%");

    }

    /**
     * Represents like function with end
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator likeEnd() {
        return (Path<?> path, CriteriaBuilder cb, String attribute, Comparable value) -> cb.like(
                cb.lower(path.get(attribute)), "%" + value.toString().toLowerCase());
    }

    /**
     * @param path      Represents {@code javax.persistence.Criteria.Root}
     * @param cb        Represents {@code CriteriaBuilder}
     * @param attribute Represents name of the attribute that will be searched
     * @param value     Represents operation's value
     * @return {@link Predicate}
     */
    Predicate apply(Path<?> path, CriteriaBuilder cb, String attribute, Comparable value);

}
