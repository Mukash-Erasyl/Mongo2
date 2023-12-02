package repository

import MongoDBConnection._
import model._
import org.mongodb.scala.Document
import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonInt32, BsonString, ObjectId}
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.util.Try;

object FacultyRepository {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  def getAllFaculties(): Future[List[Faculty]] = {
    val futureFaculties = MongoDBConnection.facultyCollection.find().toFuture()

    futureFaculties.map { docs =>
      Option(docs).map(_.map { doc =>
        Faculty(
          facultyId = doc.getInteger("facultyId"), // Переименовано
          name = doc.getString("name"),
          departmentsIds = Option(doc.getList("departmentsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
          studentsIds = Option(doc.getList("studentsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
          teachersIds = Option(doc.getList("teachersIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
          curriculumPlansIds = Option(doc.getList("curriculumPlansIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
          specialization = doc.getString("specialization"),
          creationDate = doc.getString("creationDate"),
          seats = doc.getInteger("seats")
        )
      }.toList).getOrElse(List.empty)
    }
  }

  def getFacultyById(facultyId: String): Future[Option[Faculty]] = {
    val facultyDocument = Document("_id" -> new ObjectId(facultyId))

    MongoDBConnection.facultyCollection.find(facultyDocument).headOption().map {
      case Some(doc) =>
        Some(
          Faculty(
            facultyId = doc.getInteger("facultyId"), // Переименовано
            name = doc.getString("name"),
            departmentsIds = Option(doc.getList("departmentsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
            studentsIds = Option(doc.getList("studentsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
            teachersIds = Option(doc.getList("teachersIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
            curriculumPlansIds = Option(doc.getList("curriculumPlansIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
            specialization = doc.getString("specialization"),
            creationDate = doc.getString("creationDate"),
            seats = doc.getInteger("seats")
          )
        )
      case None => None
    }
  }


  def findFacultyByParams(param: String): Future[List[Faculty]] = {
    val keyValue = param.split("=")

    if (keyValue.length == 2) {
      val key = keyValue(0)
      val value = Try(keyValue(1).toInt).toOption

      val facultyDocument = Document(key -> value)

      MongoDBConnection.facultyCollection
        .find(facultyDocument)
        .toFuture()
        .map { docs =>
          docs.map { doc =>
            Faculty(
              facultyId = doc.getInteger("facultyId"), // Переименовано
              name = doc.getString("name"),
              departmentsIds = Option(doc.getList("departmentsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
              studentsIds = Option(doc.getList("studentsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
              teachersIds = Option(doc.getList("teachersIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
              curriculumPlansIds = Option(doc.getList("curriculumPlansIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty), // Переименовано
              specialization = doc.getString("specialization"),
              creationDate = doc.getString("creationDate"),
              seats = doc.getInteger("seats")
            )
          }.toList
        }
    } else {
      // Обработка некорректного ввода
      Future.failed(new IllegalArgumentException("Неверный формат параметра"))
    }
  }


  def addFaculty(faculty: Faculty): Future[String] = {
    val facultyDocument = BsonDocument(
      "facultyId" -> BsonInt32(faculty.facultyId),
      "name" -> BsonString(faculty.name),
      "departmentsIds" -> BsonArray(faculty.departmentsIds.map(BsonInt32(_))),
      "studentsIds" -> BsonArray(faculty.studentsIds.map(BsonInt32(_))),
      "teachersIds" -> BsonArray(faculty.teachersIds.map(BsonInt32(_))),
      "curriculumPlansIds" -> BsonArray(faculty.curriculumPlansIds.map(BsonInt32(_))),
      "specialization" -> BsonString(faculty.specialization),
      "creationDate" -> BsonString(faculty.creationDate),
      "seats" -> BsonInt32(faculty.seats)
    )

    MongoDBConnection.facultyCollection.insertOne(facultyDocument).toFuture().map(_ => s"Факультет - ${faculty.name} был добавлен, проверь БД ;)")
  }

  def deleteFaculty(facultyId: String): Future[String] = {
    val facultyDocument = Document("_id" -> new ObjectId(facultyId))
    MongoDBConnection.facultyCollection.deleteOne(facultyDocument).toFuture().map(_ => s"Факультет с id ${facultyId} был удален, проверь БД ;)")
  }

  def updateFaculty(facultyId: String, updatedFaculty: Faculty): Future[String] = {
    val filter = Document("_id" -> new ObjectId(facultyId))

    val facultyDocument = BsonDocument(
      "$set" -> BsonDocument(
        "facultyId" -> BsonInt32(updatedFaculty.facultyId),
        "name" -> BsonString(updatedFaculty.name),
        "departmentsIds" -> BsonArray(updatedFaculty.departmentsIds.map(BsonInt32(_))),
        "studentsIds" -> BsonArray(updatedFaculty.studentsIds.map(BsonInt32(_))),
        "teachersIds" -> BsonArray(updatedFaculty.teachersIds.map(BsonInt32(_))),
        "curriculumPlansIds" -> BsonArray(updatedFaculty.curriculumPlansIds.map(BsonInt32(_))),
        "specialization" -> BsonString(updatedFaculty.specialization),
        "creationDate" -> BsonString(updatedFaculty.creationDate),
        "seats" -> BsonInt32(updatedFaculty.seats)
      )
    )

    MongoDBConnection.facultyCollection.updateOne(filter, facultyDocument).toFuture().map { updatedResult =>
      if (updatedResult.wasAcknowledged() && updatedResult.getModifiedCount > 0) {
        s"Факультет был обновлен, id: ${filter} - не знаю, что изменилось, проверяй в БД ;)"
      } else {
        "Обновление факультета не выполнено: Проблема либо в базе, либо в тебе ;)"
      }
    }
  }
}
