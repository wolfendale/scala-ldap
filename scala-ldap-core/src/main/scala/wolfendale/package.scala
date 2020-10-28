import scodec._
import scodec.bits.BitVector

package object wolfendale {

  def maybe[A](codec: Codec[A]): Codec[Option[A]] =
    codecs.choice(
      codec.exmap(
        { value            => Attempt.successful(Some(value)) },
        { case Some(value) => Attempt.successful(value)
          case None        => Attempt.failure(Err("Recovered"))
        }
      ),
      codecs.provide(None)
    )

  def withNullValue[A](codec: Codec[A], nullish: A): Codec[A] =
    new Codec[A] {

      private val decoder =
        codecs.withDefaultValue(maybe(codec), nullish)

      override def sizeBound: SizeBound =
        codec.sizeBound | SizeBound.exact(0)

      override def decode(bits: BitVector): Attempt[DecodeResult[A]] =
        decoder.decode(bits)

      override def encode(value: A): Attempt[BitVector] =
        if (value == nullish) Attempt.successful(BitVector.empty) else {
          codec.encode(value)
        }
    }
}
