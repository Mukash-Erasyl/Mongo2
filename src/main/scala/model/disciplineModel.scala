package model

import org.json4s.JsonAST.JString
import org.json4s.{CustomSerializer, DefaultFormats, Formats, MappingException}
import org.mongodb.scala.bson.BsonString


object TypeOfDiscipline extends Enumeration {
  type TypeOfDiscipline = Value
  val Required, Specialized, Electoral , Unknown = Value

  def toBsonStringDiscipline(disciplineType: TypeOfDiscipline): Option[BsonString] = {
    for(x<-TypeOfDiscipline.values){
      if(x==disciplineType){
        return Some(BsonString(disciplineType.toString))
      }
    }
    Some(BsonString("Unknown"))
  }

}


object Languages extends Enumeration {
  type Languages = Value
  val English, Russian, Kazakh , Unknown = Value

  def toBsonStringLanguage(languageType: Languages): Option[BsonString] = {
    for (x <- Languages.values) {
      if (x == languageType) {
        return Some(BsonString(languageType.toString))
      }
    }
    Some(BsonString("Unknown"))
  }
}



object EnumSerializer extends CustomSerializer[Enumeration#Value](format => (
  {
    case JString(s) =>
      // Проверяем, есть ли среди значений PaymentType
      if (TypeOfDiscipline.values.exists(_.toString == s)) {
        TypeOfDiscipline.withName(s)
      } else if (Languages.values.exists(_.toString == s)) {
        Languages.withName(s)
      } else {
        throw new MappingException(s"Unknown enumeration value: $s")
      }
    case value =>
      throw new MappingException(s"Can't convert $value to Enumeration")
  },
  {
    case enumValue: Enumeration#Value =>
      JString(enumValue.toString)
  }
))



object JsonFormatsDiscipline {
  implicit val formats: Formats = DefaultFormats + EnumSerializer
}


case class Discipline(
                       disciplineId: Int,
                       disciplineName: String,
                       description: String,
                       credits: Int,
                       hours: Int,
                       disciplineType: TypeOfDiscipline.TypeOfDiscipline,
                       teacherIds: List[Int],
                       department: String,
                       studentIds: List[Int],
                       topics: List[String],
                       classrooms: List[Int],
                       language: Languages.Languages,
                       scheduleIds: List[Int]
                     )

