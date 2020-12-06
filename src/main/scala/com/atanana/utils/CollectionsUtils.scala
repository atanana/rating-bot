package com.atanana.utils

object CollectionsUtils {

  implicit class EitherSet[A, B](set: Set[Either[A, B]]) {

    def unwrap(): Either[A, Set[B]] =
      set.foldRight(Right(Set.empty): Either[A, Set[B]]) {
        (e, acc) => for (xs <- acc; x <- e) yield xs + x
      }
  }

}