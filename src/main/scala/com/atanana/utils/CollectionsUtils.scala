package com.atanana.utils

object CollectionsUtils {

  def eitherSet[A, B](s: Set[Either[A, B]]): Either[A, Set[B]] =
    s.foldRight(Right(Set.empty): Either[A, Set[B]]) {
      (e, acc) => for (xs <- acc; x <- e) yield xs + x
    }
}
