package com.dynamic.search.jpa.search;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * A functional interface represents Query Operators that will be used to build a Specification.
 */
@FunctionalInterface
interface SpecificationOperator {

    /**
     * @param from      Represents {@code javax.persistence.Criteria.Root} or {@code javax.persistence.Criteria.Join}
     * @param cb        Represents {@code CriteriaBuilder}
     * @param attribute Represents entity as a {@code String}
     * @param values    Represents operation's values
     * @return {@code Predicate}
     */
    Predicate apply(Path<?> from, CriteriaBuilder cb, String attribute, Comparable values);


    /**
     * Represents equality function
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator eq() {
        return (Path<?> from, CriteriaBuilder cb, String attribute, Comparable values) ->
                cb.equal(from.get(attribute), values);
    }

    /**
     * Represents not equal function
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator notEq() {
        return (Path<?> from, CriteriaBuilder cb, String attribute, Comparable values) ->
                cb.notEqual(from.get(attribute), values);
    }

    /**
     * Represents greater than function
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator gt() {
        return (Path<?> from, CriteriaBuilder cb, String attribute, Comparable values) ->
                cb.greaterThan(from.get(attribute), values);
    }

    /**
     * Represents greater than or equal to function
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator ge() {
        return (Path<?> from, CriteriaBuilder cb, String attribute, Comparable values) ->
                cb.greaterThanOrEqualTo(from.get(attribute), values);
    }

    /**
     * Represents less than function
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator lt() {
        return (Path<?> from, CriteriaBuilder cb, String attribute, Comparable values) ->
                cb.lessThan(from.get(attribute), values);
    }

    /**
     * Represents less than or equal to function
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator le() {
        return (Path<?> from, CriteriaBuilder cb, String attribute, Comparable values) ->
                cb.lessThanOrEqualTo(from.get(attribute), values);
    }

    /**
     * Represents like function
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator like() {
        return (Path<?> from, CriteriaBuilder cb, String attribute, Comparable values) -> cb.like(cb.lower(from.get(attribute)), String.format("%%%s%%", values).toLowerCase());
    }

    /**
     * Represents like function with start
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator likeStart() {
        return (Path<?> from, CriteriaBuilder cb, String attribute, Comparable values) -> cb.like(
                cb.lower(from.get(attribute)),
                "%" + values);
    }

    /**
     * Represents like function with end
     *
     * @return {@link SpecificationOperator}
     */
    static SpecificationOperator likeEnd() {
        return (Path<?> from, CriteriaBuilder cb, String attribute, Comparable values) -> cb.like(
                cb.lower(from.get(attribute)),
                values + "%");
    }

}
