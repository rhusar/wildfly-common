/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2016 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wildfly.common.function;

import org.wildfly.common.Assert;

/**
 * A two-argument function which can throw an exception.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
@FunctionalInterface
public interface ExceptionBiFunction<T, U, R, E extends Exception> {
    /**
     * Applies this function to the given arguments.
     *
     * @param t the first argument
     * @param u the second argument
     * @return the function result
     * @throws E if an exception occurs
     */
    R apply(T t, U u) throws E;

    default <R2> ExceptionBiFunction<T, U, R2, E> andThen(ExceptionFunction<? super R, ? extends R2, ? extends E> after) {
        Assert.checkNotNullParam("after", after);
        return (t, u) -> after.apply(apply(t, u));
    }

    default ExceptionBiConsumer<T, U, E> andThen(ExceptionConsumer<R, ? extends E> after) {
        Assert.checkNotNullParam("after", after);
        return (t, u) -> after.accept(apply(t, u));
    }

    default ExceptionSupplier<R, E> compose(ExceptionSupplier<? extends T, ? extends E> before1, ExceptionSupplier<? extends U, ? extends E> before2) {
        Assert.checkNotNullParam("before1", before1);
        Assert.checkNotNullParam("before2", before2);
        return () -> apply(before1.get(), before2.get());
    }
}
