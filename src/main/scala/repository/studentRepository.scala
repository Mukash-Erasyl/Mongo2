package repository

import MongoDBConnection._
import model._
import org.mongodb.scala.Document
import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonInt32, BsonString, ObjectId}

import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.util.Try

object StudentRepository {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  def getAllStudents(): Future[List[Student]] = {
    val futureStudents = MongoDBConnection.studentCollection.find().toFuture()
    futureStudents.map { docs =>
      Option(docs).map(_.map { doc =>
        Student(
          facultyId = doc.getInteger("faculty"),
          courseId = doc.getInteger("course"),
          specializationId = doc.getInteger("specialization"),
          scheduleId = doc.getInteger("scheduleId"),
          gradesIds = Option(doc.getList("gradesIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
          subjectsIds = Option(doc.getList("subjectsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
          teachersIds = Option(doc.getList("teachersIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
          status = Statu.withName(doc.getString("status"))
        )
      }.toList).getOrElse(List.empty)
    }
  }

  def getSortedStudent(): Future[List[Student]] = {
    val futureStudents = MongoDBConnection.studentCollection.find().toFuture()
    futureStudents.map { docs =>
      Option(docs).map(_.map { doc =>
        Student(
          facultyId = doc.getInteger("faculty"),
          courseId = doc.getInteger("course"),
          specializationId = doc.getInteger("specialization"),
          scheduleId = doc.getInteger("scheduleId"),
          gradesIds = Option(doc.getList("gradesIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
          subjectsIds = Option(doc.getList("subjectsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
          teachersIds = Option(doc.getList("teachersIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
          status = Statu.withName(doc.getString("status"))
        )
      }.toList.sortBy(_.scheduleId)).getOrElse(List.empty)
    }
  }

  def getStudentById(studentId: String): Future[Option[Student]] = {
    val studentDocument = Document("_id" -> new ObjectId(studentId))

    MongoDBConnection.studentCollection.find(studentDocument).headOption().map {
      case Some(doc) =>
        Some(
          Student(
            facultyId = doc.getInteger("faculty"),
            courseId = doc.getInteger("course"),
            specializationId = doc.getInteger("specialization"),
            scheduleId = doc.getInteger("scheduleId"),
            gradesIds = Option(doc.getList("gradesIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            subjectsIds = Option(doc.getList("subjectsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            teachersIds = Option(doc.getList("teachersIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            status = Statu.withName(doc.getString("status"))
          )
        )
      case None => None
    }
  }


  def findStudentsByParams(param: String): Future[List[Student]] = {
    val keyValue = param.split("=")

    if (keyValue.length == 2) {
      val key = keyValue(0)
      val value = Try(keyValue(1).toInt).toOption;

      val studentDocument = Document(key -> value)

      MongoDBConnection.studentCollection.find(studentDocument).toFuture().map { docs =>
        docs.map { doc =>
          Student(
            facultyId = doc.getInteger("faculty"),
            courseId = doc.getInteger("course"),
            specializationId = doc.getInteger("specialization"),
            scheduleId = doc.getInteger("scheduleId"),
            gradesIds = Option(doc.getList("gradesIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            subjectsIds = Option(doc.getList("subjectsIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            teachersIds = Option(doc.getList("teachersIds", classOf[Integer])).map(_.asScala.map(_.toInt).toList).getOrElse(List.empty),
            status = Statu.withName(doc.getString("status"))
          )
        }.toList
      }
    } else {
      // Обработка некорректного ввода
      Future.failed(new IllegalArgumentException("Неверный формат параметра"))
    }
  }

  def addStudent(student: Student): Future[String] = {
    val studentDocument = BsonDocument(
      "faculty" -> BsonInt32(student.facultyId),
      "course" -> BsonInt32(student.courseId),
      "specialization" -> BsonInt32(student.specializationId),
      "scheduleId" -> BsonInt32(student.scheduleId),
      "gradesIds" -> BsonArray(student.gradesIds.map(BsonInt32(_))),
      "subjectsIds" -> BsonArray(student.subjectsIds.map(BsonInt32(_))),
      "teachersIds" -> BsonArray(student.teachersIds.map(BsonInt32(_))),
      "status" -> BsonString(student.status.toString)
    )

    MongoDBConnection.studentCollection.insertOne(studentDocument).toFuture().map(_ => s"Студент с id ${student.scheduleId} был добавлен, проверь БД ;)")
  }

  def deleteStudent(studentId: String): Future[String] = {
    val studentDocument = Document("_id" -> new ObjectId(studentId))
    MongoDBConnection.studentCollection.deleteOne(studentDocument).toFuture().map(_ => s"Студент с id ${studentId} был удален, проверь БД ;)")
  }

  def updateStudent(studentId: String, updatedStudent: Student): Future[String] = {
    val filter = Document("_id" -> new ObjectId(studentId))

    val studentDocument = BsonDocument(
      "$set" -> BsonDocument(
        "faculty" -> BsonInt32(updatedStudent.facultyId),
        "course" -> BsonInt32(updatedStudent.courseId),
        "specialization" -> BsonInt32(updatedStudent.specializationId),
        "scheduleId" -> BsonInt32(updatedStudent.scheduleId),
        "gradesIds" -> BsonArray(updatedStudent.gradesIds.map(BsonInt32(_))),
        "subjectsIds" -> BsonArray(updatedStudent.subjectsIds.map(BsonInt32(_))),
        "teachersIds" -> BsonArray(updatedStudent.teachersIds.map(BsonInt32(_))),
        "status" -> BsonString(updatedStudent.status.toString)
      )
    )

    MongoDBConnection.studentCollection.updateOne(filter, studentDocument).toFuture().map { updatedResult =>
      if (updatedResult.wasAcknowledged() && updatedResult.getModifiedCount > 0) {
        s"Студент был обновлен, id: ${filter} - не знаю, что изменилось, проверяй в БД ;)"
      } else {
        "Обновление студента не выполнено: Проблема либо в базе, либо в тебе ;)"
      }
    }
  }
}
