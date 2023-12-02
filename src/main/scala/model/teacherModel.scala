package model

import org.json4s.JsonAST.JString
import org.json4s.{CustomSerializer, DefaultFormats, Formats, JsonAST, MappingException}


object Qualification extends Enumeration {
  type Qualification = Value
  val PhD, MeD, Certified = Value
}

object Position extends Enumeration {
  type Position = Value
  val Lecturer, Docent, Professor = Value
}


object QualificationFormat extends CustomSerializer[Qualification.Value](format => (
  {
    case JString(s) => Qualification.withName(s)
    case value => throw new MappingException(s"Can't convert $value to Statu")
  },
  {
    case qualification: Qualification.Value => JString(qualification.toString)
  }
))

object PositionFormat extends CustomSerializer[Position.Value](format => (
  {
    case JString(s) => Position.withName(s)
    case value => throw new MappingException(s"Can't convert $value to Statu")
  },
  {
    case position: Position.Value => JString(position.toString)
  }
))

object JsonFormatsTeaher {
  implicit val formats: Formats = DefaultFormats + PositionFormat + QualificationFormat;
}
case class Teacher(
                    id: Int,
                    disciplinesIds: List[Int],
                    education: String,
                    qualification: Qualification.Qualification,
                    experience: Int,
                    scheduleId: Int,
                    studentsIds: List[Int],
                    salary: Int,
                    newsId: Int,
                    materialsId: Int,
                    position: Position.Position,
                    awards: String,
                    certificationId: Int,
                    attestationId: Int
                  )



