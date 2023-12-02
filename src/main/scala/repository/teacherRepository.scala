package repository


import MongoDBConnection._
import model._
import org.mongodb.scala.Document
import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonInt32, BsonString, ObjectId}
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.util.Try;

object TeacherRepository {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  def getAllTeachers(): Future[List[Teacher]] = {
    val futureTeachers = MongoDBConnection.teacherCollection.find().toFuture()

    futureTeachers.map { docs =>
      Option(docs).map(_.map { doc =>
        Teacher(
          id = doc.getInteger("id"),
          disciplinesIds = Option(doc.getList("DisciplinesID", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
          education = doc.getString("Education"),
          qualification = Qualification.withName(doc.getString("Qualification")),
          experience = doc.getInteger("Experience"),
          scheduleId = doc.getInteger("ScheduleId"),
          studentsIds = Option(doc.getList("StudentsID", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
          salary = doc.getInteger("Salary"),
          newsId = doc.getInteger("NewsID"),
          materialsId = doc.getInteger("MaterialsId"),
          position = Position.withName(doc.getString("Position")),
          awards = doc.getString("Awards"),
          certificationId = doc.getInteger("CertificationId"),
          attestationId = doc.getInteger("АттестацияID")
        )
      }.toList).getOrElse(List.empty)
    }
  }

  def getTeacherById(teacherId: String): Future[Option[Teacher]] = {
    val teacherDocument = Document("_id" -> new ObjectId(teacherId))

    MongoDBConnection.teacherCollection.find(teacherDocument).headOption().map {
      case Some(doc) =>
        Some(
          Teacher(
            id = doc.getInteger("id"),
            disciplinesIds = Option(doc.getList("DisciplinesID", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            education = doc.getString("Education"),
            qualification = Qualification.withName(doc.getString("Qualification")),
            experience = doc.getInteger("Experience"),
            scheduleId = doc.getInteger("ScheduleId"),
            studentsIds = Option(doc.getList("StudentsID", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            salary = doc.getInteger("Salary"),
            newsId = doc.getInteger("NewsID"),
            materialsId = doc.getInteger("MaterialsId"),
            position = Position.withName(doc.getString("Position")),
            awards = doc.getString("Awards"),
            certificationId = doc.getInteger("CertificationId"),
            attestationId = doc.getInteger("АттестацияID")
          )
        )
      case None => None
    }
  }

  def addTeacher(teacher: Teacher): Future[String] = {
    val teacherDocument = BsonDocument(
      "id" -> BsonInt32(teacher.id),
      "DisciplinesID" -> BsonArray(teacher.disciplinesIds.map(BsonInt32(_))),
      "Education" -> BsonString(teacher.education),
      "Qualification" -> BsonString(teacher.qualification.toString),
      "Experience" -> BsonInt32(teacher.experience),
      "ScheduleId" -> BsonInt32(teacher.scheduleId),
      "StudentsID" -> BsonArray(teacher.studentsIds.map(BsonInt32(_))),
      "Salary" -> BsonInt32(teacher.salary),
      "NewsID" -> BsonInt32(teacher.newsId),
      "MaterialsId" -> BsonInt32(teacher.materialsId),
      "Position" -> BsonString(teacher.position.toString),
      "Awards" -> BsonString(teacher.awards),
      "CertificationId" -> BsonInt32(teacher.certificationId),
      "АттестацияID" -> BsonInt32(teacher.attestationId)
    )

    MongoDBConnection.teacherCollection.insertOne(teacherDocument).toFuture().map(_ =>
      s"Преподаватель - ${teacher.id} был добавлен, проверь БД ;)"
    )
  }


  def findTeacherByParams(param: String): Future[Option[Teacher]] = {
    val keyValue = param.split("=")

    if (keyValue.length == 2) {
      val key = keyValue(0)
      val value = Try(keyValue(1).toInt).toOption

      val teacherDocument = Document(key -> value)

      MongoDBConnection.teacherCollection.find(teacherDocument).headOption().map {
        case Some(doc) =>
          Some(
            Teacher(
              id = doc.getInteger("id"),
              disciplinesIds = Option(doc.getList("DisciplinesID", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
              education = doc.getString("Education"),
              qualification = Qualification.withName(doc.getString("Qualification")),
              experience = doc.getInteger("Experience"),
              scheduleId = doc.getInteger("ScheduleId"),
              studentsIds = Option(doc.getList("StudentsID", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
              salary = doc.getInteger("Salary"),
              newsId = doc.getInteger("NewsID"),
              materialsId = doc.getInteger("MaterialsId"),
              position = Position.withName(doc.getString("Position")),
              awards = doc.getString("Awards"),
              certificationId = doc.getInteger("CertificationId"),
              attestationId = doc.getInteger("АттестацияID")
            )
          )
        case None => None
      }

    }

    else {
      // Обработка некорректного ввода
      Future.failed(new IllegalArgumentException("Неверный формат параметра"))
    }
  }


  def deleteTeacher(teacherId: String): Future[String] = {
    val teacherDocument = Document("_id" -> new ObjectId(teacherId))

    MongoDBConnection.teacherCollection.deleteOne(teacherDocument).toFuture().map(_ =>
      s"Преподаватель с id ${teacherId} был удален, проверь БД ;)"
    )
  }

  def updateTeacher(teacherId: String, updatedTeacher: Teacher): Future[String] = {
    val filter = Document("_id" -> new ObjectId(teacherId))

    val teacherDocument = BsonDocument(
      "$set" -> BsonDocument(
        "id" -> BsonInt32(updatedTeacher.id),
        "DisciplinesID" -> BsonArray(updatedTeacher.disciplinesIds.map(BsonInt32(_))),
        "Education" -> BsonString(updatedTeacher.education),
        "Qualification" -> BsonString(updatedTeacher.qualification.toString),
        "Experience" -> BsonInt32(updatedTeacher.experience),
        "ScheduleId" -> BsonInt32(updatedTeacher.scheduleId),
        "StudentsID" -> BsonArray(updatedTeacher.studentsIds.map(BsonInt32(_))),
        "Salary" -> BsonInt32(updatedTeacher.salary),
        "NewsID" -> BsonInt32(updatedTeacher.newsId),
        "MaterialsId" -> BsonInt32(updatedTeacher.materialsId),
        "Position" -> BsonString(updatedTeacher.position.toString),
        "Awards" -> BsonString(updatedTeacher.awards),
        "CertificationId" -> BsonInt32(updatedTeacher.certificationId),
        "АттестацияID" -> BsonInt32(updatedTeacher.attestationId)
      )
    )

    MongoDBConnection.teacherCollection.updateOne(filter, teacherDocument).toFuture().map { updatedResult =>
      if (updatedResult.wasAcknowledged() && updatedResult.getModifiedCount > 0) {
        s"Преподаватель был обновлен, id: ${filter} - проверь в БД ;)"
      } else {
        "Обновление преподавателя не выполнено: Проблема либо в базе, либо в тебе ;)"
      }
    }
  }

}
