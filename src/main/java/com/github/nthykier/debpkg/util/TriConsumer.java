package com.github.nthykier.debpkg.util;

public interface TriConsumer<A, B, C> {
    void accept(A a, B b, C c);
}
