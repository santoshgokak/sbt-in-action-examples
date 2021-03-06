package org.usedkittens;

import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import org.scalacheck._
import org.scalacheck.Gen._
import org.scalacheck.Arbitrary.arbitrary

object LogicSpecification extends Properties("Logic") {
  val genKitten: Gen[Kitten] = for {
    attributes <- Gen.containerOf[Set,String](Gen.alphaStr)
  } yield Kitten("id", attributes)

  val genBuyerPreferences: Gen[BuyerPreferences] = (for {
    attributes <- Gen.containerOf[Set,String](Gen.alphaStr)
  } yield BuyerPreferences(attributes))

  property("matchLikelihood") = forAll(genKitten, genBuyerPreferences)((a: Kitten, b: BuyerPreferences) => {
    if (b.attributes.size == 0) true
    else {
      val num = b.attributes.toList.map{x => if (a.attributes.contains(x)) 1.0 else 0.0}.sum
      num / b.attributes.size - Logic.matchLikelihood(a, b) < 0.001
    }
  })
}
