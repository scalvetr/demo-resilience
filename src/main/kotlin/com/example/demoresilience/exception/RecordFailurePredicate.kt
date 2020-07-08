package com.example.demoresilience.exception

import java.util.function.Predicate

class RecordFailurePredicate : Predicate<Throwable> {
    override fun test(throwable: Throwable): Boolean {
        return throwable !is BusinessException
    }
}